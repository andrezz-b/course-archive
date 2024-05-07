package com.andrezzb.coursearchive.college.dto;

import lombok.Data;

@Data
public class CollegeDto {
    private Long id;
    private String name;
    private String acronym;
    private String city;
    private Integer postcode;
    private String address;
    private String website;
    private String description;
}
