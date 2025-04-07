package cz.upce.fei.nnpia.service;

import cz.upce.fei.nnpia.domain.Restaurant;
import cz.upce.fei.nnpia.domain.RestaurantImage;
import cz.upce.fei.nnpia.domain.User;
import cz.upce.fei.nnpia.exception.NotFoundException;
import cz.upce.fei.nnpia.exception.OwnershipException;
import cz.upce.fei.nnpia.repository.RestaurantImageRepository;
import cz.upce.fei.nnpia.repository.RestaurantRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class RestaurantImageService {


    private final RestaurantRepository restaurantRepository;
    private final RestaurantImageRepository restaurantImageRepository;

    public RestaurantImageService(RestaurantRepository restaurantRepository, RestaurantImageRepository restaurantImageRepository) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantImageRepository = restaurantImageRepository;
    }

    public void uploadRestaurantImage(Long restaurantId, MultipartFile file, User user) throws IOException {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new NotFoundException("Restaurant not found"));

        if (restaurant.getUser().getId() != user.getId()) {
            throw new OwnershipException("You are not the owner!");
        }

        if (file.getContentType() == null || file.getOriginalFilename() == null) {
            throw new BadRequestException();
        }
        System.out.println(file);
        RestaurantImage image = new RestaurantImage(UUID.randomUUID(),file.getBytes(), file.getContentType(), file.getOriginalFilename(), restaurant);

        restaurantImageRepository.save(image);
    }

    public RestaurantImage findByUuid(UUID uuid) {
        return restaurantImageRepository.findByUuid(uuid).orElseThrow(() -> new NotFoundException("Restaurant not found"));
    }
}
