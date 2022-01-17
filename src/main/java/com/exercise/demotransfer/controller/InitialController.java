package com.exercise.demotransfer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitialController {

    @RequestMapping("/")
    public String getHelloWorld(){
        return "Hello World";
    }
}
