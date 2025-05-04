package cz.upce.fei.nnpia.repository;

import cz.upce.fei.nnpia.domain.Desk;
import cz.upce.fei.nnpia.domain.Restaurant;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface DeskRepository extends JpaRepository<Desk, Long> {
    List<Desk> findByRestaurant(@NonNull Restaurant restaurant);
}
