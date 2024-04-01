package ru.astondevs.servlet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.astondevs.model.Patient;
import ru.astondevs.servlet.dto.incomingDto.IncomingPatientDto;
import ru.astondevs.servlet.dto.outGoingDto.OutPatientDto;

@Mapper
public interface PatientMapper {

    PatientMapper INSTANCE = Mappers.getMapper(PatientMapper.class);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "age", target = "age")
    @Mapping(source = "number", target = "policyNumber")
    Patient map(IncomingPatientDto incomingPatientdto);


    @Mapping(source = "name", target = "name")
    @Mapping(source = "age", target = "age")
    @Mapping(source = "policyNumber", target = "number")
    OutPatientDto map(Patient patient);
}
