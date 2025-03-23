package cz.upce.fei.nnpia.dto;

import cz.upce.fei.nnpia.domain.Address;
import cz.upce.fei.nnpia.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponseDto {
    private long id;
    private String street;
    private String city;
    private String zipCode;
    private String country;
    private String buildingNumber;
}
