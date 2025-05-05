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

        this.createTestData();
    }

    private void createTestData() {
        User user = new User("ondra@gmail.com","$2a$10$KSRH./npzzj79BO8klmGrO2LZT5Jm3qdJMWuK.gg41tQ.eQppp9/y");
        if(!userRepository.existsByEmail(user.getEmail())) {
            userRepository.save(user);
            Restaurant restaurant1 = new Restaurant("Restaurant 1", true, 50, LocalTime.of(8, 0), LocalTime.of(22, 0), user);
            Restaurant restaurant2 = new Restaurant("Restaurant 2", true, 30, LocalTime.of(9, 0), LocalTime.of(21, 0), user);
            Restaurant restaurant3 = new Restaurant("Restaurant 3", true, 20, LocalTime.of(10, 0), LocalTime.of(20, 0), user);
            Restaurant restaurant4 = new Restaurant("Restaurant 4", false, 40, LocalTime.of(11, 0), LocalTime.of(19, 0), user);
            Restaurant restaurant5 = new Restaurant("Restaurant 5", false, 60, LocalTime.of(12, 0), LocalTime.of(18, 0), user);
            Restaurant restaurant6 = new Restaurant("Restaurant 6", false, 70, LocalTime.of(13, 0), LocalTime.of(17, 0), user);
            Restaurant restaurant7 = new Restaurant("Restaurant 7", false, 80, LocalTime.of(14, 0), LocalTime.of(16, 0), user);
            Restaurant restaurant8 = new Restaurant("Restaurant 8", false, 90, LocalTime.of(15, 0), LocalTime.of(15, 0), user);
            Restaurant restaurant9 = new Restaurant("Restaurant 9", false, 100, LocalTime.of(16, 0), LocalTime.of(14, 0), user);
            Restaurant restaurant10 = new Restaurant("Restaurant 10", false, 110, LocalTime.of(17, 0), LocalTime.of(13, 0), user);


            restaurantRepository.save(restaurant1);
            restaurantRepository.save(restaurant2);
            restaurantRepository.save(restaurant3);
            restaurantRepository.save(restaurant4);
            restaurantRepository.save(restaurant5);
            restaurantRepository.save(restaurant6);
            restaurantRepository.save(restaurant7);
            restaurantRepository.save(restaurant8);
            restaurantRepository.save(restaurant9);
            restaurantRepository.save(restaurant10);

            Address address1 = new Address("Street 1", "City 1", "12345", "Country 1", "1", restaurant1);
            Address address2 = new Address("Street 2", "City 2", "23456", "Country 2", "2", restaurant2);
            Address address3 = new Address("Street 3", "City 3", "34567", "Country 3", "3", restaurant3);
            Address address4 = new Address("Street 4", "City 4", "45678", "Country 4", "4", restaurant4);
            Address address5 = new Address("Street 5", "City 5", "56789", "Country 5", "5", restaurant5);
            Address address6 = new Address("Street 6", "City 6", "67890", "Country 6", "6", restaurant6);
            Address address7 = new Address("Street 7", "City 7", "78901", "Country 7", "7", restaurant7);
            Address address8 = new Address("Street 8", "City 8", "89012", "Country 8", "8", restaurant8);
            Address address9 = new Address("Street 9", "City 9", "90123", "Country 9", "9", restaurant9);
            Address address10 = new Address("Street 10", "City 10", "01234", "Country 10", "10", restaurant10);
            addressRepository.save(address1);
            addressRepository.save(address2);
            addressRepository.save(address3);
            addressRepository.save(address4);
            addressRepository.save(address5);
            addressRepository.save(address6);
            addressRepository.save(address7);
            addressRepository.save(address8);
            addressRepository.save(address9);
            addressRepository.save(address10);

            Desk desk1_1 = new Desk("Desk 1", 4, restaurant1);
            Desk desk1_2 = new Desk("Desk 2", 6, restaurant1);
            Desk desk1_3 = new Desk("Desk 3", 8, restaurant1);
            Desk desk1_4 = new Desk("Desk 4", 10, restaurant1);
            Desk desk1_5 = new Desk("Desk 5", 12, restaurant1);

            Desk desk2_1 = new Desk("Desk 1", 4, restaurant2);
            Desk desk2_2 = new Desk("Desk 2", 6, restaurant2);
            Desk desk2_3 = new Desk("Desk 3", 8, restaurant2);
            Desk desk2_4 = new Desk("Desk 4", 10, restaurant2);
            Desk desk2_5 = new Desk("Desk 5", 12, restaurant2);

            Desk desk3_1 = new Desk("Desk 1", 4, restaurant3);
            Desk desk3_2 = new Desk("Desk 2", 6, restaurant3);
            Desk desk3_3 = new Desk("Desk 3", 8, restaurant3);
            Desk desk3_4 = new Desk("Desk 4", 10, restaurant3);
            Desk desk3_5 = new Desk("Desk 5", 12, restaurant3);

            deskRepository.save(desk1_1);
            deskRepository.save(desk1_2);
            deskRepository.save(desk1_3);
            deskRepository.save(desk1_4);
            deskRepository.save(desk1_5);
            deskRepository.save(desk2_1);
            deskRepository.save(desk2_2);
            deskRepository.save(desk2_3);
            deskRepository.save(desk2_4);
            deskRepository.save(desk2_5);
            deskRepository.save(desk3_1);
            deskRepository.save(desk3_2);
            deskRepository.save(desk3_3);
            deskRepository.save(desk3_4);
            deskRepository.save(desk3_5);

        }
    }
}