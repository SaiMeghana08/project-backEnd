package com.example.project.placement.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "companies")
public class Company {

    @Id
    private int id;

    private String name;
    private String contactEmail;

    @ManyToOne
    @JoinColumn(name = "added_by_admin_id")
    private Admin addedBy;
}

