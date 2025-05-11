package by.svistunovvv.agsr_test.repository;

import by.svistunovvv.agsr_test.model.sensor.SensorUnitOfMeasure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensorUnitOfMeasureRepository extends JpaRepository<SensorUnitOfMeasure, Long> {
    Optional<SensorUnitOfMeasure> findByName (String name);

}
