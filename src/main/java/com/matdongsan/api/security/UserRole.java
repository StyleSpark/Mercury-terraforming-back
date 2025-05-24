package com.matdongsan.api.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class UserRole implements UserDetails {
  private final Long id;
  private final String email;
  private final String role;

  public UserRole(Long id, String email, String role) {
    this.id = id;
    this.email = email;
    this.role = role;
  }

  @Override public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(() -> "ROLE_" + role.toUpperCase());
  }

  @Override public String getUsername() { return email; }
  @Override public String getPassword() { return null; }
  @Override public boolean isAccountNonExpired() { return true; }
  @Override public boolean isAccountNonLocked() { return true; }
  @Override public boolean isCredentialsNonExpired() { return true; }
  @Override public boolean isEnabled() { return true; }
}

