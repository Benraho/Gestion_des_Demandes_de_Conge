package com.example.MiniProject.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conges/test/employe")
class EmployeTestController {
    @GetMapping("/{id}")
    public ResponseEntity<String> getEmploye(@PathVariable Long id) {
        return ResponseEntity.ok("Employé OK");
    }
}

@RestController
@RequestMapping("/api/conges")
class ManagerTestController {
    @PostMapping("/approuver/{id}")
    public ResponseEntity<String> approuver(@PathVariable Long id) {
        return ResponseEntity.ok("Approuvé OK");
    }

    @PostMapping("/refuser/{id}")
    public ResponseEntity<String> refuser(@PathVariable Long id) {
        return ResponseEntity.ok("Refusé OK");
    }
}

@RestController
@RequestMapping("/api/admin")
class AdminTestController {
    @GetMapping
    public ResponseEntity<String> getAdminPage() {
        return ResponseEntity.ok("Admin OK");
    }
}

@RestController
@RequestMapping("/api/auth")
class AuthTestController {
    @GetMapping("/login")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("Login OK");
    }
}