package ro.tuc.ds2020.dtos;

import lombok.Getter;
import lombok.Setter;
import ro.tuc.ds2020.dtos.validators.annotation.AgeLimit;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Setter
@Getter
public class PersonDetailsDTO {

    private UUID id;
    @NotNull
    private String name;
    @NotNull
    private String address;
    @AgeLimit(limit = 18)
    private int age;
    @NotNull
    private String role;
    @NotNull
    private String password;

}
