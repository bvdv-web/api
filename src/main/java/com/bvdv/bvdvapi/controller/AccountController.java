package com.bvdv.bvdvapi.controller;

import com.bvdv.bvdvapi.models.User;
import com.bvdv.bvdvapi.models.UserDetail;
import com.bvdv.bvdvapi.representation.LoginRequest;
import com.bvdv.bvdvapi.service.AccountService;
import com.bvdv.bvdvapi.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping({"account", "account/"})
public class AccountController {
    @Autowired
    AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(accountService.login(loginRequest));
    }

    @PostMapping({"register"})
    public ResponseEntity<?> create(@Valid @RequestBody User user) {
        return ResponseEntity.ok().body(accountService.create(user));
    }

    @GetMapping({"my-account"})
    public ResponseEntity<?> checkMyAccount() {
        return ResponseEntity.ok().body(UserDetail.getAuthorizedUser());
    }

//    @PostMapping("/logout")
//    public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
//        refreshTokenService.deleteByUserId(logOutRequest.getUserId());
//        return ResponseObject.success();
//    }
}
