package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.service.ImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
@Tag(name = "Importação", description = "Endpoints para importação de dados via CSV")
public class ImportController {

    private final ImportService importService;

    @PostMapping("/pacientes")
    @Operation(summary = "Importar pacientes", description = "Importa pacientes de um arquivo CSV")
    public ResponseEntity<Map<String, Object>> importPacientes(
            @RequestParam("file") MultipartFile file,
            @RequestParam("clinicaId") Long clinicaId) {
        
        Map<String, Object> result = importService.importPacientes(file, clinicaId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/sessoes")
    @Operation(summary = "Importar sessões", description = "Importa sessões de um arquivo CSV")
    public ResponseEntity<Map<String, Object>> importSessoes(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = importService.importSessoes(file);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/template/pacientes")
    @Operation(summary = "Download template pacientes", description = "Baixa template CSV para importação de pacientes")
    public ResponseEntity<String> downloadTemplatePacientes() {
        String template = "nome,email,telefone,cpf,dataNascimento\n" +
                         "João Silva,joao@email.com,11999999999,12345678900,01/01/1990\n" +
                         "Maria Santos,maria@email.com,11988888888,98765432100,15/05/1985";
        
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=\"template_pacientes.csv\"")
            .body(template);
    }

    @GetMapping("/template/sessoes")
    @Operation(summary = "Download template sessões", description = "Baixa template CSV para importação de sessões")
    public ResponseEntity<String> downloadTemplateSessoes() {
        String template = "data,horario,pacienteCpf,psicologoId,status,observacoes\n" +
                         "01/01/2024,10:00,12345678900,1,ATIVA,Primeira consulta\n" +
                         "02/01/2024,14:30,98765432100,1,ATIVA,Retorno";
        
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=\"template_sessoes.csv\"")
            .body(template);
    }
}

