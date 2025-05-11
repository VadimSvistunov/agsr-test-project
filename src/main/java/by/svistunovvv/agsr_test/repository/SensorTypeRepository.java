package by.svistunovvv.agsr_test.repository;

import by.svistunovvv.agsr_test.model.sensor.SensorType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensorTypeRepository extends JpaRepository<SensorType, Long> {
    Optional<SensorType> findByName (String name);

}
