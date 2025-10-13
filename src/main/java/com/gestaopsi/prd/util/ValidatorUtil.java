package com.gestaopsi.prd.util;

import org.owasp.encoder.Encode;

import java.util.regex.Pattern;

public class ValidatorUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern TELEFONE_PATTERN = Pattern.compile(
        "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$"
    );

    /**
     * Valida CPF
     */
    public static boolean isValidCPF(String cpf) {
        if (cpf == null) {
            return false;
        }

        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");

        // CPF deve ter 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Valida primeiro dígito verificador
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int firstDigit = 11 - (sum % 11);
        if (firstDigit >= 10) {
            firstDigit = 0;
        }
        if (firstDigit != Character.getNumericValue(cpf.charAt(9))) {
            return false;
        }

        // Valida segundo dígito verificador
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int secondDigit = 11 - (sum % 11);
        if (secondDigit >= 10) {
            secondDigit = 0;
        }
        return secondDigit == Character.getNumericValue(cpf.charAt(10));
    }

    /**
     * Valida CNPJ
     */
    public static boolean isValidCNPJ(String cnpj) {
        if (cnpj == null) {
            return false;
        }

        cnpj = cnpj.replaceAll("[^0-9]", "");

        if (cnpj.length() != 14) {
            return false;
        }

        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        // Valida primeiro dígito
        int[] weight1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += Character.getNumericValue(cnpj.charAt(i)) * weight1[i];
        }
        int digit1 = sum % 11 < 2 ? 0 : 11 - (sum % 11);
        if (digit1 != Character.getNumericValue(cnpj.charAt(12))) {
            return false;
        }

        // Valida segundo dígito
        int[] weight2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        sum = 0;
        for (int i = 0; i < 13; i++) {
            sum += Character.getNumericValue(cnpj.charAt(i)) * weight2[i];
        }
        int digit2 = sum % 11 < 2 ? 0 : 11 - (sum % 11);
        return digit2 == Character.getNumericValue(cnpj.charAt(13));
    }

    /**
     * Valida email
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Valida telefone brasileiro
     */
    public static boolean isValidTelefone(String telefone) {
        if (telefone == null || telefone.isEmpty()) {
            return false;
        }
        return TELEFONE_PATTERN.matcher(telefone).matches();
    }

    /**
     * Sanitiza string para prevenir XSS
     */
    public static String sanitizeHtml(String input) {
        if (input == null) {
            return null;
        }
        return Encode.forHtml(input);
    }

    /**
     * Sanitiza string para uso em JavaScript
     */
    public static String sanitizeJavaScript(String input) {
        if (input == null) {
            return null;
        }
        return Encode.forJavaScript(input);
    }

    /**
     * Remove caracteres especiais de CPF/CNPJ
     */
    public static String cleanDocument(String document) {
        if (document == null) {
            return null;
        }
        return document.replaceAll("[^0-9]", "");
    }

    /**
     * Formata CPF
     */
    public static String formatCPF(String cpf) {
        if (cpf == null) {
            return null;
        }
        cpf = cleanDocument(cpf);
        if (cpf.length() != 11) {
            return cpf;
        }
        return String.format("%s.%s.%s-%s",
            cpf.substring(0, 3),
            cpf.substring(3, 6),
            cpf.substring(6, 9),
            cpf.substring(9, 11)
        );
    }

    /**
     * Formata CNPJ
     */
    public static String formatCNPJ(String cnpj) {
        if (cnpj == null) {
            return null;
        }
        cnpj = cleanDocument(cnpj);
        if (cnpj.length() != 14) {
            return cnpj;
        }
        return String.format("%s.%s.%s/%s-%s",
            cnpj.substring(0, 2),
            cnpj.substring(2, 5),
            cnpj.substring(5, 8),
            cnpj.substring(8, 12),
            cnpj.substring(12, 14)
        );
    }

    /**
     * Formata telefone
     */
    public static String formatTelefone(String telefone) {
        if (telefone == null) {
            return null;
        }
        telefone = telefone.replaceAll("[^0-9]", "");
        if (telefone.length() == 11) {
            return String.format("(%s) %s-%s",
                telefone.substring(0, 2),
                telefone.substring(2, 7),
                telefone.substring(7, 11)
            );
        } else if (telefone.length() == 10) {
            return String.format("(%s) %s-%s",
                telefone.substring(0, 2),
                telefone.substring(2, 6),
                telefone.substring(6, 10)
            );
        }
        return telefone;
    }
}


