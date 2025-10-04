package com.gestaopsi.prd.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Login da clínica é obrigatório")
    private String clinicaLogin;

    @NotBlank(message = "Login do psicólogo é obrigatório")
    private String psicologLogin;

    @NotBlank(message = "Senha é obrigatória")
    private String password;

    public String getClinicaLogin() {
        return clinicaLogin;
    }

    public void setClinicaLogin(String clinicaLogin) {
        this.clinicaLogin = clinicaLogin;
    }

    public String getPsicologLogin() {
        return psicologLogin;
    }

    public void setPsicologLogin(String psicologLogin) {
        this.psicologLogin = psicologLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


