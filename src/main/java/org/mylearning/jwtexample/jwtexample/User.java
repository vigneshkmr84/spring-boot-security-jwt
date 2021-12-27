package org.mylearning.jwtexample.jwtexample;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class User implements Serializable {

    private String name;
    private String password;

    public User(String name){
        this.name = name;
    }
}
