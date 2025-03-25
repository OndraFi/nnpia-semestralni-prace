package cz.upce.fei.nnpia.dto;

import cz.upce.fei.nnpia.domain.Restaurant;
import cz.upce.fei.nnpia.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRequestDto {

    @NotNull(message = "Name cannot be null")
    @NotBlank
    private String name;

    @NotNull(message = "Cpacity cannot be null")
    private int capacity;

    @NotNull
    private LocalTime openTime;

    @NotNull
    private LocalTime closeTime;

    public Restaurant toRestaurant(User user) {
        return new Restaurant(name,capacity,openTime,closeTime, user);
    }
}
