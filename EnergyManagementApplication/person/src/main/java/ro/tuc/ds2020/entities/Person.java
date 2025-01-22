package ro.tuc.ds2020.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Entity
public class Person implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @Column(name = "address", nullable = false)
    private String address;

    @Setter
    @Column(name = "age", nullable = false)
    private int age;

    @Setter
    @Column(name = "role", nullable = false)
    private String role;

    @Setter
    @Column(name = "password", nullable = false)
    private String password;

    public Person() {}

    public Person(String name, String address, int age, String role, String password) {
        this.name = name;
        this.address = address;
        this.age = age;
        this.role = role;
        this.password = password;
    }
}

