package com.gestaopsi.prd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPreferencesRequest {
    private String temaPreferido;
    private String idioma;
    private String timezone;
    private Boolean receberNotificacoesEmail;
    private Boolean receberNotificacoesSistema;
    private Boolean lembretesSessao;
    private Boolean notificacoesPagamento;
}




