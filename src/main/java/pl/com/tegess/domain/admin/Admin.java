package pl.com.tegess.domain.admin;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Admin implements UserDetails {

    @Id
    private String username;
    private String password;
    private boolean accountNotExpired;
    private boolean accountNotLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;

    public Admin() {
    }

    public Admin(String username, String password) {
        this(username, password, new ArrayList<>());
    }

    public Admin(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.accountNotExpired = true;
        this.accountNotLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
        this.authorities = authorities;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNotExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNotLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
