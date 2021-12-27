package org.mylearning.jwtexample.jwtexample;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationManager {

    public boolean authenticate(User user){
        // Write your custom code to connect to SSO / Database / Other standard Credentials' manager to validate the user name and password

        // For this project, I have used the name and password shd be the same.
        return user.getName().equals(user.getPassword());
    }
}
