package com.example.store.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Store Service";
    }
}
