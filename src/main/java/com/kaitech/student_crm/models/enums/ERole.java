package com.kaitech.student_crm.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum ERole implements GrantedAuthority {
    ROLE_ADMIN,
    ROlE_STUDENT;

    @Override
    public String getAuthority() {
        return name();
    }
}
