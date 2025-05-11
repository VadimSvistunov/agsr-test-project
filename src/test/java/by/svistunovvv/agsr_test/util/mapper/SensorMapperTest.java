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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SensorMapperTest {

    @Mock
    private SensorTypeService sensorTypeService;
    @Mock
    private SensorLocationService sensorLocationService;
    @Mock
    private SensorUnitOfMeasureService sensorUnitOfMeasureService;

    @InjectMocks
    private SensorMapper mapperUtil;

    @Test
    void toEntitySuccessAllFields() {
        SensorDto dto = new SensorDto(
                "Barometer",
                "ac-23",
                new RangeDto(22, 45),
                "Pressure",
                "bar",
                "kitchen",
                "Indoor sensor"
        );

        SensorType mockType = new SensorType();
        mockType.setId(1L);
        mockType.setName("Pressure");

        SensorLocation mockLocation = new SensorLocation();
        mockLocation.setId(1L);
        mockLocation.setName("kitchen");

        SensorUnitOfMeasure mockUnit = new SensorUnitOfMeasure();
        mockUnit.setId(1L);
        mockUnit.setName("bar");

        Mockito.when(sensorTypeService.findByNameOrCreate("Pressure")).thenReturn(mockType);
        Mockito.when(sensorLocationService.findByNameOrCreate("kitchen")).thenReturn(mockLocation);
        Mockito.when(sensorUnitOfMeasureService.findByNameOrCreate("bar")).thenReturn(mockUnit);

        Sensor sensor = mapperUtil.toEntity(dto);

        Assertions.assertEquals("Barometer", sensor.getName());
        Assertions.assertEquals("ac-23", sensor.getModel());
        Assertions.assertEquals(22, sensor.getRangeFrom());
        Assertions.assertEquals(45, sensor.getRangeTo());
        Assertions.assertEquals("Indoor sensor", sensor.getDescription());
        Assertions.assertEquals("Pressure", sensor.getType().getName());
        Assertions.assertEquals("kitchen", sensor.getLocation().getName());
        Assertions.assertEquals("bar", sensor.getUnit().getName());
    }

    @Test
    void toEntitySuccessOptionalFieldsNull() {
        SensorDto dto = new SensorDto(
                "Thermometer",
                "th-01",
                new RangeDto(0, 100),
                "Temperature",
                "",
                "",
                ""
        );

        SensorType mockType = new SensorType();
        mockType.setId(2L);
        mockType.setName("Temperature");

        Mockito.when(sensorTypeService.findByNameOrCreate("Temperature")).thenReturn(mockType);

        Sensor sensor = mapperUtil.toEntity(dto);

        Assertions.assertEquals("Thermometer", sensor.getName());
        Assertions.assertNull(sensor.getUnit());
        Assertions.assertNull(sensor.getLocation());
        Assertions.assertNull(sensor.getDescription());
        Assertions.assertEquals("Temperature", sensor.getType().getName());
    }

    @Test
    void toEntityShouldThrowWhenTypeNotFound() {
        SensorDto dto = new SensorDto(
                "UnknownSensor",
                "x1",
                new RangeDto(1, 10),
                "UnknownType",
                "bar",
                "kitchen",
                "desc"
        );

        Mockito.when(sensorTypeService.findByNameOrCreate("UnknownType"))
                .thenThrow(new IllegalArgumentException("Type not found"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> mapperUtil.toEntity(dto));
    }

    @Test
    void toDtoSuccess() {
        SensorType type = new SensorType();
        type.setId(1L);
        type.setName("Pressure");

        SensorUnitOfMeasure unit = new SensorUnitOfMeasure();
        unit.setId(1L);
        unit.setName("bar");

        SensorLocation location = new SensorLocation();
        location.setId(1L);
        location.setName("kitchen");

        Sensor sensor = Sensor.builder()
                .name("Barometer")
                .model("ac-23")
                .rangeFrom(22)
                .rangeTo(45)
                .description("A barometer")
                .type(type)
                .unit(unit)
                .location(location)
                .build();

        SensorDto dto = mapperUtil.toDto(sensor);

        Assertions.assertEquals("Barometer", dto.name());
        Assertions.assertEquals("ac-23", dto.model());
        Assertions.assertEquals(22, dto.range().from());
        Assertions.assertEquals(45, dto.range().to());
        Assertions.assertEquals("Pressure", dto.type());
        Assertions.assertEquals("bar", dto.unit());
        Assertions.assertEquals("kitchen", dto.location());
        Assertions.assertEquals("A barometer", dto.description());
    }
}