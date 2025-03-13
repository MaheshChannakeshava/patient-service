package com.mahi.patientservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientResponseDTO {
    private String id;
    private String name;
    private String address;
    private String email;
    private String dateOfBirth;
}
