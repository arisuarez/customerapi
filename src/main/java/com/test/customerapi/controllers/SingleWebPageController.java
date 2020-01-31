package com.test.customerapi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SingleWebPageController {

    /**
     * We need only one template as a single web page using vuejs
     * @return
     */
    @GetMapping("/")
    public String index() {
        return "base";
    }

}