package com.gestaopsi.prd.validation;

import com.gestaopsi.prd.util.ValidatorUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CPFValidator implements ConstraintValidator<CPF, String> {

    @Override
    public void initialize(CPF constraintAnnotation) {
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.isEmpty()) {
            return true; // Use @NotBlank para tornar obrigatório
        }
        return ValidatorUtil.isValidCPF(cpf);
    }
}

