package ru.astondevs.servlet.dto.incomingDto;

import org.junit.jupiter.api.Test;
import ru.astondevs.model.Doctor;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IncomingDoctorDtoTest {

    @Test
    public void testConstructor() {
        IncomingDoctorDto dto = new IncomingDoctorDto("Петрович", Doctor.Speciality.THERAPIST);
        assertEquals("Петрович", dto.getName());
        assertEquals(Doctor.Speciality.THERAPIST, dto.getSpeciality());
    }

    @Test
    public void testSettersAndGetters() {
        IncomingDoctorDto dto = new IncomingDoctorDto();
        dto.setName("Петрович");
        dto.setSpeciality(Doctor.Speciality.THERAPIST);
        assertEquals("Петрович", dto.getName());
        assertEquals(Doctor.Speciality.THERAPIST, dto.getSpeciality());
    }

}