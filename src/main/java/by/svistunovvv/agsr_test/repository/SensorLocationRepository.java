package by.svistunovvv.agsr_test.repository;

import by.svistunovvv.agsr_test.model.sensor.SensorLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensorLocationRepository extends JpaRepository<SensorLocation, Long> {
    Optional<SensorLocation> findByName (String name);
}
