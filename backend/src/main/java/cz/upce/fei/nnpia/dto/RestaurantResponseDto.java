package cz.upce.fei.nnpia.dto;

import lombok.*;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantResponseDto {

    private long id;
    private String name;
    private int capacity;
    private LocalTime openTime;
    private LocalTime closeTime;
    private AddressResponseDto address;
}
