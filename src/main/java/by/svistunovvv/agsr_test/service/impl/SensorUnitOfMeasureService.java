package by.svistunovvv.agsr_test.service.impl;

import by.svistunovvv.agsr_test.model.sensor.SensorUnitOfMeasure;
import by.svistunovvv.agsr_test.repository.SensorUnitOfMeasureRepository;
import by.svistunovvv.agsr_test.service.MinorSensorTablesServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SensorUnitOfMeasureService implements MinorSensorTablesServiceInterface<SensorUnitOfMeasure> {
    SensorUnitOfMeasureRepository repository;

    @Override
    public SensorUnitOfMeasure findByNameOrCreate(String name) {
        return repository.findByName(name)
                .orElseGet( () ->
                        repository.save(
                                SensorUnitOfMeasure.builder()
                                        .name(name)
                                        .build()
                        )
                );
    }
}
