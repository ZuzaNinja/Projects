package ro.tuc.ds2020.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class PersonDTO {
    private UUID id;
    private String name;
    private String address;
    private int age;
    private String role;
    private String password;

    public PersonDTO(UUID id, String name, String address, int age, String role, String password) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.age = age;
        this.role = role;
        this.password = password;
    }

}
