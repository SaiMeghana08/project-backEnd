package com.example.project.placement.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "jobs")
public class Job {

    @Id
    private int id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private String title;
    private String description;
    private String location;
    private String salary;
    private String eligibilityCriteria;
}

