package cz.upce.fei.nnpia.dto;

import cz.upce.fei.nnpia.domain.Restaurant;
import cz.upce.fei.nnpia.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRequestDto {

    @NotNull(message = "Name cannot be null")
    @NotBlank
    private String name;

    public Restaurant toRestaurant(User user) {
        return new Restaurant(name, user);
    }
}
