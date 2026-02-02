package com.ghostapidetector.Ghost.API.detector.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testcontroller {

    @GetMapping("/api/test")
    public String testController(){

        return "Ghost API is alive.";
    }

    @GetMapping("/api/users/free")
    public String getFreeUsers(){
        return "these are the free users";
    }

    @PostMapping("/age")
    public String myAge(){
        return "your age is 23";
    }

}
