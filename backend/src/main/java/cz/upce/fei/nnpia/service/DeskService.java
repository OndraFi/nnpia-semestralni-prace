package cz.upce.fei.nnpia.service;

import com.mailjet.client.errors.MailjetException;
import cz.upce.fei.nnpia.domain.Desk;
import cz.upce.fei.nnpia.domain.Reservation;
import cz.upce.fei.nnpia.domain.Restaurant;
import cz.upce.fei.nnpia.domain.User;
import cz.upce.fei.nnpia.dto.DeskRequestDto;
import cz.upce.fei.nnpia.exception.NotFoundException;
import cz.upce.fei.nnpia.exception.OwnershipException;
import cz.upce.fei.nnpia.repository.DeskRepository;
import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class DeskService {

    private final DeskRepository deskRepository;
    private final RestaurantService restaurantService;
    private final ReservationService reservationService;

    public DeskService(DeskRepository deskRepository, RestaurantService restaurantService, ReservationService reservationService) {
        this.deskRepository = deskRepository;
        this.restaurantService = restaurantService;
        this.reservationService = reservationService;
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

    public void deleteDesk( long restaurant_id, long id, User user) throws MailjetException {
        Restaurant restaurant = restaurantService.findRestaurant(restaurant_id);
        if(restaurant.getUser().getId() != user.getId()){
            throw new OwnershipException("You dont own this restaurant");
        }

        deskRepository.deleteById(id);

        // Get all reservations for the desk and delete them
        List<Reservation> reservations = reservationService.findReservationsByDeskId(id);
        for (Reservation reservation : reservations) {
            reservationService.deleteReservation(restaurant.getId(),reservation.getUuid());
        }

    }

    public Desk findDesk(long id) {
        return deskRepository.findById(id).orElseThrow(() -> new NotFoundException("Desk id:"+id+" not found"));
    }

    public Collection<Desk> findAllDesks(@Min(1) long restaurantId) {
        Restaurant restaurant = restaurantService.findRestaurant(restaurantId);
        return deskRepository.findByRestaurant(restaurant);
    }
}
