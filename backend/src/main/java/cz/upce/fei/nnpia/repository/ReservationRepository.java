package cz.upce.fei.nnpia.repository;

import cz.upce.fei.nnpia.domain.Reservation;
import cz.upce.fei.nnpia.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByRestaurantAndStartTimeBetween(Restaurant restaurant, LocalDateTime from, LocalDateTime to);
}
