package com.bvdv.bvdvapi.models;

import com.bvdv.bvdvapi.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;
    private String firstName;
    private String lastName;
}
