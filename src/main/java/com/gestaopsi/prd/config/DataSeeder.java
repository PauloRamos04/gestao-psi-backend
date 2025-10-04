package com.gestaopsi.prd.config;

import com.gestaopsi.prd.entity.Clinica;
import com.gestaopsi.prd.entity.Psicologo;
import com.gestaopsi.prd.entity.Usuario;
import com.gestaopsi.prd.repository.ClinicaRepository;
import com.gestaopsi.prd.repository.PsicologoRepository;
import com.gestaopsi.prd.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final ClinicaRepository clinicaRepository;
    private final PsicologoRepository psicologoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(
            ClinicaRepository clinicaRepository,
            PsicologoRepository psicologoRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.clinicaRepository = clinicaRepository;
        this.psicologoRepository = psicologoRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("🌱 Iniciando seeder de dados...");

        // Verificar se já existe a clínica admin
        if (clinicaRepository.findByClinicaLoginAndStatusTrue("admin").isPresent()) {
            logger.info("✅ Dados de teste já existem. Pulando seeder.");
            return;
        }

        logger.info("📝 Criando dados iniciais...");

        // 1. Criar Clínica Admin
        Clinica clinica = new Clinica();
        clinica.setClinicaLogin("admin");
        clinica.setNome("Clínica Gestão PSI - Admin");
        clinica.setStatus(true);
        clinica.setTitulo("Clínica Principal");
        clinica = clinicaRepository.save(clinica);
        logger.info("✅ Clínica criada: {} (ID: {})", clinica.getNome(), clinica.getId());

        // 2. Criar Psicólogo Admin
        Psicologo psicologo = new Psicologo();
        psicologo.setPsicologLogin("admin");
        psicologo.setNome("Administrador do Sistema");
        psicologo.setDtAtivacao(LocalDate.now());
        psicologo.setCategoriaId(1); // Categoria padrão
        psicologo = psicologoRepository.save(psicologo);
        logger.info("✅ Psicólogo criado: {} (ID: {})", psicologo.getNome(), psicologo.getId());

        // 3. Criar Usuário Admin (conecta clínica + psicólogo)
        Usuario usuario = new Usuario();
        usuario.setClinicaId(clinica.getId().intValue());
        usuario.setPsicologId(psicologo.getId().intValue());
        usuario.setTipoId(1); // Tipo Admin
        usuario.setSenha(passwordEncoder.encode("admin123"));
        usuario.setStatus(true);
        usuario.setTitulo("Sistema Gestão PSI");
        usuario = usuarioRepository.save(usuario);
        logger.info("✅ Usuário criado: ID {}", usuario.getId());

        logger.info("");
        logger.info("=================================================");
        logger.info("🎉 DADOS INICIAIS CRIADOS COM SUCESSO!");
        logger.info("=================================================");
        logger.info("📋 Credenciais de acesso:");
        logger.info("   Login da Clínica: admin");
        logger.info("   Login do Psicólogo: admin");
        logger.info("   Senha: admin123");
        logger.info("=================================================");
        logger.info("");
    }
}


