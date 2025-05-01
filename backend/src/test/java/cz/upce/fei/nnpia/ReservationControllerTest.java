package cz.upce.fei.nnpia;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.upce.fei.nnpia.config.JwtAuthenticationFilter;
import cz.upce.fei.nnpia.controller.ReservationController;
import cz.upce.fei.nnpia.domain.Reservation;
import cz.upce.fei.nnpia.domain.value.TimeRange;
import cz.upce.fei.nnpia.dto.ReservationRequestDto;
import cz.upce.fei.nnpia.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void shouldCreateReservationAndReturn201() throws Exception {
        long restaurantId = 1L;

        ReservationRequestDto dto = new ReservationRequestDto();
        dto.setStartTime(LocalDateTime.now().plusHours(1));
        dto.setEndTime(LocalDateTime.now().plusHours(2));
        dto.setEmail("test@test.cz");
        dto.setPhone("123456789");
        dto.setReservationName("Honza");
        dto.setNumberOfPeople(4);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // pokud používáš LocalDateTime
        String jsonRequestBody = mapper.writeValueAsString(dto);

        when(reservationService.createReservation(eq(restaurantId), any())).thenReturn(new Reservation());

        mockMvc.perform(post("/api/v1/restaurants/1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)   // ← nutné pro .content()
                        .content(jsonRequestBody))                 // ← jinak nebude fungovat
                .andExpect(status().isCreated());
    }


    @Test
    void shouldReturnAvailableTimes() throws Exception {
        long restaurantId = 1L;
        String date = "2025-04-10";
        int people = 4;

        List<TimeRange> available = List.of(
                new TimeRange(LocalTime.of(8, 0), LocalTime.of(9, 0)),
                new TimeRange(LocalTime.of(10, 0), LocalTime.of(12, 0))
        );

        when(reservationService.getAvailableTimeBlocks(eq(restaurantId), eq(LocalDate.parse(date)), eq(people)))
                .thenReturn(available);

        mockMvc.perform(get("/api/v1/restaurants/{id}/reservations/available-times", restaurantId)
                        .param("date", date)
                        .param("numberOfPeople", String.valueOf(people)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].start").value("08:00:00"))
                .andExpect(jsonPath("$[1].end").value("12:00:00"));
    }
}
