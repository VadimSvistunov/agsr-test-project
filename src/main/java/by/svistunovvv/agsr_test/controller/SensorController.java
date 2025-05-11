package by.svistunovvv.agsr_test.controller;

import by.svistunovvv.agsr_test.model.dto.SensorDto;
import by.svistunovvv.agsr_test.model.sensor.Sensor;
import by.svistunovvv.agsr_test.service.SensorService;
import by.svistunovvv.agsr_test.util.mapper.SensorMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RequestMapping("/api/v1/sensor")
@RestController
public class SensorController {
    private final SensorService service;
    private final SensorMapper sensorMapper;

    @PostMapping
    public ResponseEntity<Long> save(@RequestBody SensorDto sensorDto) {
        Sensor sensor = service.save(sensorMapper.toEntity(sensorDto));
        return ResponseEntity.ok(sensor.getId());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Long> update(@PathVariable Long id, @RequestBody SensorDto sensorDto) {
        Sensor existingSensor = service.findById(id);
        Sensor updatedSensor = sensorMapper.updateEntity(sensorDto, existingSensor);
        service.update(updatedSensor);
        return ResponseEntity.ok(updatedSensor.getId());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sensorMapper.toDto(service.findById(id)));
    }

    @GetMapping("/all")
    public List<Sensor> getAll() {
        return service.findAll();
    }

    @GetMapping("/name")
    public List<SensorDto> getNameContains(@RequestParam String name) {
        return sensorMapper.listToDto(service.findByNameContains(name));
    }

    @GetMapping("/model")
    public List<SensorDto> getModelContains(@RequestParam String model) {
        return sensorMapper.listToDto(service.findByModelContains(model));
    }

    @GetMapping("/secret")
    public String secret() {
        return "secret";
    }
}
