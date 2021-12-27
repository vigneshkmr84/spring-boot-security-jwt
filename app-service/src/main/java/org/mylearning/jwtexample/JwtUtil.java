package org.mylearning.jwtexample;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
@Slf4j
public class JwtUtil implements Serializable {

    public static final long TOKEN_DURATION_VALIDITY = 3 * 60 * 60; // 5 hours

    // JWT signing secret key
    private static final String SECRET = "password";

    //private static final PrivateKey privateKey = "";
    @Autowired
    ApplicationContext applicationContext;

    // get environment from properties file
    @Value("${application.environment}")
    private String environment;

    /**
     * Get UserName from Token
     *
     * @param token - KWT
     * @return Username
     */
    public String getUserNameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Get Expiry Date from Token
     *
     * @param token - JWT
     * @return - Expiry Date
     */
    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Get "role" parameter from Claims of the Token
     *
     * @param token - JWT
     * @return role
     */
    public String getRoleFromToken(String token) {
        return getAllClaimsFromToken(token).get("role", String.class);
    }

    /**
     * Get The Claims mapping from the token
     *
     * @param token - JWT
     * @return - Claims mapping
     */
    private Claims getAllClaimsFromToken(String token) {
        try {
            // use this for Static Secret
            //return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
            return Jwts.parser().setSigningKey((PublicKey) applicationContext.getBean("readPublicKey")).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Check if the token is expired
     *
     * @param token - JWT
     * @return true / false
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        log.info("Expiry Date : " + expiration);
        return expiration.before(new Date());
    }

    // custom method to generate token for user of customs user Object
    public String generateToken(User userDetails, String role) {
        Map<String, Object> claims = new HashMap<>();
        // setting custom claims parameters
        claims.put("role", role);
        claims.put("environment", environment);
        claims.put("user", userDetails.getName());
        return doGenerateToken(claims, userDetails.getName());
    }


    private String doGenerateToken(Map<String, Object> claims, String subject) {

        try {

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(subject)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + TOKEN_DURATION_VALIDITY * 1000))
                    // use this for Static Secret
                    //.signWith(SignatureAlgorithm.HS512, SECRET)
                    .signWith(SignatureAlgorithm.RS256, (RSAPrivateKey) applicationContext.getBean("readPrivateKey"))
                    .compact();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //validate token
    public boolean validateToken(String token, User userDetails) {
        final String username = getUserNameFromToken(token);
        log.info("username : " + username);
        return (username.equals(userDetails.getName()) && !isTokenExpired(token));
    }

    public String getToken(String headers) {
        return headers.substring(7);
    }
}
