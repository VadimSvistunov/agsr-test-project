package by.svistunovvv.agsr_test.model.dto;

public record SensorDto(
        String name,
        String model,
        RangeDto range,
        String type,
        String unit,
        String location,
        String description
) {
}
