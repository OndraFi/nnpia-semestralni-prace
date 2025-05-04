package cz.upce.fei.nnpia.service;

import com.mailjet.client.errors.MailjetException;
import cz.upce.fei.nnpia.domain.Reservation;
import cz.upce.fei.nnpia.domain.Restaurant;
import cz.upce.fei.nnpia.domain.User;
import cz.upce.fei.nnpia.domain.value.TimeRange;
import cz.upce.fei.nnpia.dto.ReservationRequestDto;
import cz.upce.fei.nnpia.exception.BadRequestException;
import cz.upce.fei.nnpia.exception.NotFoundException;
import cz.upce.fei.nnpia.exception.OwnershipException;
import cz.upce.fei.nnpia.repository.ReservationRepository;
import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantService restaurantService;
    private final MailJetService mailJetService;

    public ReservationService(ReservationRepository reservationRepository, RestaurantService restaurantService, MailJetService mailJetService) {
        this.reservationRepository = reservationRepository;
        this.restaurantService = restaurantService;
        this.mailJetService = mailJetService;
    }

    public Reservation createReservation(long restaurantId, ReservationRequestDto reservationRequestDto) throws MailjetException {
        if(reservationRequestDto.getStartTime().isBefore(LocalDateTime.now()) || reservationRequestDto.getEndTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Reservation time must be in the future");
        }

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

        Reservation reservation = reservationRequestDto.toReservation(restaurant);

//        System.out.println("DESK" + reservationRequestDto.getDesk().getName());
        reservation.setDesk(reservationRequestDto.getDesk());
        reservationRepository.save(reservation);

        mailJetService.sendReservationEmail(reservationRequestDto, restaurant.getId(), reservation.getUuid());

        return reservation;
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

            // Block times that have already passed today
            if (currentTime.isBefore(LocalDateTime.now())) {
                current = current.plus(granularity);
                continue;
            }

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

    public List<TimeRange> getAvailableTimeBlocksForDesk(@Min(1) long restaurantId, @Min(1) long deskId, LocalDate date) {
        Restaurant restaurant = restaurantService.findRestaurant(restaurantId);

        LocalTime openTime = restaurant.getOpenTime();
        LocalTime closeTime = restaurant.getCloseTime();
        Duration granularity = Duration.ofMinutes(15);

        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = date.atTime(LocalTime.MAX);

        List<Reservation> reservations = reservationRepository.findByDeskIdAndStartTimeBetween(deskId, from, to);

        List<TimeRange> availableRanges = new ArrayList<>();

        LocalTime currentStart = openTime;
        LocalTime current = openTime;
        boolean inFreeBlock = false;

        while (!current.isAfter(closeTime)) {
            LocalDateTime currentTime = LocalDateTime.of(date, current);

            if (currentTime.isBefore(LocalDateTime.now())) {
                current = current.plus(granularity);
                continue;
            }

            boolean isBlocked = reservations.stream()
                    .anyMatch(res -> res.getStartTime().isBefore(currentTime.plus(granularity)) &&
                            res.getEndTime().isAfter(currentTime));

            if (!isBlocked) {
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

        if (inFreeBlock && !current.equals(currentStart)) {
            availableRanges.add(new TimeRange(currentStart, closeTime));
        }

        return availableRanges;
    }

    public List<Reservation> getReservations(@Min(1) long restaurantId, User user) {
        Restaurant restaurant = restaurantService.findRestaurant(restaurantId);
        if (restaurant.getUser().getId() != user.getId()) {
            throw new OwnershipException("You are not the owner of this restaurant");
        }
        List<Reservation> reservations = reservationRepository.findAllByRestaurantId(restaurantId);
        reservations.sort((r1, r2) -> r1.getStartTime().compareTo(r2.getStartTime()));
        return reservations;
    }

    public Reservation acceptReservation(@Min(1) long restaurantId, Long reservationId, User user) throws MailjetException {
        Restaurant restaurant = restaurantService.findRestaurant(restaurantId);
        if (restaurant.getUser().getId() != user.getId()) {
            throw new OwnershipException("You are not the owner of this restaurant");
        }
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found"));



        reservation.setIsAccepted(true);
        reservationRepository.save(reservation);

        mailJetService.sendReservationAcceptedEmail(reservation,restaurant.getId(), reservation.getUuid());

        return reservation;
    }

    public void deleteReservation(@Min(1) long restaurantId, UUID reservationUuid) throws MailjetException {

        if(!reservationRepository.existsByUuid(reservationUuid)) {
            throw new NotFoundException("Reservation not found");
        }
        Reservation reservation = reservationRepository.findByUuid(reservationUuid);
        mailJetService.sendReservationCancelEmail(reservation);
        reservationRepository.deleteByUuid(reservationUuid);
    }
}
