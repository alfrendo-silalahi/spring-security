package dev.alfrendosilalahi.spring.security.controller;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/managements")
public class ManagementController {

    @GetMapping
    public String getManagementResources(Principal principal) {
        return "Get MANAGEMENT resources :: " + principal.getName();
    }

    @PostMapping
    public String postManagementResource() {
        return "Post MANAGEMENT resource";
    }

    @PutMapping
    public String putManagementResource() {
        return "Put MANAGEMENT resource";
    }

    @DeleteMapping
    public String deleteManagementResource() {
        return "Delete MANAGEMENT resource";
    }

}
