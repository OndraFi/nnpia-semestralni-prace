package cz.upce.fei.nnpia.controller;

import com.mailjet.client.errors.MailjetException;
import cz.upce.fei.nnpia.domain.Desk;
import cz.upce.fei.nnpia.domain.User;
import cz.upce.fei.nnpia.dto.DeskRequestDto;
import cz.upce.fei.nnpia.dto.DeskResponseDto;
import cz.upce.fei.nnpia.service.DeskService;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Slf4j
@Validated
@RequestMapping("/api/v1/restaurants/{restaurant_id}/desks")
public class DeskController {

    private final DeskService deskService;

    public DeskController(DeskService deskService) {
        this.deskService = deskService;
    }

    @GetMapping("")
    public ResponseEntity<Collection<DeskResponseDto>> findAllDesks(@PathVariable @Min(1) long restaurant_id) {
        return ResponseEntity.ok(deskService.findAllDesks(restaurant_id).stream().map(Desk::toDeskResponseDto).toList());
    }

    @GetMapping("/{desk_id}")
    public ResponseEntity<DeskResponseDto> findDesk(@PathVariable  @Min(1) long restaurant_id, @PathVariable @Min(1)  long desk_id) {
        return ResponseEntity.ok(deskService.findDesk(desk_id).toDeskResponseDto());
    }

    @PostMapping
    public ResponseEntity<DeskResponseDto> createDesk(@PathVariable @Min(1) long restaurant_id, @RequestBody DeskRequestDto deskRequestDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(deskService.createDesk(restaurant_id, deskRequestDto,user).toDeskResponseDto());
    }

    @PutMapping("/{desk_id}")
    public ResponseEntity<DeskResponseDto> updateDesk(@PathVariable @Min(1) long restaurant_id, @PathVariable @Min(1) long desk_id, @RequestBody DeskRequestDto deskRequestDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(deskService.updateDesk(restaurant_id, desk_id, deskRequestDto,user).toDeskResponseDto());
    }

    @DeleteMapping("/{desk_id}")
    public ResponseEntity<?> deleteDesk(@PathVariable @Min(1) long restaurant_id, @PathVariable @Min(1) long desk_id) throws MailjetException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        deskService.deleteDesk(restaurant_id, desk_id,user);
        return ResponseEntity.noContent().build();
    }
}
