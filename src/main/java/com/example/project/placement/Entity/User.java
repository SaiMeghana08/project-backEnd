package com.example.project.placement.Entity;



import jakarta.persistence.*;
        import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    private int id;
    private String username;
    private String password;
    private String role;
}
