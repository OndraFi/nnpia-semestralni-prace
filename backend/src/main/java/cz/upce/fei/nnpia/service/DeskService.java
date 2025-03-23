package cz.upce.fei.nnpia.service;

import cz.upce.fei.nnpia.domain.Desk;
import cz.upce.fei.nnpia.domain.Restaurant;
import cz.upce.fei.nnpia.domain.User;
import cz.upce.fei.nnpia.dto.DeskRequestDto;
import cz.upce.fei.nnpia.exception.NotFoundException;
import cz.upce.fei.nnpia.exception.OwnershipException;
import cz.upce.fei.nnpia.repository.DeskRepository;
import cz.upce.fei.nnpia.repository.RestaurantRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeskService {

    private final DeskRepository deskRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantService restaurantService;

    public DeskService(DeskRepository deskRepository, RestaurantRepository restaurantRepository, RestaurantService restaurantService) {
        this.deskRepository = deskRepository;
        this.restaurantRepository = restaurantRepository;
        this.restaurantService = restaurantService;
    }

    public Desk createDesk(long restaurantId, DeskRequestDto deskRequestDto, User user) {
        Restaurant restaurant = restaurantService.findRestaurant(restaurantId);
        if(restaurant.getUser().getId() != user.getId()){
            throw new OwnershipException("You dont own this restaurant");
        }
        return deskRepository.save(deskRequestDto.toDesk(restaurant));
    }

    public Desk updateDesk(long restaurantId, long id, DeskRequestDto deskRequestDto, User user) {
        Restaurant restaurant = restaurantService.findRestaurant(restaurantId);
        if(restaurant.getUser().getId() != user.getId()){
            throw new OwnershipException("You dont own this restaurant");
        }
        Desk deskToUpdate = deskRepository.findById(id).orElseThrow(()-> new NotFoundException("Desk id:"+id+" not found"));
        deskToUpdate.setName(deskRequestDto.getName());
        deskToUpdate.setCapacity(deskRequestDto.getCapacity());
        return deskRepository.save(deskToUpdate);
    }

    public void deleteDesk( long restaurant_id, long id, User user) {
        Restaurant restaurant = restaurantService.findRestaurant(restaurant_id);
        if(restaurant.getUser().getId() != user.getId()){
            throw new OwnershipException("You dont own this restaurant");
        }
        deskRepository.deleteById(id);
    }

    public Desk findDesk(long id) {
        return deskRepository.findById(id).orElseThrow(() -> new NotFoundException("Desk id:"+id+" not found"));
    }

}
