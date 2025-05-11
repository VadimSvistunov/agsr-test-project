package by.svistunovvv.agsr_test.service.impl;

import by.svistunovvv.agsr_test.model.sensor.SensorLocation;
import by.svistunovvv.agsr_test.repository.SensorLocationRepository;
import by.svistunovvv.agsr_test.service.MinorSensorTablesServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SensorLocationService implements MinorSensorTablesServiceInterface<SensorLocation> {
    SensorLocationRepository repository;

    @Override
    public SensorLocation findByNameOrCreate(String name) {
        return repository.findByName(name)
                .orElseGet( () ->
                        repository.save(
                                SensorLocation.builder()
                                        .name(name)
                                        .build()
                        )
                );
    }
}
