package dev.alfrendosilalahi.spring.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protected-resources")
public class DemoController {


    @GetMapping
    public String getProtectedResources() {
        return "Protected resources";
    }

}
