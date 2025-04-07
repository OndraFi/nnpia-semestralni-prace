package cz.upce.fei.nnpia.domain;

import cz.upce.fei.nnpia.dto.AddressResponseDto;
import cz.upce.fei.nnpia.dto.RestaurantResponseDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "app_restaurant")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NonNull
    private String name;

    @NonNull
    private int capacity;
    @NonNull
    private LocalTime openTime;
    @NonNull
    private LocalTime closeTime;
    @NonNull
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;



    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Desk> desks;

    @OneToOne(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RestaurantImage> images;

    public RestaurantResponseDto toResponseDto() {
        AddressResponseDto addressDto = null;
        if(address != null) {
            addressDto = address.toResponseDto();
        }
        List<UUID> imageUUIDs = new LinkedList<>();
        if(images != null) {

         imageUUIDs = images.stream().map(RestaurantImage::getUuid).toList();
        }
        return new RestaurantResponseDto(id, name,capacity, openTime,closeTime, addressDto, imageUUIDs);
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                '}';
    }
}
