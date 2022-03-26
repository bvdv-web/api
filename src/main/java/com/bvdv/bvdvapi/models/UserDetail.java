package com.bvdv.bvdvapi.models;

import com.bvdv.bvdvapi.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@With
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDetail implements UserDetails {
    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private String email;

    private Role role;
    private String firstName;
    private String lastName;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetail from(User user) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
        return new UserDetail().withId(user.getId()).withUsername(user.getUsername())
                .withPassword(user.getPassword()).withEmail(user.getEmail()).withRole(user.getRole()).withAuthorities(authorities)
                .withFirstName(user.getFirstName()).withLastName(user.getLastName());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static Optional<UserDetail> getAuthorizedUser() {
        try {
            return Optional.of((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
