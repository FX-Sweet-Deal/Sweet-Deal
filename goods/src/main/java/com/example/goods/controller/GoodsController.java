package com.example.goods.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodsController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Goods Service!";
    }
}