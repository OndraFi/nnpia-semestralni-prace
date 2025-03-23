package cz.upce.fei.nnpia.service;

import cz.upce.fei.nnpia.domain.Restaurant;
import cz.upce.fei.nnpia.domain.User;
import cz.upce.fei.nnpia.dto.RestaurantRequestDto;
import cz.upce.fei.nnpia.exception.OwnershipException;
import cz.upce.fei.nnpia.exception.RestaurantNotFoundException;
import cz.upce.fei.nnpia.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public Restaurant updateRestaurant(long id, RestaurantRequestDto restaurantRequestDto, User user) throws RestaurantNotFoundException, OwnershipException {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(()-> new RestaurantNotFoundException(id));
        if(restaurant.getUser().getId() != user.getId()) {
            throw new OwnershipException("You are not the owner of this restaurant");
        }
        restaurant.setName(restaurantRequestDto.getName());
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    public void deleteRestaurant(long id, User user) throws RestaurantNotFoundException, OwnershipException {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(()-> new RestaurantNotFoundException(id));
        if(restaurant.getUser().getId() != user.getId()) {
            throw new OwnershipException("You are not the owner of this restaurant");
        }
        restaurantRepository.deleteById(id);
    }

    public Restaurant findRestaurant(long id) throws RestaurantNotFoundException {
        return restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException(id));
    }

}
