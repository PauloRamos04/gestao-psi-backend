package com.gestaopsi.prd.dto;

public class LoginResponse {
    private String token;
    private Long userId;
    private String username;
    private Long clinicaId;
    private Long psicologId;
    private String tipoUser;
    private String clinicaNome;
    private String psicologoNome;
    private String tituloSite;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Long getClinicaId() { return clinicaId; }
    public void setClinicaId(Long clinicaId) { this.clinicaId = clinicaId; }
    public Long getPsicologId() { return psicologId; }
    public void setPsicologId(Long psicologId) { this.psicologId = psicologId; }
    public String getTipoUser() { return tipoUser; }
    public void setTipoUser(String tipoUser) { this.tipoUser = tipoUser; }
    public String getClinicaNome() { return clinicaNome; }
    public void setClinicaNome(String clinicaNome) { this.clinicaNome = clinicaNome; }
    public String getPsicologoNome() { return psicologoNome; }
    public void setPsicologoNome(String psicologoNome) { this.psicologoNome = psicologoNome; }
    public String getTituloSite() { return tituloSite; }
    public void setTituloSite(String tituloSite) { this.tituloSite = tituloSite; }
}


