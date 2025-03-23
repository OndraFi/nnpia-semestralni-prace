package cz.upce.fei.nnpia.repository;

import cz.upce.fei.nnpia.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
