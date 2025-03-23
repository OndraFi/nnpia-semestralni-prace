package cz.upce.fei.nnpia.repository;

import cz.upce.fei.nnpia.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
