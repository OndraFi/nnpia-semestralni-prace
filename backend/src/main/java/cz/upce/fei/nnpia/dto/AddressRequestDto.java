package cz.upce.fei.nnpia.dto;

import cz.upce.fei.nnpia.domain.Address;
import cz.upce.fei.nnpia.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequestDto {
    private String street;
    private String city;
    private String zipCode;
    private String country;
    private String buildingNumber;

    public Address toAddress(Restaurant restaurant) {
        return new Address(street, city, zipCode, country, buildingNumber,restaurant);
    }
}
