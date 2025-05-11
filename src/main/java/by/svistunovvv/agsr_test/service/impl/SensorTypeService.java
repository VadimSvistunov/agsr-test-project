package by.svistunovvv.agsr_test.service.impl;

import by.svistunovvv.agsr_test.model.sensor.SensorType;
import by.svistunovvv.agsr_test.repository.SensorTypeRepository;
import by.svistunovvv.agsr_test.service.MinorSensorTablesServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SensorTypeService implements MinorSensorTablesServiceInterface<SensorType> {
    SensorTypeRepository repository;

    @Override
    public SensorType findByNameOrCreate(String name) {
        return repository.findByName(name)
                .orElseGet( () ->
                        repository.save(
                                SensorType.builder()
                                        .name(name)
                                        .build()
                        )
                );
    }
}
