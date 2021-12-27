package org.mylearning.jwtexample;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // This is invoked when user tries to access a secured REST resource without supplying any credentials
        // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to
        // Here you can place any message you want

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String message;
        /*if ( authException.getCause() !=null ){
            message = authException.getCause().toString() + " " + authException.getMessage();
        }else
            message = authException.getMessage();*/
        log.info("Unauthorized Access");
        message = (authException.getCause() !=null )?
                authException.getCause().toString() + " " + authException.getMessage() : authException.getMessage();

        byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", message));
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        response.getOutputStream().write(body);
    }
}
