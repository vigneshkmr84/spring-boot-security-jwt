package org.mylearning.jwtexample.jwtexample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AuthenticationManager {

    private static final Map<String, String> userCredentials = new HashMap<String, String>() {{
        put("admin", "admin");
        put("maker", "maker");
        put("checker", "checker");
    }};

    public boolean authenticate(User user) {
        // Write your custom code to connect to SSO / Database / Other standard Credentials' manager to validate the user name and password

        // For this project, I've just used a simple map to see if the username and credentials are matching
        String password = userCredentials.get(user.getName());

        return password != null && password.equals(user.getPassword());
    }
}
