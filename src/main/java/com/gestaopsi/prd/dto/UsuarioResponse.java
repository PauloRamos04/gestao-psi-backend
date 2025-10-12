package com.gestaopsi.prd.dto;

import com.gestaopsi.prd.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String username;
    private Integer clinicaId;
    private Integer psicologId;
    private Integer tipoId;
    private Boolean status;
    private String titulo;
    private String clinicaNome;
    private String psicologoNome;
    private String tipoNome;
    
    // Informações pessoais
    private String nomeCompleto;
    private String email;
    private String telefone;
    private String celular;
    private String cargo;
    private String departamento;
    private String fotoUrl;
    private String observacoes;
    
    // Preferências
    private String temaPreferido;
    private String idioma;
    private String timezone;
    private Boolean receberNotificacoesEmail;
    private Boolean receberNotificacoesSistema;
    private Boolean lembretesSessao;
    private Boolean notificacoesPagamento;
    
    // Controle
    private String ultimoAccesso;
    private String dataCriacao;

    public static UsuarioResponse fromEntity(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .clinicaId(usuario.getClinicaId())
                .psicologId(usuario.getPsicologId())
                .tipoId(usuario.getTipoId())
                .status(usuario.getStatus())
                .titulo(usuario.getTitulo())
                .clinicaNome(usuario.getClinica() != null ? usuario.getClinica().getNome() : null)
                .psicologoNome(usuario.getPsicologo() != null ? usuario.getPsicologo().getNome() : null)
                .tipoNome(usuario.getTipo() != null ? usuario.getTipo().getNome() : null)
                // Informações pessoais
                .nomeCompleto(usuario.getNomeCompleto())
                .email(usuario.getEmail())
                .telefone(usuario.getTelefone())
                .celular(usuario.getCelular())
                .cargo(usuario.getCargo())
                .departamento(usuario.getDepartamento())
                .fotoUrl(usuario.getFotoUrl())
                .observacoes(usuario.getObservacoes())
                // Preferências
                .temaPreferido(usuario.getTemaPreferido())
                .idioma(usuario.getIdioma())
                .timezone(usuario.getTimezone())
                .receberNotificacoesEmail(usuario.getReceberNotificacoesEmail())
                .receberNotificacoesSistema(usuario.getReceberNotificacoesSistema())
                .lembretesSessao(usuario.getLembretesSessao())
                .notificacoesPagamento(usuario.getNotificacoesPagamento())
                // Controle
                .ultimoAccesso(usuario.getUltimoAccesso() != null ? usuario.getUltimoAccesso().toString() : null)
                .dataCriacao(usuario.getDataCriacao() != null ? usuario.getDataCriacao().toString() : null)
                .build();
    }
}
