package com.gestaopsi.prd.dto;

public class LoginResponse {
    private String token;
    private Long userId;
    private Long clinicaId;
    private Long psicologId;
    private String clinicaLogin;
    private String psicologLogin;
    private String tipoUser;
    private String clinicaNome;
    private String psicologNome;
    private String tituloSite;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getClinicaId() { return clinicaId; }
    public void setClinicaId(Long clinicaId) { this.clinicaId = clinicaId; }
    public Long getPsicologId() { return psicologId; }
    public void setPsicologId(Long psicologId) { this.psicologId = psicologId; }
    public String getClinicaLogin() { return clinicaLogin; }
    public void setClinicaLogin(String clinicaLogin) { this.clinicaLogin = clinicaLogin; }
    public String getPsicologLogin() { return psicologLogin; }
    public void setPsicologLogin(String psicologLogin) { this.psicologLogin = psicologLogin; }
    public String getTipoUser() { return tipoUser; }
    public void setTipoUser(String tipoUser) { this.tipoUser = tipoUser; }
    public String getClinicaNome() { return clinicaNome; }
    public void setClinicaNome(String clinicaNome) { this.clinicaNome = clinicaNome; }
    public String getPsicologNome() { return psicologNome; }
    public void setPsicologNome(String psicologNome) { this.psicologNome = psicologNome; }
    public String getTituloSite() { return tituloSite; }
    public void setTituloSite(String tituloSite) { this.tituloSite = tituloSite; }
}


