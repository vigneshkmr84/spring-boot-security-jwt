package org.mylearning.jwtexample;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
public class User implements Serializable {

    private String name;
    private String password;

    public User(String name) {
        this.name = name;
    }
}
