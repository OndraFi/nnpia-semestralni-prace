package cz.upce.fei.nnpia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDto {

    private long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String phone;
    private String email;
    private String reservationName;
    private long numberOfPeople;
    private Boolean isAccepted;
}
