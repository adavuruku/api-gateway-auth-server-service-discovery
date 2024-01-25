package com.example.apigateway.security.config;

import java.util.Arrays;
import java.util.List;

public class test {
    public static void main(String[] args) {

        List<String> list = Arrays.asList("/api/v1/users/create", "/api/v1/users/authenticate", "/login");
        boolean answer
                = list.stream().noneMatch(num -> num.contains("/api/v1/users/create/create-customer"));

        // Printing and displaying the above boolean value
        System.out.println(answer);
    }
}
