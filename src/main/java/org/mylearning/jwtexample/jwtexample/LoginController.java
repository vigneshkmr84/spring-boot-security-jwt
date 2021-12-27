package org.mylearning.jwtexample.jwtexample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        log.info("Login - user details : " + user.getName());


        boolean isAuthenticated = authenticationManager.authenticate(user);

        if ( isAuthenticated ){
            //jwtTokenUtil.generateToken(userDetails);
            log.info("User successfully authenticated");
            String token = jwtUtil.generateToken(user, "maker");
            log.info("Token - " + token);
            return ResponseEntity.accepted().body(token);
        }
        else{
            log.error("Invalid User Credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid User Credentials");
        }
    }
}
