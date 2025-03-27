package com.jp.apicompositorservice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class User {

    private String username;
    private String fullname;
    private String email;
    private String address;
    private String contact;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }
}
