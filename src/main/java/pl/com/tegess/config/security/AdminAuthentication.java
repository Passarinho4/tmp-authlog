package pl.com.tegess.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import pl.com.tegess.domain.admin.Admin;

import java.util.Collection;

public class AdminAuthentication implements Authentication {

    private final Admin admin;
    private boolean authenticated;


    public AdminAuthentication(Admin admin) {
        this.admin = admin;
        this.authenticated = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return admin.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return admin.getPassword();
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return admin;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return admin.getUsername();
    }
}
