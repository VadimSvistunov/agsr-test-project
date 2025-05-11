package by.svistunovvv.agsr_test.repository;

import by.svistunovvv.agsr_test.model.sensor.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
    List<Sensor> findSensorsByNameContains(String nameContains);
    List<Sensor> findSensorsByModelContains(String nameContains);
}
