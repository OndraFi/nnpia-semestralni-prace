package cz.upce.fei.nnpia.component;

import cz.upce.fei.nnpia.domain.*;
import cz.upce.fei.nnpia.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReservationRepository reservationRepository;
    private final AddressRepository addressRepository;
    private final DeskRepository deskRepository;

    public DataInitializer(UserRepository userRepository, RestaurantRepository restaurantRepository, ReservationRepository reservationRepository, AddressRepository addressRepository, DeskRepository deskRepository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.reservationRepository = reservationRepository;
        this.addressRepository = addressRepository;
        this.deskRepository = deskRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        User user = new User("admin@gmail.com", "$2a$10$KSRH./npzzj79BO8klmGrO2LZT5Jm3qdJMWuK.gg41tQ.eQppp9/y");
        if(!userRepository.existsByEmail(user.getEmail())) {
            log.info("Creating user {}", user.getEmail());
            userRepository.save(user);

            LocalTime openTime = LocalTime.of(8, 0);
            LocalTime closeTime = LocalTime.of(22, 0);
            Restaurant restaurant = new Restaurant("Admin Restaurant",false,50,openTime,closeTime,user);
            if(!restaurantRepository.existsById(restaurant.getId())) {
                log.info("Creating restaurant {}", restaurant.getId());
                restaurantRepository.save(restaurant);
            }

            Address resetaurantAddress = new Address("Hlavní","Pardubice", "285 66", "Czech Republic", "7", restaurant);
            addressRepository.saveAndFlush(resetaurantAddress);

            Desk desk = new Desk("1",6, restaurant);
            Desk desk2 = new Desk("2",10, restaurant);
            Desk desk3 = new Desk("3",2, restaurant);
            Desk desk4 = new Desk("4",4, restaurant);
            Desk desk5 = new Desk("5",2, restaurant);

            deskRepository.saveAndFlush(desk);
            deskRepository.saveAndFlush(desk2);
            deskRepository.saveAndFlush(desk3);
            deskRepository.saveAndFlush(desk4);
            deskRepository.saveAndFlush(desk5);

            LocalDateTime start = LocalDateTime.now();
            LocalDateTime end = start.plusHours(2);
            Reservation reservation = new Reservation(start,end,"123456789","fialka.ondra@gmail.com","Admin", 5,restaurant);
            if(!reservationRepository.existsById(reservation.getId())) {
                log.info("Creating reservation {}", reservation.getId());
                reservationRepository.save(reservation);
            }

            LocalDateTime start2 = end.plusHours(1);
            LocalDateTime end2 = start2.plusHours(2);
            Reservation reservation2 = new Reservation(start2,end2,"987654321","fialka.ondra@gmail.com","Test user", 2,restaurant);

            if(!reservationRepository.existsById(reservation2.getId())) {
                log.info("Creating reservation {}", reservation2.getId());
                reservationRepository.save(reservation2);
            }
        }
    }
}