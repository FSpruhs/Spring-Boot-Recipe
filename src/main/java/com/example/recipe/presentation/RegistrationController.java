package com.example.recipe.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.recipe.businesslayer.User;
import com.example.recipe.businesslayer.UserService;

import javax.validation.Valid;
@RequestMapping("/api/register")
@RestController
public class RegistrationController {


    private final UserService service;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    public RegistrationController(UserService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity register(@Valid @RequestBody User user) {
        return service.save(user);
    }
}
