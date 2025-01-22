package ro.tuc.ds2020.dtos;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class LoginRequestDTO {

    @NotNull
    private String username;

    @NotNull
    private String password;

    public LoginRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
