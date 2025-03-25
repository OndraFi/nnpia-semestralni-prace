package cz.upce.fei.nnpia.controller;

import cz.upce.fei.nnpia.domain.Restaurant;
import cz.upce.fei.nnpia.domain.User;
import cz.upce.fei.nnpia.dto.RestaurantRequestDto;
import cz.upce.fei.nnpia.dto.RestaurantResponseDto;
import cz.upce.fei.nnpia.service.RestaurantService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Validated
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> findRestaurant(@PathVariable long id) {
        return ResponseEntity.ok().body(restaurantService.findRestaurant(id).toResponseDto());
    }

    @GetMapping("/byUser")
    public ResponseEntity<List<RestaurantResponseDto>> findAllUsersRestaurants() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Restaurant> restaurants = restaurantService.findRestaurantsByUser(user);
        List<RestaurantResponseDto> restaurantResponseDtos = restaurants.stream()
                .map(Restaurant::toResponseDto)
                .toList();
        return ResponseEntity.ok().body(restaurantResponseDtos);
    }

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> findAllRestaurantsPaginated(@RequestParam int page, @RequestParam int size, @RequestParam String searchSlug) {
        List<Restaurant> restaurants = restaurantService.findRestaurantsPaginated(page, size, searchSlug);
        List<RestaurantResponseDto> restaurantResponseDtos = restaurants.stream()
                .map(Restaurant::toResponseDto)
                .toList();
        long totalRestaurants = restaurantService.countRestaurants(searchSlug);
        Map<String, Object> response = new HashMap<>();
        response.put("restaurants", restaurantResponseDtos);
        response.put("total", totalRestaurants);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<RestaurantResponseDto> createRestaurant(@RequestBody @Valid RestaurantRequestDto restaurantRequestDto) {
        log.info("Request for creating restaurant obtained {}", restaurantRequestDto);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("User: {}", user);
        return ResponseEntity.ok().body(restaurantService.createRestaurant(restaurantRequestDto.toRestaurant(user)).toResponseDto());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> updateRestaurant(@PathVariable @Min(1) long id, @RequestBody @Valid RestaurantRequestDto restaurantRequestDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().body(restaurantService.updateRestaurant(id, restaurantRequestDto, user).toResponseDto());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable @Min(1) long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        restaurantService.deleteRestaurant(id,user);
        return ResponseEntity.noContent().build();
    }

}