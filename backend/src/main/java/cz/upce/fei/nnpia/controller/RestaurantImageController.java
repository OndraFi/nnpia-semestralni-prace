package cz.upce.fei.nnpia.controller;

import cz.upce.fei.nnpia.domain.Restaurant;
import cz.upce.fei.nnpia.domain.RestaurantImage;
import cz.upce.fei.nnpia.domain.User;
import cz.upce.fei.nnpia.service.RestaurantImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@Validated
@RequestMapping("/api/v1/restaurant-images")
public class RestaurantImageController {

    private final RestaurantImageService restaurantImageService;

    public RestaurantImageController(RestaurantImageService restaurantImageService) {
        this.restaurantImageService = restaurantImageService;
    }

    @PostMapping("/restaurant/{restaurantId}")
    public ResponseEntity<?> uploadImage(
            @PathVariable Long restaurantId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        restaurantImageService.uploadRestaurantImage(restaurantId,file,user);
        return ResponseEntity.status(201).build();
    }

    @Transactional(readOnly = true)
    @GetMapping("/{uuid}")
    public ResponseEntity<byte[]> getImage(@PathVariable UUID uuid) {
        RestaurantImage image = restaurantImageService.findByUuid(uuid);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .body(image.getData());
    }


}
