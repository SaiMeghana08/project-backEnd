package com.example.project.placement.Dto;


import lombok.Data;

@Data
public class JobDto {
    private int id;
    private int company_id;
    private String title;
    private String description;
    private String location;
    private String salary;
    private String eligibility_criteria;
}
