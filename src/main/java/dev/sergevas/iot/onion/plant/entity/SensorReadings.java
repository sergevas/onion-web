package dev.sergevas.iot.onion.plant.entity;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public record SensorReadings(Double temperature,
                             Double humidity,
                             Double pressure,
                             Double light,
                             Instant timestamp,
                             CameraMode cameraMode
) {
    public SensorReadings() {
        this(null, null, null, null, null, CameraMode.UNDEFINED);
    }

    public String getFormattedDateTime(Instant timestamp) {
        return Optional.ofNullable(timestamp)
                .map(t -> DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss", Locale.of("ru", "RU"))
                        .format(t.atOffset(ZoneOffset.of("+03:00"))))
                .orElse(null);
    }

    public String getReadingsDateTime() {
        return getFormattedDateTime(timestamp);
    }

    public String getCurrentDateTime() {
        return getFormattedDateTime(Instant.now());
    }
}
