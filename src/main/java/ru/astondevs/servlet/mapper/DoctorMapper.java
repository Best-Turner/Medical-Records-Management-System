package ru.astondevs.servlet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.astondevs.model.Doctor;
import ru.astondevs.servlet.dto.incomingDto.IncomingDoctorDto;
import ru.astondevs.servlet.dto.outGoingDto.OutDoctorDto;

@Mapper
public interface DoctorMapper {

    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "speciality", target = "speciality")
    Doctor map(IncomingDoctorDto incomingDoctordto);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "speciality", source = "speciality")
    OutDoctorDto map(Doctor doctor);

}
