package com.example.valuepaljava.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name="application_user")
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="password")
    private String password;
    @Column(name="username")
    private String username;
    @Column(name="is_account_expired")
    private boolean isAccountNonExpired = true;
    @Column(name="is_account_non_locked")
    private boolean isAccountNonLocked = true;
    @Column(name="is_Credentials_non_expired")
    private boolean isCredentialsNonExpired = true;
    @Column(name="is_enabled")
    private boolean isEnabled = true;

    public ApplicationUser(String password, String username, boolean isAccountNonExpired, boolean isAccountNonLocked, boolean isCredentialsNonExpired, boolean isEnabled) {

        this.password = password;
        this.username = username;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
    }

    public ApplicationUser() {
    }

//    @Override
//    public Collection<GrantedAuthority> getAuthorities() {
//        return grantedAuthorities;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setId(Integer id) {
        this.id = id;
    }

//    public void setGrantedAuthorities(Set<GrantedAuthority> grantedAuthorities) {
//        this.grantedAuthorities = grantedAuthorities;
//    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
