package cz.upce.fei.nnpia;

import cz.upce.fei.nnpia.domain.Address;
import cz.upce.fei.nnpia.domain.Restaurant;
import cz.upce.fei.nnpia.domain.User;
import cz.upce.fei.nnpia.dto.AddressRequestDto;
import cz.upce.fei.nnpia.repository.AddressRepository;
import cz.upce.fei.nnpia.repository.RestaurantRepository;
import cz.upce.fei.nnpia.repository.UserRepository;
import cz.upce.fei.nnpia.service.AddressService;
import cz.upce.fei.nnpia.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class RestaurantIntegrationTest {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setup() {
        addressRepository.deleteAll();
        restaurantRepository.deleteAll();
        userRepository.deleteAll();

        user = new User("owner@example.com", "password123");
        userRepository.save(user);
    }

    @Test
    void shouldCreateRestaurantAndAddAddress() {
        Restaurant restaurant = new Restaurant(
                "U Tygra",
                false,
                30,
                LocalTime.of(9, 0),
                LocalTime.of(22, 0),
                user
        );
        Restaurant created = restaurantService.createRestaurant(restaurant);

        assertNotNull(created);
        assertEquals("U Tygra", created.getName());

        AddressRequestDto addressRequestDto = new AddressRequestDto(
                "Ulice",
                "Praha",
                "111 00",
                "CZ",
                "12"
        );
        addressService.createAddress(created.getId(), addressRequestDto,user);

        Restaurant loaded = restaurantRepository.findById(created.getId()).orElseThrow();
        assertNotNull(loaded.getAddress());
        assertEquals("Praha", loaded.getAddress().getCity());
    }
}

