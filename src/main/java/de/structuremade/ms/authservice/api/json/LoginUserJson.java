package de.structuremade.ms.authservice.api.json;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class LoginUserJson {
    @NotEmpty(message = "E-Mail or Password false")
    private String email;
    @NotEmpty(message = "E-Mail or Password false")
    private String password;
}
