package com.gestaopsi.prd.validation;

import com.gestaopsi.prd.util.ValidatorUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CNPJValidator implements ConstraintValidator<CNPJ, String> {

    @Override
    public void initialize(CNPJ constraintAnnotation) {
    }

    @Override
    public boolean isValid(String cnpj, ConstraintValidatorContext context) {
        if (cnpj == null || cnpj.isEmpty()) {
            return true; // Use @NotBlank para tornar obrigatório
        }
        return ValidatorUtil.isValidCNPJ(cnpj);
    }
}


