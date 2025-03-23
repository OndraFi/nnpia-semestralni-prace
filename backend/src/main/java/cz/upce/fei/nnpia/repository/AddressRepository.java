package cz.upce.fei.nnpia.repository;

import cz.upce.fei.nnpia.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findByRestaurantId(long restaurantId);
}
