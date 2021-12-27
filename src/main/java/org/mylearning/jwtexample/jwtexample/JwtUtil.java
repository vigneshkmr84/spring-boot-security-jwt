package org.mylearning.jwtexample.jwtexample;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
@Slf4j
public class JwtUtil implements Serializable {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; // 5 hours
    private static final long serialVersionUID = -2550185165626007488L;
    // JWT signing secret key
    private String secret = "password";

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        log.info("expiration : " + expiration);
        log.info("Is expired : " + expiration.before(new Date()));
        return expiration.before(new Date());
    }

    //generate token for user

    // ORIGINAL METHOD
    /*public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }*/

    // custom method to generate token for user of customs user Object
    public String generateToken(User userDetails, String role) {
        Map<String, Object> claims = new HashMap<>();
        // setting custom claims parameters
        claims.put("role", role);
        claims.put("environment", "dev");
        claims.put("user", userDetails.getName());
        return doGenerateToken(claims, userDetails.getName());
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                //.signWith(SignatureAlgorithm.HS512, secret).compact();
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    //validate token
    // ORIGINAL METHOD - involving Spring UserDetails object
    /*public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }*/

    public Boolean validateToken(String token, User userDetails) {
        final String username = getUsernameFromToken(token);
        log.info("username : " + username);
        return (username.equals(userDetails.getName()) && !isTokenExpired(token));
    }
}