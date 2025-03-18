package cz.upce.fei.nnpia.dto;

import cz.upce.fei.nnpia.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private String email;
    private String password;

    public User toUser(){
        return new User(email, password);
    }
}
