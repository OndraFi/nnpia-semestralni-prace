package cz.upce.fei.nnpia.responses;

import lombok.Getter;
import lombok.Setter;

public class LoginResponse {
    @Getter
    @Setter
    private String token;

    @Setter
    @Getter
    private long expiresIn;

}