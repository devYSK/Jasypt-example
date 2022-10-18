package com.ys.jaspytexample.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JasyptTestController {

    @Value("${example.encrypt-value}")
    private String value;

    @GetMapping
    public String getTest() {
        return value;
    }

}
