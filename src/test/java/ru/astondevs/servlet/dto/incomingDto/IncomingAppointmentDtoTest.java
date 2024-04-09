package ru.astondevs.servlet.dto.incomingDto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IncomingAppointmentDtoTest {
    @Test
    void testConstructor() {
        LocalTime now = LocalTime.now();
        IncomingAppointmentDto dto =
                new IncomingAppointmentDto(LocalDate.now(), now, 123, 456);

        assertEquals(LocalDate.now(), dto.getDate());
        assertEquals(now, dto.getTime());
        assertEquals(123, dto.getPatientId());
        assertEquals(456, dto.getDoctorId());
    }

    @Test
    void testSettersAndGetters() {
        IncomingAppointmentDto dto = new IncomingAppointmentDto();
        LocalTime now = LocalTime.now();
        dto.setDate(LocalDate.now());
        dto.setTime(now);
        dto.setPatientId(123);
        dto.setDoctorId(456);
        assertEquals(LocalDate.now(), dto.getDate());
        assertEquals(now, dto.getTime());
        assertEquals(123, dto.getPatientId());
        assertEquals(456, dto.getDoctorId());
    }
}