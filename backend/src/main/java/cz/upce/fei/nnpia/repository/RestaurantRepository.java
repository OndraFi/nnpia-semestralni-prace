package cz.upce.fei.nnpia.repository;

import cz.upce.fei.nnpia.domain.Restaurant;
import cz.upce.fei.nnpia.domain.RestaurantImage;
import cz.upce.fei.nnpia.domain.User;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByUser(@NonNull User user);

    List<Restaurant> findByNameContainingIgnoreCase(String searchSlug, Pageable pageable);

    long countByNameContainsIgnoreCase(String searchSlug);

}
