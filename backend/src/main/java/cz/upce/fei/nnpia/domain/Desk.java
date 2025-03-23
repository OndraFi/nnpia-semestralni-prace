package cz.upce.fei.nnpia.domain;

import cz.upce.fei.nnpia.dto.DeskResponseDto;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "app_desk")
public class Desk {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NonNull
    private String name;
    @NonNull
    private int capacity;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "desk", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    public DeskResponseDto toDeskResponseDto() {
        return new DeskResponseDto(id, name, capacity);
    }
}
