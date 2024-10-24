package dev.alfrendosilalahi.spring.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public String getAdminResources() {
        return "Get ADMIN resources";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")

    public String postAdminResource() {
        return "Post ADMIN resource";
    }

    @PutMapping
    @PreAuthorize("hasAuthority('admin:update')")
    public String putAdminResource() {
        return "Put ADMIN resource";
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('admin:delete')")
    public String deleteAdminResource() {
        return "Delete ADMIN resource";
    }

}
