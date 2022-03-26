package com.bvdv.bvdvapi.representation;

import com.bvdv.bvdvapi.enums.Role;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Date exp;
    private String notification;
    private Role role;

    public JwtResponse(String accessToken, Date exp, String notification, Role role) {
        this.token = accessToken;
        this.exp = exp;
        this.notification = notification;
        this.role = role;
    }
}