package ru.astondevs.servlet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.astondevs.model.Appointment;
import ru.astondevs.servlet.dto.incomingDto.IncomingAppointmentDto;
import ru.astondevs.servlet.dto.outGoingDto.OutAppointmentDto;

@Mapper
public interface AppointmentMapper {

    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "time", target = "time")
    @Mapping(source = "date", target = "date", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "patientId", target = "patient.id")
    @Mapping(source = "doctorId", target = "doctor.id")
    Appointment map(IncomingAppointmentDto appointmentDto);


    @Mapping(target = "date", source = "date",dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(target = "time", source = "time", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(target = "specialityDoctor", source = "doctor.speciality")
    @Mapping(target = "nameDoctor", source = "doctor.name")
    OutAppointmentDto map(Appointment appointment);
}
