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
                .build();
    }
}
