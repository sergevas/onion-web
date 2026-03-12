package dev.sergevas.iot.onion.entity;

public record SensorReadings( String moscowTime,
                              double temperature,
                              int humidity,
                              int pressure,
                              int light,
                              long timestamp
) {
    public SensorReadings() {
        this("12.03.2026 19:16:00", 0.0, 0, 0, 0, System.currentTimeMillis());
    }
}
