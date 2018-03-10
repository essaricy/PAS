package com.softvision.ipm.pms.web.config;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    public CustomPermissionEvaluator() {
    }

    public boolean hasPermission(Authentication arg0, Object arg1) {
        if (arg0 == null || !arg0.isAuthenticated()) {
            return false;
        }
        else {
            for(GrantedAuthority authority: arg0.getAuthorities()) {
                if(authority.getAuthority().equals(arg1))
                    return true;
            }
            return false;
        }
    }

    @Override
    public boolean hasPermission(Authentication arg0, Object arg1, Object arg2) {
        if (arg0 == null || !arg0.isAuthenticated()) {
            return false;
        }
        else {
            for(GrantedAuthority authority: arg0.getAuthorities()) {
                if(authority.getAuthority().equals(arg2))
                    return true;
            }
            return false;
        }
    }

    @Override
    public boolean hasPermission(Authentication arg0, Serializable arg1, String arg2, Object arg3) {
        throw new RuntimeException("Id-based permission evaluation not currently supported.");
    }

}