package ru.astondevs.servlet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.astondevs.model.DoctorSchedule;
import ru.astondevs.servlet.dto.incomingDto.IncomingScheduleDto;
import ru.astondevs.servlet.dto.outGoingDto.OutDoctorScheduleDto;

@Mapper
public interface DoctorScheduleMapper {

    DoctorScheduleMapper INSTANCE = Mappers.getMapper(DoctorScheduleMapper.class);

    @Mapping(source = "date", target = "date")
    @Mapping(source = "time", target = "time")
    @Mapping(source = "doctorId", target = "doctor.id")
    DoctorSchedule map(IncomingScheduleDto scheduleDto);

    @Mapping(target = "date", source = "date")
    @Mapping(target = "time", source = "time")
    @Mapping(target = "status", source = "booked")
    OutDoctorScheduleDto map(DoctorSchedule doctorSchedule);
}
