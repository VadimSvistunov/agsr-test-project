package by.svistunovvv.agsr_test.service;

import by.svistunovvv.agsr_test.exception.DeleteSensorException;
import by.svistunovvv.agsr_test.exception.SaveSensorException;
import by.svistunovvv.agsr_test.exception.SensorNotFoundException;
import by.svistunovvv.agsr_test.model.sensor.Sensor;
import by.svistunovvv.agsr_test.repository.SensorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class SensorService {
    private SensorRepository repository;

    public Sensor save(Sensor sensor) {
        try {
            repository.save(sensor);
        } catch (Exception e) {
            throw new SaveSensorException("Error while saving Sensor");
        }

        return sensor;
    }

    @Transactional
    public Sensor update(Sensor sensor) {
        return save(sensor);
    }

    public boolean deleteById(long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new DeleteSensorException("Error while deleting Sensor");
        }

        return true;
    }

    public Sensor findById(long id) {
        return repository.findById(id).orElseThrow(() -> new SensorNotFoundException("Error while get sensor by id: " + id));
    }

    public List<Sensor> findAll() {
        List<Sensor> sensors;
        try {
            sensors = repository.findAll();
        } catch (Exception e) {
            throw new SensorNotFoundException("Error while get all sensors");
        }

        return sensors;
    }

    public List<Sensor> findByNameContains (String nameContains) {
        List<Sensor> sensors;
        try {
            sensors = repository.findSensorsByNameContains(nameContains);
        } catch (Exception e) {
            throw new SensorNotFoundException("Error while get sensor by name contains with search string: " + nameContains);
        }

        return sensors;
    }

    public List<Sensor> findByModelContains (String modelContains) {
        List<Sensor> sensors;
        try {
            sensors = repository.findSensorsByModelContains(modelContains);
        } catch (Exception e) {
            throw new SensorNotFoundException("Error while get sensor by model contains with search string: " + modelContains);
        }

        return sensors;
    }
}
