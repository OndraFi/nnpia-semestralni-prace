package cz.upce.fei.nnpia.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name="app_reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NonNull
    private LocalDateTime startTime; // Čas začátku rezervace

    @NonNull
    private LocalDateTime endTime; // Čas konce rezervace

    @NonNull
    private String phone;

    @NonNull
    private String reservationName;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @NonNull
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
}
