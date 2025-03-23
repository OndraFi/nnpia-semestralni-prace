package cz.upce.fei.nnpia.dto;

import cz.upce.fei.nnpia.domain.Reservation;
import cz.upce.fei.nnpia.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeskResponseDto {

    private long id;
    private String name;
    private int capacity;
}
