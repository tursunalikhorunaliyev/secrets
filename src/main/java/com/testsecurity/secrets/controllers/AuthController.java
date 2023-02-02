package com.testsecurity.secrets.controllers;

import com.testsecurity.secrets.dto.TokenModel;
import com.testsecurity.secrets.entities.RoleEntity;
import com.testsecurity.secrets.entities.UserEntity;
import com.testsecurity.secrets.repositories.RoleEntityRepository;
import com.testsecurity.secrets.repositories.UserEntityRepository;
import com.testsecurity.secrets.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private UserEntityRepository userEntityRepository;
    private RoleEntityRepository roleEntityRepository;

    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;


    public AuthController(UserEntityRepository userEntityRepository, RoleEntityRepository roleEntityRepository, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userEntityRepository = userEntityRepository;
        this.roleEntityRepository = roleEntityRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("register")
    public ResponseEntity<String> register(@RequestParam("username") String username, @RequestParam("password") String password){

        if(userEntityRepository.existsByUsername(username)){
            return new ResponseEntity<>("Username alredy taken", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = new UserEntity();


        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        RoleEntity role = roleEntityRepository.findByName("USER").get();
        user.setRoles(Collections.singleton(role));

        userEntityRepository.save(user);

        return new ResponseEntity<>("Yeahooooooo successfully registered!", HttpStatus.OK);


    }

    @PostMapping("login")
    public ResponseEntity<TokenModel> login(@RequestParam("username") String username, @RequestParam("password") String password){

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        String token = jwtService.generateToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>(new TokenModel(token), HttpStatus.OK);
    }


}
