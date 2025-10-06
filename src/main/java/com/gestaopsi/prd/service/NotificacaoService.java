package com.gestaopsi.prd.service;

import com.gestaopsi.prd.entity.Notificacao;
import com.gestaopsi.prd.entity.Usuario;
import com.gestaopsi.prd.repository.NotificacaoRepository;
import com.gestaopsi.prd.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final UsuarioRepository usuarioRepository;

    public List<Notificacao> listarNaoLidas(Long usuarioId) {
        return notificacaoRepository.findByUsuarioIdAndLidaFalseOrderByDataCriacaoDesc(usuarioId);
    }

    public List<Notificacao> listarTodas(Long usuarioId) {
        return notificacaoRepository.findByUsuarioIdOrderByDataCriacaoDesc(usuarioId);
    }

    public Long contarNaoLidas(Long usuarioId) {
        return notificacaoRepository.countByUsuarioIdAndLidaFalse(usuarioId);
    }

    @Transactional
    public Notificacao criar(Long usuarioId, String titulo, String mensagem, String tipo, String link) {
        log.info("Criando notificação para usuário: {}", usuarioId);
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Notificacao notificacao = Notificacao.builder()
            .usuario(usuario)
            .titulo(titulo)
            .mensagem(mensagem)
            .tipo(tipo)
            .link(link)
            .dataCriacao(LocalDateTime.now())
            .lida(false)
            .enviada(false)
            .build();

        return notificacaoRepository.save(notificacao);
    }

    @Transactional
    public boolean marcarComoLida(Long id) {
        return notificacaoRepository.findById(id)
            .map(notificacao -> {
                notificacao.setLida(true);
                notificacao.setDataLeitura(LocalDateTime.now());
                notificacaoRepository.save(notificacao);
                return true;
            })
            .orElse(false);
    }

    @Transactional
    public void marcarTodasComoLidas(Long usuarioId) {
        List<Notificacao> notificacoes = notificacaoRepository
            .findByUsuarioIdAndLidaFalseOrderByDataCriacaoDesc(usuarioId);
        
        notificacoes.forEach(n -> {
            n.setLida(true);
            n.setDataLeitura(LocalDateTime.now());
        });
        
        notificacaoRepository.saveAll(notificacoes);
    }

    /**
     * Criar notificação de lembrete de sessão
     */
    public void criarNotificacaoSessao(Long usuarioId, String pacienteNome, 
                                       String data, String hora) {
        String titulo = "Lembrete de Sessão";
        String mensagem = String.format("Sessão com %s agendada para %s às %s",
            pacienteNome, data, hora);
        
        criar(usuarioId, titulo, mensagem, "SESSAO", "/agenda");
    }

    /**
     * Criar notificação de pagamento pendente
     */
    public void criarNotificacaoPagamento(Long usuarioId, String pacienteNome, double valor) {
        String titulo = "Pagamento Pendente";
        String mensagem = String.format("Pagamento de R$ %.2f do paciente %s está pendente",
            valor, pacienteNome);
        
        criar(usuarioId, titulo, mensagem, "PAGAMENTO", "/pagamentos");
    }
}

