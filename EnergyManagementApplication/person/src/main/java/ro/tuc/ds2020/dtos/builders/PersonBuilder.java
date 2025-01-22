package ro.tuc.ds2020.dtos.builders;

import org.springframework.stereotype.Component;
import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.entities.Person;

@Component
public class PersonBuilder {

    public PersonDTO toPersonDTO(Person person) {
        return new PersonDTO(person.getId(), person.getName(), person.getAddress(), person.getAge(), person.getRole(), person.getPassword());
    }

}
