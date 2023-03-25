package antifraud.dto;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ADMINISTRATOR("ROLE_ADMINISTRATOR"),
    SUPPORT("ROLE_SUPPORT"),
    MERCHANT("ROLE_MERCHANT");

    private String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
