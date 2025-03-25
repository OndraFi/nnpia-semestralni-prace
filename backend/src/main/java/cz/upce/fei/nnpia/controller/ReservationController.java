package cz.upce.fei.nnpia.controller;

import cz.upce.fei.nnpia.domain.Reservation;
import cz.upce.fei.nnpia.domain.value.TimeRange;
import cz.upce.fei.nnpia.dto.ReservationRequestDto;
import cz.upce.fei.nnpia.dto.ReservationResponseDto;
import cz.upce.fei.nnpia.service.ReservationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
    public ResponseEntity<ReservationResponseDto> createReservation(@PathVariable @Min(1) long restaurantId, @RequestBody @Valid ReservationRequestDto reservationRequestDto) {
        Reservation reservation = reservationService.createReservation(restaurantId, reservationRequestDto);
        return ResponseEntity.status(201).body(reservation.toDto());
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<TimeRange>> getAvailableTimes(@PathVariable @Min(1) long restaurantId, @RequestParam LocalDate date, @RequestParam @Min(1) int numberOfPeople) {
        List<TimeRange> availableTimes = reservationService.getAvailableTimeBlocks(restaurantId, date, numberOfPeople);
        return ResponseEntity.ok().body(availableTimes);
    }

}
