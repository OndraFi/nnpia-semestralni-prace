package cz.upce.fei.nnpia.domain;

import cz.upce.fei.nnpia.dto.AddressResponseDto;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "app_address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NonNull
    private String street;

    @NonNull
    private String city;

    @NonNull
    private String zipCode;

    @NonNull
    private String country;

    @NonNull
    private String buildingNumber;

    @NonNull
    @OneToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", buildingNumber='" + buildingNumber + '\'' +
                ", country='" + country + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                '}';
    }

    public AddressResponseDto toResponseDto() {
        return new AddressResponseDto(id, street, city, zipCode, country, buildingNumber);
    }
}
