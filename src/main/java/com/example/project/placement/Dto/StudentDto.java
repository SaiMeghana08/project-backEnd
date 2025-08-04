package com.example.project.placement.Dto;

import lombok.Data;

@Data
public class StudentDto {
    private int id;
    private int user_id;
    private String name;
    private String branch;
    private float cgpa;
    private int batch_year;

}
