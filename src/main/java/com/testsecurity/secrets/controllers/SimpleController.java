package com.testsecurity.secrets.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/simple")
public class SimpleController {

    @GetMapping("/salom")
    public ResponseEntity<String> salom(){
        return ResponseEntity.ok("Hello Spring Security!!!");
    }

}
