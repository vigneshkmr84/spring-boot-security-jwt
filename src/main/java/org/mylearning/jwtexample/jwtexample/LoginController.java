package org.mylearning.jwtexample.jwtexample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        log.info("Login - user details : " + user.getName());


        //jwtTokenUtil.generateToken(userDetails);
        String token = jwtUtil.generateToken(user);
        log.info("Token - " + token);
        return ResponseEntity.accepted().body(token);
    }
}
