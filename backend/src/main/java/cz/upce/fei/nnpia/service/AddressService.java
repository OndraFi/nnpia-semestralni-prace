package cz.upce.fei.nnpia.service;

import cz.upce.fei.nnpia.domain.Address;
import cz.upce.fei.nnpia.domain.Restaurant;
import cz.upce.fei.nnpia.domain.User;
import cz.upce.fei.nnpia.dto.AddressRequestDto;
import cz.upce.fei.nnpia.exception.BadRequestException;
import cz.upce.fei.nnpia.exception.NotFoundException;
import cz.upce.fei.nnpia.exception.OwnershipException;
import cz.upce.fei.nnpia.repository.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final RestaurantService restaurantService;

    public AddressService(AddressRepository addressRepository, RestaurantService restaurantService) {
        this.addressRepository = addressRepository;
        this.restaurantService = restaurantService;
    }

    public Address createAddress(long restaurantId, AddressRequestDto addressRequestDto, User user) throws OwnershipException, BadRequestException {
        Restaurant restaurant = findRestaurant(restaurantId,user);
        Address existingAddress = addressRepository.findByRestaurantId(restaurantId);
        if(existingAddress != null){
            throw new BadRequestException("Restaurant already has an address");
        }
        Address address = addressRequestDto.toAddress(restaurant);
        return addressRepository.save(address);
    }

    public Address updateAddress(long restaurantId, AddressRequestDto addressRequestDto, User user) throws OwnershipException, NotFoundException {
        Restaurant restaurant = findRestaurant(restaurantId,user);
        Address existingAddress = findAddressByRestaurantId(restaurantId);
        existingAddress.setCity(addressRequestDto.getCity());
        existingAddress.setStreet(addressRequestDto.getStreet());
        existingAddress.setZipCode(addressRequestDto.getZipCode());
        existingAddress.setCountry(addressRequestDto.getCountry());
        existingAddress.setBuildingNumber(addressRequestDto.getBuildingNumber());
        return addressRepository.save(existingAddress);
    }
    
    public Address findAddress(long restaurantId) throws NotFoundException {
        return findAddressByRestaurantId(restaurantId);
    }
    
    public void deleteAddress(long restaurantId, User user) throws NotFoundException {
        Restaurant restaurant = findRestaurant(restaurantId, user);
        Address existingAddress = findAddressByRestaurantId(restaurantId);
        restaurantService.removeRestaurantAddress(restaurant);
        addressRepository.delete(existingAddress);
    }

    private Restaurant findRestaurant(long restaurantId, User user) throws OwnershipException {
        Restaurant restaurant = restaurantService.findRestaurant(restaurantId);
        if(restaurant.getUser().getId() != user.getId()){
            throw new OwnershipException("You dont own this restaurant");
        }
        return restaurant;
    }

    private Address findAddressByRestaurantId(long restaurantId) throws NotFoundException {
        Address address = addressRepository.findByRestaurantId(restaurantId);
        if(address == null){
            throw new NotFoundException("Address not found");
        }
        return address;
    }
}
