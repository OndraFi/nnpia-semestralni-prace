package cz.upce.fei.nnpia.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantResponseDto {

    private long id;
    private String name;
}
