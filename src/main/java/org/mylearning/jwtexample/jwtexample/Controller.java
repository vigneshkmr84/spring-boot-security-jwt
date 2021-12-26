package org.mylearning.jwtexample.jwtexample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class Controller {

    @GetMapping("/health")
    public ResponseEntity<String> health(){
        log.info("Health-OK");
        return ResponseEntity.ok().body("Ok");
    }


    @GetMapping("/protected/admin")
    public ResponseEntity<String> adminAPI(){
        log.info("This is a Admin-Protected API");
        return ResponseEntity.ok().body("Hello Admin - Welcome!!!");
    }

    @GetMapping("/protected/maker")
    public ResponseEntity<String> makerAPI(){
        log.info("This is a Maker-Protected API");
        return ResponseEntity.ok().body("Hello Maker - Welcome!!!");
    }

    @GetMapping("/protected/checker")
    public ResponseEntity<String> checkerAPI(){
        log.info("This is a Checker-Protected API");
        return ResponseEntity.ok().body("Hello Checker - Welcome!!!");
    }



}
