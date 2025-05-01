package cz.upce.fei.nnpia.domain;

import cz.upce.fei.nnpia.dto.ReservationResponseDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name="app_reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @Column(unique = true, nullable = false, updatable = false)
    private UUID uuid = UUID.randomUUID();

    @NonNull
    private LocalDateTime startTime; // Čas začátku rezervace

    @NonNull
    private LocalDateTime endTime; // Čas konce rezervace

    @NonNull
    private String phone;

    @NonNull
    private String email;

    @NonNull
    private String reservationName;

    @NonNull
    private long numberOfPeople;

    private Boolean isAccepted = false;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "desk_id")
    private Desk desk;

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", reservationName='" + reservationName + '\'' +
                '}';
    }

    public ReservationResponseDto toDto(){
        return new ReservationResponseDto(id,uuid,startTime,endTime,phone,email,reservationName,numberOfPeople,isAccepted);
    }
}
