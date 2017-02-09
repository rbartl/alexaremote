package com.edvbartl.alexafb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RootController {

    @RequestMapping("/")
    public String greeting() {
        return "OK";
    }
}