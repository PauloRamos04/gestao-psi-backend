package com.gestaopsi.prd.service;

import com.gestaopsi.prd.entity.Paciente;
import com.gestaopsi.prd.entity.Sessao;
import com.gestaopsi.prd.exception.InvalidDataException;
import com.gestaopsi.prd.repository.PacienteRepository;
import com.gestaopsi.prd.repository.PsicologoRepository;
import com.gestaopsi.prd.repository.SessaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportService {

    private final PacienteRepository pacienteRepository;
    private final SessaoRepository sessaoRepository;
    private final PsicologoRepository psicologoRepository;

    /**
     * Importa pacientes de um arquivo CSV
     * Formato esperado: nome,email,telefone,cpf,dataNascimento
     */
    @Transactional
    public Map<String, Object> importPacientes(MultipartFile file, Long clinicaId) {
        validateCSVFile(file);
        
        List<String> errors = new ArrayList<>();
        List<Paciente> imported = new ArrayList<>();
        int lineNumber = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = CSVFormat.Builder.create()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreHeaderCase(true)
                .setTrim(true)
                .build()
                .parse(reader)) {

            for (CSVRecord record : csvParser) {
                lineNumber++;
                try {
                    Paciente paciente = new Paciente();
                    paciente.setNome(record.get("nome"));
                    paciente.setEmail(record.get("email"));
                    paciente.setTelefone(record.get("telefone"));
                    paciente.setCpf(record.get("cpf"));
                    
                    String dataNascStr = record.get("dataNascimento");
                    if (dataNascStr != null && !dataNascStr.isEmpty()) {
                        paciente.setDataNascimento(LocalDate.parse(dataNascStr, 
                            DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    }
                    
                    // Validações
                    if (paciente.getNome() == null || paciente.getNome().isEmpty()) {
                        throw new InvalidDataException("Nome é obrigatório");
                    }
                    
                    // Verifica duplicidade de CPF
                    if (paciente.getCpf() != null && !paciente.getCpf().isEmpty()) {
                        if (pacienteRepository.existsByCpf(paciente.getCpf())) {
                            throw new InvalidDataException("CPF já cadastrado: " + paciente.getCpf());
                        }
                    }
                    
                    // Verifica duplicidade de email
                    if (paciente.getEmail() != null && !paciente.getEmail().isEmpty()) {
                        if (pacienteRepository.existsByEmail(paciente.getEmail())) {
                            throw new InvalidDataException("Email já cadastrado: " + paciente.getEmail());
                        }
                    }

                    pacienteRepository.save(paciente);
                    imported.add(paciente);
                    
                } catch (Exception e) {
                    errors.add("Linha " + lineNumber + ": " + e.getMessage());
                    log.error("Erro ao importar paciente na linha {}: {}", lineNumber, e.getMessage());
                }
            }

        } catch (Exception e) {
            log.error("Erro ao processar arquivo CSV", e);
            throw new RuntimeException("Erro ao processar arquivo CSV: " + e.getMessage());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalProcessed", lineNumber);
        result.put("totalImported", imported.size());
        result.put("totalErrors", errors.size());
        result.put("errors", errors);

        log.info("Importação de pacientes concluída: {} importados, {} erros", imported.size(), errors.size());
        return result;
    }

    /**
     * Importa sessões de um arquivo CSV
     * Formato esperado: data,horario,pacienteCpf,psicologoId,valor,status
     */
    @Transactional
    public Map<String, Object> importSessoes(MultipartFile file) {
        validateCSVFile(file);
        
        List<String> errors = new ArrayList<>();
        List<Sessao> imported = new ArrayList<>();
        int lineNumber = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = CSVFormat.Builder.create()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreHeaderCase(true)
                .setTrim(true)
                .build()
                .parse(reader)) {

            for (CSVRecord record : csvParser) {
                lineNumber++;
                try {
                    Sessao sessao = new Sessao();
                    
                    // Data
                    String dataStr = record.get("data");
                    sessao.setData(LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    
                    // Horário (campo correto é 'hora')
                    String horarioStr = record.get("horario");
                    sessao.setHora(LocalTime.parse(horarioStr, DateTimeFormatter.ofPattern("HH:mm")));
                    
                    // Paciente
                    String pacienteCpf = record.get("pacienteCpf");
                    Paciente paciente = pacienteRepository.findByCpf(pacienteCpf)
                        .orElseThrow(() -> new InvalidDataException("Paciente não encontrado com CPF: " + pacienteCpf));
                    sessao.setPaciente(paciente);
                    
                    // Psicólogo
                    Long psicologoId = Long.parseLong(record.get("psicologoId"));
                    var psicologo = psicologoRepository.findById(psicologoId)
                        .orElseThrow(() -> new InvalidDataException("Psicólogo não encontrado com ID: " + psicologoId));
                    sessao.setPsicologo(psicologo);
                    
                    // Nota: Sessao não tem campo 'valor' na entidade atual
                    // Se precisar adicionar, deve ser feito na entidade primeiro
                    
                    // Status (Boolean, não String - true = ativa, false = cancelada)
                    String statusStr = record.get("status");
                    if (statusStr != null && !statusStr.isEmpty()) {
                        sessao.setStatus(statusStr.equalsIgnoreCase("ATIVA") || 
                                        statusStr.equalsIgnoreCase("AGENDADA") || 
                                        statusStr.equalsIgnoreCase("true"));
                    } else {
                        sessao.setStatus(true); // Ativa por padrão
                    }
                    
                    // Observações (opcional)
                    try {
                        String obs = record.get("observacoes");
                        if (obs != null && !obs.isEmpty()) {
                            sessao.setObservacoes(obs);
                        }
                    } catch (IllegalArgumentException e) {
                        // Campo observacoes não existe no CSV, ignora
                    }

                    sessaoRepository.save(sessao);
                    imported.add(sessao);
                    
                } catch (Exception e) {
                    errors.add("Linha " + lineNumber + ": " + e.getMessage());
                    log.error("Erro ao importar sessão na linha {}: {}", lineNumber, e.getMessage());
                }
            }

        } catch (Exception e) {
            log.error("Erro ao processar arquivo CSV de sessões", e);
            throw new RuntimeException("Erro ao processar arquivo CSV: " + e.getMessage());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalProcessed", lineNumber);
        result.put("totalImported", imported.size());
        result.put("totalErrors", errors.size());
        result.put("errors", errors);

        log.info("Importação de sessões concluída: {} importadas, {} erros", imported.size(), errors.size());
        return result;
    }

    private void validateCSVFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidDataException("Arquivo CSV está vazio");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".csv")) {
            throw new InvalidDataException("Arquivo deve ser do tipo CSV");
        }

        // Limite de 10MB
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new InvalidDataException("Arquivo muito grande. Máximo: 10MB");
        }
    }
}

