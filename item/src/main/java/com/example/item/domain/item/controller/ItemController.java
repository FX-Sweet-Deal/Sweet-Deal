package com.example.item.domain.item.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello from item Service";
    }
}