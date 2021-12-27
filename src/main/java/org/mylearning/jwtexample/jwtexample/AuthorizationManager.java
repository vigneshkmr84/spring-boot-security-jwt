package org.mylearning.jwtexample.jwtexample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthorizationManager {

    @Autowired
    JwtUtil jwtUtil;

    /**
     * Custom function to check if the role with token
     * matches with the provided role
     *
     * @param bearer - Bearer Token
     * @param role   - Provided role
     * @return - true / false
     */
    public boolean isRoleMatching(String bearer, String role) {

        if (bearer == null) return false;
        String token = jwtUtil.getToken(bearer);
        String roleFromToken = jwtUtil.getRoleFromToken(token);

        log.info("Role From Token : " + roleFromToken);
        return roleFromToken.equals(role);
    }

    /*public boolean hasRole(String role){
        log.info("Role - " + role);
        return true;
    }*/
}
