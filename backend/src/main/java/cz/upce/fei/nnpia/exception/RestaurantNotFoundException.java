package cz.upce.fei.nnpia.exception;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(Long id) {
        super("Restaurant with ID " + id + " not found.");
    }
}
