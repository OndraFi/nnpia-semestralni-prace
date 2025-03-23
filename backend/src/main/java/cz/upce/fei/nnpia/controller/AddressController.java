package cz.upce.fei.nnpia.controller;

import cz.upce.fei.nnpia.domain.Address;
import cz.upce.fei.nnpia.domain.User;
import cz.upce.fei.nnpia.dto.AddressRequestDto;
import cz.upce.fei.nnpia.dto.AddressResponseDto;
import cz.upce.fei.nnpia.service.AddressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Validated
@RequestMapping("/api/v1/restaurants/{restaurant_id}/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<AddressResponseDto> findAddress(@PathVariable @Min(1) long restaurant_id){
        return ResponseEntity.ok(addressService.findAddress(restaurant_id).toResponseDto());
    }

    @PostMapping
    public ResponseEntity<AddressResponseDto> createAddress(@PathVariable @Min(1) long restaurant_id, @RequestBody AddressRequestDto addressRequestDto){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(201).body(addressService.createAddress(restaurant_id, addressRequestDto, user).toResponseDto());
    }

    @PutMapping
    public ResponseEntity<AddressResponseDto> updateAddress(@PathVariable @Min(1) long restaurant_id, @RequestBody @Valid AddressRequestDto addressRequestDto){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(addressService.updateAddress(restaurant_id, addressRequestDto, user).toResponseDto());
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAddress(@PathVariable @Min(1) long restaurant_id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        addressService.deleteAddress(restaurant_id, user);
        return ResponseEntity.noContent().build();
    }
}
