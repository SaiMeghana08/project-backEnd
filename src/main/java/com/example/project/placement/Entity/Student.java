package com.example.project.placement.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "students")
public class Student {

    @Id
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;
    private String branch;
    private float cgpa;
    private int batchYear;
}

