package cz.upce.fei.nnpia.dto;

import cz.upce.fei.nnpia.domain.Desk;
import cz.upce.fei.nnpia.domain.Reservation;
import cz.upce.fei.nnpia.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDto {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String phone;
    private String email;
    private String reservationName;
    private long numberOfPeople;
    private Desk desk;

    public Reservation toReservation(Restaurant restaurant){
        return new Reservation(startTime,endTime,phone,email,reservationName,numberOfPeople, restaurant);
    }
}
