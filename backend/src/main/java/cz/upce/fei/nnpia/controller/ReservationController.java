package cz.upce.fei.nnpia.controller;

import com.mailjet.client.errors.MailjetException;
import cz.upce.fei.nnpia.domain.Reservation;
import cz.upce.fei.nnpia.domain.User;
import cz.upce.fei.nnpia.domain.value.TimeRange;
import cz.upce.fei.nnpia.dto.ReservationRequestDto;
import cz.upce.fei.nnpia.dto.ReservationResponseDto;
import cz.upce.fei.nnpia.service.ReservationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@Validated
@RequestMapping("/api/v1/restaurants/{restaurantId}/reservations")
public class ReservationController {


    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(@PathVariable @Min(1) long restaurantId, @RequestBody @Valid ReservationRequestDto reservationRequestDto) throws MailjetException {
        Reservation reservation = reservationService.createReservation(restaurantId, reservationRequestDto);
        return ResponseEntity.status(201).body(reservation.toDto());
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<TimeRange>> getAvailableTimes(@PathVariable @Min(1) long restaurantId, @RequestParam LocalDate date, @RequestParam @Min(1) int numberOfPeople) {
        List<TimeRange> availableTimes = reservationService.getAvailableTimeBlocks(restaurantId, date, numberOfPeople);
        return ResponseEntity.ok().body(availableTimes);
    }

    @GetMapping("/available-times/{deskId}")
    public ResponseEntity<List<TimeRange>> getAvailableTimes(@PathVariable @Min(1) long restaurantId, @PathVariable @Min(1) long deskId, @RequestParam LocalDate date) {
        List<TimeRange> availableTimes = reservationService.getAvailableTimeBlocksForDesk(restaurantId, deskId, date);
        return ResponseEntity.ok().body(availableTimes);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getReservations(@PathVariable @Min(1) long restaurantId, @RequestParam(required = false) Boolean accepted, @RequestParam(required = false) String name) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Reservation> reservations = reservationService.getReservations(restaurantId, user, accepted,name);
        return ResponseEntity.ok().body(reservations.stream().map(Reservation::toDto).toList());
    }

    @PostMapping("/{reservationId}/accept")
    public ResponseEntity<ReservationResponseDto> acceptReservation(@PathVariable @Min(1) long restaurantId, @PathVariable Long reservationId) throws MailjetException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(reservationService.acceptReservation(restaurantId, reservationId, user).toDto());
    }

    @Transactional
    @DeleteMapping("/{reservationUuid}")
    public ResponseEntity<Void> deleteReservation(@PathVariable @Min(1) long restaurantId, @PathVariable UUID reservationUuid) throws MailjetException {
        reservationService.deleteReservation(restaurantId, reservationUuid);
        return ResponseEntity.noContent().build();
    }

}
