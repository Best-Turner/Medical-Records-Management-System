package ru.astondevs.servlet.dto.incomingDto;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IncomingPatientDtoTest {

    @Test
    void testConstructor() {
        IncomingPatientDto dto = new IncomingPatientDto("Петрович", 30, "123-456-7890");
        assertEquals("Петрович", dto.getName());
        assertEquals(30, dto.getAge());
        assertEquals("123-456-7890", dto.getNumber());
    }

    @Test
    void testSettersAndGetters() {
        IncomingPatientDto dto = new IncomingPatientDto();

        dto.setName("Петрович");
        dto.setAge(30);
        dto.setNumber("123-456-7890");

        assertEquals("Петрович", dto.getName());
        assertEquals(30, dto.getAge());
        assertEquals("123-456-7890", dto.getNumber());
    }

}