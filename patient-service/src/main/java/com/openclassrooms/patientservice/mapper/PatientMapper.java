package com.openclassrooms.patientservice.mapper;


import com.openclassrooms.patientservice.dto.PatientDto;
import com.openclassrooms.patientservice.entity.PatientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface PatientMapper {


    PatientEntity mapToPatientEntity(PatientDto patientDto);

    @Mapping(target = "id", source = "id")
    PatientDto mapToPatientDto(PatientEntity patientEntity);







}
