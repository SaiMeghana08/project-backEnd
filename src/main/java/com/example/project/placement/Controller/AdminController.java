package com.example.project.placement.Controller;

import com.example.project.placement.Entity.Admin;
import com.example.project.placement.Repository.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminRepo adminRepo;


    @GetMapping
    public List<Admin> getAllAdmins() {
        return adminRepo.findAll();
    }


    @GetMapping("/{id}")
    public Admin getAdminById(@PathVariable int id) {
        return adminRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + id));
    }
}
