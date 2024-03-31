package ru.astondevs.servlet.dto.incomingDto;

public class IncomingPatientDto {

    private long id;
    private String name;
    private int age;
    private String number;

    public IncomingPatientDto() {

    }

    public IncomingPatientDto(long id, String name, int age, String number) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
