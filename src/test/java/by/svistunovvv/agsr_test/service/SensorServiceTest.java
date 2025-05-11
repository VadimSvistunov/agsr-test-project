package by.svistunovvv.agsr_test.service;

import by.svistunovvv.agsr_test.exception.SaveSensorException;
import by.svistunovvv.agsr_test.exception.SensorNotFoundException;
import by.svistunovvv.agsr_test.model.sensor.Sensor;
import by.svistunovvv.agsr_test.model.sensor.SensorLocation;
import by.svistunovvv.agsr_test.model.sensor.SensorType;
import by.svistunovvv.agsr_test.model.sensor.SensorUnitOfMeasure;
import by.svistunovvv.agsr_test.service.impl.SensorLocationService;
import by.svistunovvv.agsr_test.service.impl.SensorTypeService;
import by.svistunovvv.agsr_test.service.impl.SensorUnitOfMeasureService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@Testcontainers
@TestPropertySource("/application-test.yml")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SensorServiceTest {
    @LocalServerPort
    private Integer port;

    @Autowired
    private SensorService service;
    @Autowired
    private SensorLocationService sensorLocationService;
    @Autowired
    private SensorTypeService sensorTypeService;
    @Autowired
    private SensorUnitOfMeasureService sensorUnitOfMeasureService;
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    ).withInitScript("data.sql");


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void saveCorrectSensorObjectTest() {
        SensorType type = sensorTypeService.findByNameOrCreate("Pressure");
        SensorLocation location = sensorLocationService.findByNameOrCreate("kitchen");
        SensorUnitOfMeasure unitOfMeasure = sensorUnitOfMeasureService.findByNameOrCreate("bar");
        Sensor sensor = Sensor.builder()
                .name("name")
                .model("model")
                .rangeFrom(22)
                .rangeTo(11)
                .description("description")
                .location(location)
                .unit(unitOfMeasure)
                .type(type)
                .build();

        sensor = service.save(sensor);

        Assertions.assertNotNull(sensor.getId());
    }

    @Test
    void findAllSensorEntityTest() {
        List<Sensor> sensors = service.findAll();

        Assertions.assertEquals(7, sensors.size());
    }

    @Test
    void saveNullFieldsInSensorObjectTest() {
        SensorType type = sensorTypeService.findByNameOrCreate("Pressure");
        SensorLocation location = sensorLocationService.findByNameOrCreate("kitchen");
        SensorUnitOfMeasure unitOfMeasure = sensorUnitOfMeasureService.findByNameOrCreate("bar");
        Sensor sensor = Sensor.builder()
                .name("name")
                .model("model")
                .rangeFrom(22)
                .rangeTo(11)
                .location(location)
                .type(type)
                .build();

        sensor = service.save(sensor);

        Assertions.assertNull(sensor.getDescription());
        Assertions.assertNull(sensor.getUnit());
    }

    @Test
    void saveInvalidSensorObjectTest() {
        SensorType type = sensorTypeService.findByNameOrCreate("Pressure");
        SensorLocation location = sensorLocationService.findByNameOrCreate("kitchen");
        SensorUnitOfMeasure unitOfMeasure = sensorUnitOfMeasureService.findByNameOrCreate("bar");
        Sensor sensor = Sensor.builder()
                .name("name")
                .model("model")
                .rangeFrom(-1)
                .rangeTo(11)
                .description("description")
                .location(location)
                .unit(unitOfMeasure)
                .type(type)
                .build();

        Assertions.assertThrows(SaveSensorException.class, () -> service.save(sensor));
    }

    @Test
    void deleteSensorEntityTest() {
        SensorType type = sensorTypeService.findByNameOrCreate("Pressure");
        SensorLocation location = sensorLocationService.findByNameOrCreate("kitchen");
        SensorUnitOfMeasure unitOfMeasure = sensorUnitOfMeasureService.findByNameOrCreate("bar");
        Sensor sensor = Sensor.builder()
                .name("name")
                .model("model")
                .rangeFrom(22)
                .rangeTo(11)
                .location(location)
                .type(type)
                .build();
        sensor = service.save(sensor);
        long newId = sensor.getId();

        service.deleteById(newId);

        Assertions.assertThrows(SensorNotFoundException.class, () -> service.findById(newId));
    }

    @Test
    void updateSensorEntityTest() {
        Sensor sensorOriginal = service.findById(2);
        sensorOriginal.setName("BarometerNew");
        service.update(sensorOriginal);

        Sensor sensorNew = service.findById(2);
        Assertions.assertEquals("BarometerNew", sensorNew.getName());
    }

    @Test
    void findByWrongIdTest() {
        Assertions.assertThrows(SensorNotFoundException.class, () -> service.findById(6));
    }

    @Test
    void findByCorrectIdTest() {
        Sensor sensor = service.findById(2);

        Assertions.assertEquals(2, sensor.getId());
    }

    @Test
    void findByNameContainsTest() {
        List<Sensor> sensors = service.findByNameContains("rmomet");

        Assertions.assertEquals(3, sensors.size());
    }

    @Test
    void findByModelContainsTest() {
        List<Sensor> sensors = service.findByModelContains("temp");

        Assertions.assertEquals(3, sensors.size());
    }

}