package cz.upce.fei.nnpia.repository;

import cz.upce.fei.nnpia.domain.RestaurantImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantImageRepository extends JpaRepository<RestaurantImage, Long> {
    Optional<RestaurantImage> findByUuid(UUID uuid);
}
