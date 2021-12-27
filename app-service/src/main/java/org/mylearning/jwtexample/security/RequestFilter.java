package org.mylearning.jwtexample.security;

import lombok.extern.slf4j.Slf4j;
import org.mylearning.jwtexample.JwtUtil;
import org.mylearning.jwtexample.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class RequestFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    CustomUseDetailsService customUseDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String headerString = request.getHeader("Authorization");
        String token = "";


        if (headerString != null && !headerString.isEmpty() && headerString.startsWith("Bearer")) {
            //log.info("Complete Authorization Header String : " + headerString);
            token = headerString.substring(7); // Bearer and space = 7 characters
            //log.info("Token : " + token);
            String userName = jwtUtil.getUserNameFromToken(token);

            if (userName != null) {

                if (jwtUtil.validateToken(token, new User(userName))) {

                    UserDetails userDetails = this.customUseDetailsService.loadUserByUsername(userName);
                    log.info("JWT token is valid");
                    // continue the normal flow of application after token is valid
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } else {
                    log.error("Invalid JWT");
                    try {
                        throw new Exception("Invalid Token");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        filterChain.doFilter(request, response);
    }
}
