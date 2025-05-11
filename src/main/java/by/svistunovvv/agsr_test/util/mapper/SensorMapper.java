package by.svistunovvv.agsr_test.util.mapper;

import by.svistunovvv.agsr_test.model.sensor.Sensor;
import by.svistunovvv.agsr_test.model.sensor.SensorLocation;
import by.svistunovvv.agsr_test.model.sensor.SensorType;
import by.svistunovvv.agsr_test.model.sensor.SensorUnitOfMeasure;
import by.svistunovvv.agsr_test.model.dto.RangeDto;
import by.svistunovvv.agsr_test.model.dto.SensorDto;
import by.svistunovvv.agsr_test.service.impl.SensorLocationService;
import by.svistunovvv.agsr_test.service.impl.SensorTypeService;
import by.svistunovvv.agsr_test.service.impl.SensorUnitOfMeasureService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SensorMapper {
    private SensorLocationService sensorLocationService;
    private SensorTypeService sensorTypeService;
    private SensorUnitOfMeasureService sensorUnitOfMeasureService;

    public Sensor toEntity(SensorDto sensorDto) {
        SensorType type = sensorTypeService.findByNameOrCreate(sensorDto.type());

        SensorLocation location = !sensorDto.location().isEmpty() ? sensorLocationService.findByNameOrCreate(sensorDto.location()) : null;
        SensorUnitOfMeasure unitOfMeasure = !sensorDto.location().isEmpty() ? sensorUnitOfMeasureService.findByNameOrCreate(sensorDto.unit()) : null;
        String description = !sensorDto.description().isEmpty() ? sensorDto.description() : null;


        return Sensor.builder()
                .name(sensorDto.name())
                .description(description)
                .rangeFrom(sensorDto.range().from())
                .rangeTo(sensorDto.range().to())
                .model(sensorDto.model())
                .location(location)
                .type(type)
                .unit(unitOfMeasure)
                .build();
    }

    public SensorDto toDto(Sensor sensor) {
        String unit = sensor.getUnit() != null ? sensor.getUnit().getName() : "";
        String location = sensor.getLocation() != null ? sensor.getLocation().getName() : "";
        String description = sensor.getDescription() != null ? sensor.getDescription() : "";

        return new SensorDto(
                sensor.getName(),
                sensor.getModel(),
                new RangeDto(sensor.getRangeFrom(), sensor.getRangeTo()),
                sensor.getType().getName(),
                unit,
                location,
                description
        );
    }

    public List<SensorDto> listToDto(List<Sensor> sensors) {
        return sensors.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Sensor updateEntity(SensorDto dto, Sensor entity) {
        entity.setName(dto.name());
        entity.setModel(dto.model());
        entity.setRangeFrom(dto.range().from());
        entity.setRangeTo(dto.range().to());
        entity.setType(sensorTypeService.findByNameOrCreate(dto.type()));

        if (dto.unit() != null) {
            entity.setUnit(sensorUnitOfMeasureService.findByNameOrCreate(dto.unit()));
        }
        if (dto.location() != null) {
            entity.setLocation(sensorLocationService.findByNameOrCreate(dto.location()));
        }
        if (dto.description() != null) {
            entity.setDescription(dto.description());
        }

        return entity;
    }
}
