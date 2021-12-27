package org.mylearning.jwtexample.jwtexample;

import org.springframework.stereotype.Service;

@Service
public class Authorization {

    public boolean authorise(User user){
        // Write your custom code to connect to database / LDAP or any other instance to validate the user

        return user.getName().equals(user.getPassword());
    }
}
