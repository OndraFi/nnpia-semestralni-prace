package cz.upce.fei.nnpia.service;

import cz.upce.fei.nnpia.domain.Reservation;
import cz.upce.fei.nnpia.domain.Restaurant;
import cz.upce.fei.nnpia.domain.value.TimeRange;
import cz.upce.fei.nnpia.dto.ReservationRequestDto;
import cz.upce.fei.nnpia.repository.ReservationRepository;
import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantService restaurantService;

    public ReservationService(ReservationRepository reservationRepository, RestaurantService restaurantService) {
        this.reservationRepository = reservationRepository;
        this.restaurantService = restaurantService;
    }

    public Reservation createReservation(long restaurantId, ReservationRequestDto reservationRequestDto){
        Restaurant restaurant = restaurantService.findRestaurant(restaurantId);
        List<Reservation> reservations = reservationRepository.findByRestaurantAndStartTimeBetween(
                restaurant, reservationRequestDto.getStartTime().toLocalDate().atStartOfDay(),
                reservationRequestDto.getStartTime().toLocalDate().atTime(LocalTime.MAX)
        );

        long peopleNow = reservations.stream()
                .filter(res -> res.getStartTime().isBefore(reservationRequestDto.getEndTime()) &&
                        res.getEndTime().isAfter(reservationRequestDto.getStartTime()))
                .mapToLong(Reservation::getNumberOfPeople)
                .sum();

        if (peopleNow + reservationRequestDto.getNumberOfPeople() > restaurant.getCapacity()) {
            throw new IllegalArgumentException("Not enough capacity for the reservation.");
        }

        return reservationRepository.save(reservationRequestDto.toReservation(restaurant));
    }

    public List<TimeRange> getAvailableTimeBlocks(long restaurantId, LocalDate date, int numberOfPeople) {
        Restaurant restaurant = restaurantService.findRestaurant(restaurantId);
        int totalCapacity = restaurant.getCapacity();

        LocalTime openTime = restaurant.getOpenTime();
        LocalTime closeTime = restaurant.getCloseTime();
        Duration granularity = Duration.ofMinutes(15); // jak přesně kontrolujeme čas

        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = date.atTime(LocalTime.MAX); // = 23:59:59.999999999

        List<Reservation> reservations = reservationRepository.findByRestaurantAndStartTimeBetween(restaurant, from, to);

        List<TimeRange> availableRanges = new ArrayList<>();

        LocalTime currentStart = openTime;
        LocalTime current = openTime;
        boolean inFreeBlock = false;

        while (!current.isAfter(closeTime)) {
            LocalDateTime currentTime = LocalDateTime.of(date, current);

            long peopleNow = reservations.stream()
                    .filter(res -> res.getStartTime().isBefore(currentTime.plus(granularity)) &&
                            res.getEndTime().isAfter(currentTime))
                    .mapToLong(Reservation::getNumberOfPeople)
                    .sum();

            boolean enoughSpace = (peopleNow + numberOfPeople <= totalCapacity);

            if (enoughSpace) {
                if (!inFreeBlock) {
                    currentStart = current;
                    inFreeBlock = true;
                }
            } else {
                if (inFreeBlock) {
                    availableRanges.add(new TimeRange(currentStart, current));
                    inFreeBlock = false;
                }
            }

            current = current.plus(granularity);
        }

        // Pokud byl den volný až do konce, přidáme poslední blok
        if (inFreeBlock && !current.equals(currentStart)) {
            availableRanges.add(new TimeRange(currentStart, closeTime));
        }

        return availableRanges;
    }

}
