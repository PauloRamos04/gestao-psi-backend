package com.gestaopsi.prd.security;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class CustomMethodSecurityExpressionRoot 
    extends SecurityExpressionRoot 
    implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    /**
     * Verifica se o usuário tem uma permissão específica
     */
    public boolean hasPermission(String permission) {
        return getAuthentication().getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(auth -> auth.equals(permission));
    }

    /**
     * Verifica se o usuário tem qualquer uma das permissões
     */
    public boolean hasAnyPermission(String... permissions) {
        for (String permission : permissions) {
            if (hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se o usuário tem todas as permissões
     */
    public boolean hasAllPermissions(String... permissions) {
        for (String permission : permissions) {
            if (!hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica se o usuário é admin
     */
    public boolean isAdmin() {
        return hasRole("ADMIN");
    }

    /**
     * Verifica se o usuário é psicólogo
     */
    public boolean isPsicologo() {
        return hasRole("PSICOLOGO");
    }

    /**
     * Verifica se o usuário é secretária
     */
    public boolean isSecretaria() {
        return hasRole("SECRETARIA");
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }
}


