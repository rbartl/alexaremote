package com.edvbartl.alexafb.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AliveController {

    @RequestMapping("/alive")
    public String alive() {
        return "OK";
    }

    @RequestMapping("/secured")
    public String secured() {
        return "OK";
    }


}