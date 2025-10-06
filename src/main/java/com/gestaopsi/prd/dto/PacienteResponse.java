package com.gestaopsi.prd.dto;

import com.gestaopsi.prd.entity.Paciente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteResponse {
    private Long id;
    private Integer clinicaId;
    private Integer psicologId;
    private String nome;
    private Boolean status;
    private String clinicaNome;
    private String psicologoNome;

    public static PacienteResponse fromEntity(Paciente paciente) {
        return PacienteResponse.builder()
                .id(paciente.getId())
                .clinicaId(paciente.getClinica() != null ? paciente.getClinica().getId().intValue() : null)
                .psicologId(paciente.getPsicologo() != null ? paciente.getPsicologo().getId().intValue() : null)
                .nome(paciente.getNome())
                .status(paciente.getStatus())
                .clinicaNome(paciente.getClinica() != null ? paciente.getClinica().getNome() : null)
                .psicologoNome(paciente.getPsicologo() != null ? paciente.getPsicologo().getNome() : null)
                .build();
    }
}

