package by.svistunovvv.agsr_test.model.sensor;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters")
    @Column(nullable = false)
    private String name;
    @NotBlank
    @Size(max = 15, message = "Model must be at most 15 characters")
    @Column(nullable = false)
    private String model;
    @NotNull
    @Min(value = 0, message = "Range From must be a positive integer")
    @Column(nullable = false)
    private Integer rangeFrom;
    @NotNull
    @Min(value = 0, message = "Range To must be a positive integer")
    @Column(nullable = false)
    private Integer rangeTo;
    @Size(max = 200, message = "Description must be at most 200 characters")
    private String description;
    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private SensorType type;
    @ManyToOne
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    private SensorUnitOfMeasure unit;
    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private SensorLocation location;
}
