package by.svistunovvv.agsr_test.service;

import java.util.Optional;

public interface MinorSensorTablesServiceInterface<T> {
    T findByNameOrCreate(String name);
}
