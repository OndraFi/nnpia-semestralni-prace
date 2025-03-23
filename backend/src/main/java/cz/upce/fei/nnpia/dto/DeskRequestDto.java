package cz.upce.fei.nnpia.dto;

import cz.upce.fei.nnpia.domain.Desk;
import cz.upce.fei.nnpia.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeskRequestDto {

    private String name;

    private int capacity;


    public Desk toDesk(Restaurant restaurant) {
        return new Desk(name, capacity, restaurant);
    }
}
