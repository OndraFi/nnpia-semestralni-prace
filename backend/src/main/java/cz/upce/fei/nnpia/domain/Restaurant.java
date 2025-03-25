package cz.upce.fei.nnpia.domain;

import cz.upce.fei.nnpia.dto.AddressResponseDto;
import cz.upce.fei.nnpia.dto.RestaurantResponseDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.List;

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

    public RestaurantResponseDto toResponseDto() {
        AddressResponseDto addressDto = null;
        if(address != null) {
            addressDto = address.toResponseDto();
        }
        return new RestaurantResponseDto(id, name,capacity, openTime,closeTime, addressDto);
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                '}';
    }
}
