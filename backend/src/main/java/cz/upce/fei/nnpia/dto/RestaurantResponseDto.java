package cz.upce.fei.nnpia.dto;

import lombok.*;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

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
    private List<UUID> imagesIds;
    private Boolean usesDesks;
}
