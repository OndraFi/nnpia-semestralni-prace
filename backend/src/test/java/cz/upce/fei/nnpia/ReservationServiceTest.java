package cz.upce.fei.nnpia;

import cz.upce.fei.nnpia.domain.Reservation;
import cz.upce.fei.nnpia.domain.Restaurant;
import cz.upce.fei.nnpia.domain.User;
import cz.upce.fei.nnpia.domain.value.TimeRange;
import cz.upce.fei.nnpia.dto.ReservationRequestDto;
import cz.upce.fei.nnpia.repository.ReservationRepository;
import cz.upce.fei.nnpia.service.MailJetService;
import cz.upce.fei.nnpia.service.ReservationService;
import cz.upce.fei.nnpia.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private MailJetService mailJetService;


    @Test
    void shouldReturnAvailableTimeBlocksRespectingCapacity() {
        // Arrange
        long restaurantId = 1L;
        LocalDate date = LocalDate.of(2025, 5, 10);
        int numberOfPeople = 4;

        Restaurant restaurant = new Restaurant("Test",false, 10,
                LocalTime.of(8, 0), LocalTime.of(12, 0), new User());

        when(restaurantService.findRestaurant(restaurantId)).thenReturn(restaurant);

        // Existující rezervace od 9:00 do 10:00 pro 6 lidí
        Reservation res1 = new Reservation(
                LocalDateTime.of(2025, 5, 10, 9, 0),
                LocalDateTime.of(2025, 5, 10, 10, 0),
                "646778888", "mail@test.cz", "Jirka", 10, restaurant
        );

        when(reservationRepository.findByRestaurantAndStartTimeBetween(
                eq(restaurant),
                eq(date.atStartOfDay()),
                eq(date.atTime(LocalTime.MAX))
        )).thenReturn(List.of(res1));

        // Act
        List<TimeRange> result = reservationService.getAvailableTimeBlocks(restaurantId, date, numberOfPeople);

        // Assert
        assertFalse(result.isEmpty());

        boolean conflictSlotPresent = result.stream()
                .anyMatch(range ->
                        range.start().equals(LocalTime.of(9, 0)) &&
                                range.end().equals(LocalTime.of(10, 0))
                );

        assertFalse(result.contains(new TimeRange(LocalTime.of(9, 0), LocalTime.of(10, 0))),"Slot 9:00–10:00 by měl být blokovaný");

    }
    @Test
    void shouldCreateReservationWhenEnoughCapacity() throws Exception {
        // Arrange
        long restaurantId = 1L;

        LocalDateTime now = LocalDateTime.now().plusHours(2);
        LocalDateTime later = now.plusHours(1);

        Restaurant restaurant = new Restaurant("Test", false, 10,
                LocalTime.of(8, 0), LocalTime.of(20, 0), new User());

        Reservation existingRes = new Reservation(
                now, later,
                "111222333", "test@test.cz", "Adam", 4, restaurant
        );

        ReservationRequestDto req = new ReservationRequestDto(
                now,
                later,
                "333222111",
                "new@test.cz",
                "Eva",
                4,
                null
        );

        when(restaurantService.findRestaurant(restaurantId)).thenReturn(restaurant);
        when(reservationRepository.findByRestaurantAndStartTimeBetween(
                eq(restaurant),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of(existingRes));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Reservation result = reservationService.createReservation(restaurantId, req);

        // Assert
        assertEquals(req.getEmail(), result.getEmail());
        assertEquals(req.getNumberOfPeople(), result.getNumberOfPeople());
        assertEquals(req.getStartTime(), result.getStartTime());
    }

}