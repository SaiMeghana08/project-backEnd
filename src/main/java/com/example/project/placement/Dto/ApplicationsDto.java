package com.example.project.placement.Dto;

import lombok.Data;

@Data
public class ApplicationsDto {
    private int id;
    private int student_id;
    private int job_id;
    private String status;
    private String applied_at; //
}
