package com.bvdv.bvdvapi.service;

import com.bvdv.bvdvapi.config.jwt.JwtUtils;
import com.bvdv.bvdvapi.models.User;
import com.bvdv.bvdvapi.models.UserDetail;
import com.bvdv.bvdvapi.repository.UserRepository;
import com.bvdv.bvdvapi.representation.JwtResponse;
import com.bvdv.bvdvapi.representation.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Value("${security.jwt.expiration}")
    private int jwtExpirationMs;

    public JwtResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Email or password incorrect"));

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), loginRequest.getPassword()));
            Date date = new Date();
            long iat = date.getTime();
            Date exp = new Date(iat + jwtExpirationMs);

            String notification = "Login successfully!!";

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            return new JwtResponse(
                    jwt,
                    exp,
                    notification,
                    userDetail.getRole()
            );
        } catch (BadCredentialsException e) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Email or password incorrect");
        }

    }

    public boolean create(User user) {
        if (userRepository.findFirstByUsernameOrEmail(user.getUsername(), user.getEmail()).isPresent())
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Email or username already exists");
        user.setPassword(new BCryptPasswordEncoder(4).encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }
}
