package com.example.project.placement.Controller;


import com.example.project.placement.Entity.Student;
import com.example.project.placement.Repository.StudentRepo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentRepo studentRepo;


    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }


    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable int id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));
    }

}
