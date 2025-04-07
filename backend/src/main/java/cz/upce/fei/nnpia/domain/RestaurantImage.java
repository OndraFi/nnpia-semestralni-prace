package cz.upce.fei.nnpia.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "app_restaurantImage")
public class RestaurantImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = true, nullable = false, updatable = false)
    private UUID uuid;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @NonNull
    private byte[] data;

    @NonNull
    private String contentType;

    @NonNull
    private String filename;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @NonNull
    private Restaurant restaurant;

    public RestaurantImage(UUID uuid, byte[] data, String contentType, String filename, Restaurant restaurant) {
        this.uuid = uuid;
        this.data = data;
        this.contentType = contentType;
        this.filename = filename;
        this.restaurant = restaurant;
    }

}
