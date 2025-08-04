package com.example.project.placement.Dto;


import lombok.Data;

@Data
public class CompanyDto {
    private int id;
    private String name;
    private String contact_email;
    private int added_by_admin_id;
}
