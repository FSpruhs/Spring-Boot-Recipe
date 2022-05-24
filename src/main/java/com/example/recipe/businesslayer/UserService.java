package com.example.recipe.businesslayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.recipe.persistence.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private final UserRepository repository;

    @Autowired
    PasswordEncoder encoder;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username).map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException(username + "not found"));
    }

    public ResponseEntity save(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } else {
            user.setPassword(encoder.encode(user.getPassword()));
            repository.save(user);
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.OK);
        }
    }
}
