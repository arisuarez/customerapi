package com.test.customerapi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SingleWebPageController {

    @GetMapping("/")
    public String index() {
        return "base";
    }
}