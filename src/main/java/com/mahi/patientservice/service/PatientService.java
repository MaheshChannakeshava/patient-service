package com.mahi.patientservice.service;

import com.mahi.patientservice.dto.PatientRequestDTO;
import com.mahi.patientservice.dto.PatientResponseDTO;
import com.mahi.patientservice.exception.EmailAlreadyExistsException;
import com.mahi.patientservice.exception.PatientNotFoundException;
import com.mahi.patientservice.mapper.PatientMapper;
import com.mahi.patientservice.model.Patient;
import com.mahi.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients(){
        List<Patient> patients = patientRepository.findAll();
        List<PatientResponseDTO> patientResponseDTOs = patients.stream()
                .map(PatientMapper::toDTO).toList();

        return patientResponseDTOs;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())){
            throw new EmailAlreadyExistsException("A patient is already registered with this" +
                    "email address"+ patientRequestDTO.getEmail());
        }

        Patient newPatient=patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        return PatientMapper.toDTO(newPatient);
    }

    public PatientResponseDTO updatePatient(UUID id,PatientRequestDTO patientRequestDTO){
        Patient patient = patientRepository.findById(id).orElseThrow(()-> new PatientNotFoundException("" +
                "Patient Not found:" +id));
        if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),id)){
            throw new EmailAlreadyExistsException("A patient is already registered with this" +
                    "email address"+ patientRequestDTO.getEmail());
        }

        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        Patient updatedPatient= patientRepository.save(patient);
        return PatientMapper.toDTO(updatedPatient);


    }

    public void deletePatient(UUID id){
        patientRepository.deleteById(id);
    }
}
