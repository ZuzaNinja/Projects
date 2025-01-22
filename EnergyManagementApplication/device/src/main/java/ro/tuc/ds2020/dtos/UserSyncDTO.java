package ro.tuc.ds2020.dtos;

import java.util.UUID;

public class UserSyncDTO {

    private UUID id;
    private final String name;

    public UserSyncDTO(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
}
