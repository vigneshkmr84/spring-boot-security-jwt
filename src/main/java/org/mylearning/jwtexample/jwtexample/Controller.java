package org.mylearning.jwtexample.jwtexample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class Controller {

    @Autowired
    AuthorizationManager authorizationManager;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        log.info("Health-OK");
        return ResponseEntity.ok().body("Ok");
    }

    //@PreAuthorize("@authorizationManager.hasRole('admin')")
    @PreAuthorize("@authorizationManager.isRoleMatching(#token, 'admin')")
    @GetMapping("/protected/admin")
    public ResponseEntity<String> adminAPI(@RequestHeader(name = "Authorization") String token) {
        log.info("This is a Admin-Protected API");
        //log.info(String.valueOf(authorizationManager.isRoleMatching(token, "admin")));
        return ResponseEntity.ok().body("Hello Admin - Welcome!!!");
    }

    @PreAuthorize("@authorizationManager.isRoleMatching(#token, 'maker')")
    @GetMapping("/protected/maker")
    public ResponseEntity<String> makerAPI(@RequestHeader(name = "Authorization") String token) {
        log.info("This is a Maker-Protected API");
        return ResponseEntity.ok().body("Hello Maker - Welcome!!!");
    }

    @PreAuthorize("@authorizationManager.isRoleMatching(#token, 'checker')")
    @GetMapping("/protected/checker")
    public ResponseEntity<String> checkerAPI(@RequestHeader(name = "Authorization") String token) {
        log.info("This is a Checker-Protected API");
        return ResponseEntity.ok().body("Hello Checker - Welcome!!!");
    }


}
