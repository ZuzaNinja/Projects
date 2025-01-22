package ro.tuc.ds2020.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class UserSyncDTO {

    private UUID id;
    private String name;

    public UserSyncDTO(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
