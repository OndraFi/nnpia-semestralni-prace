package cz.upce.fei.nnpia.service;

import cz.upce.fei.nnpia.domain.Restaurant;
import cz.upce.fei.nnpia.domain.User;
import cz.upce.fei.nnpia.dto.RestaurantRequestDto;
import cz.upce.fei.nnpia.exception.BadRequestException;
import cz.upce.fei.nnpia.exception.OwnershipException;
import cz.upce.fei.nnpia.exception.RestaurantNotFoundException;
import cz.upce.fei.nnpia.repository.RestaurantRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant createRestaurant(Restaurant restaurant) {
        if (restaurant.getOpenTime().plusHours(1).isAfter(restaurant.getCloseTime())) {
            throw new BadRequestException("open time has to be lower than close time!");
        }
        return restaurantRepository.save(restaurant);
    }

    public Restaurant updateRestaurant(long id, RestaurantRequestDto restaurantRequestDto, User user) throws RestaurantNotFoundException, OwnershipException {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(()-> new RestaurantNotFoundException(id));
        if(restaurant.getUser().getId() != user.getId()) {
            throw new OwnershipException("You are not the owner of this restaurant");
        }
        restaurant.setName(restaurantRequestDto.getName());
        restaurant.setCapacity(restaurantRequestDto.getCapacity());
        restaurant.setOpenTime(restaurantRequestDto.getOpenTime());
        restaurant.setCloseTime(restaurantRequestDto.getCloseTime());
        restaurant.setUsesDesks(restaurantRequestDto.getUsesDesks());
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

    public List<Restaurant> findRestaurantsByUser(User user){
        return restaurantRepository.findByUser(user);
    }

    public List<Restaurant> findRestaurantsPaginated(int page, int size, String searchSlug) {
        Pageable pageable = PageRequest.of(page, size);
        if (searchSlug != null && !searchSlug.isEmpty()) {
            return restaurantRepository.findByNameContainingIgnoreCase(searchSlug, pageable);
        } else {
            return restaurantRepository.findAll(pageable).getContent();
        }
    }

    public long countRestaurants(String searchSlug) {
        if(searchSlug != null && !searchSlug.isEmpty()) {
            return restaurantRepository.countByNameContainsIgnoreCase(searchSlug);
        }else{
            return restaurantRepository.count();
        }
    }

    public void removeRestaurantAddress(Restaurant restaurant){
        restaurant.setAddress(null);
        restaurantRepository.save(restaurant);
    }
}