package dev.sergevas.iot.onion.shared.entity;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
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

    public Long getPressureMmhg() {
        return Optional.ofNullable(pressure).map(p -> Math.round(p / 133)).orElse(null);
    }

    public String getTemperatureFormatted() {
        return String.format("%.2f", temperature);
    }

    public String getHumidityFormatted() {
        return String.format("%.2f", humidity);
    }

    public String getLightFormatted() {
        return String.format("%.2f", light);
    }

    public List<String> toStringFormatted() {
        return Arrays.asList(
                "Луковка онлайн: " + (timestamp != null ? getReadingsDateTime() : "Нет данных"),
                "Температура: " + (temperature != null ? String.format("%.2f °C", temperature) : "Нет данных"),
                "Влажность: " + (humidity != null ? String.format("%.2f %%", humidity) : "Нет данных"),
                "Давление: " + (pressure != null ? String.format("%d мм рт.ст.", getPressureMmhg()) : "Нет данных"),
                "Количество света: " + (light != null ? String.format("%.2f лк", light) : "Нет данных"),
                "Режим камеры: " + (cameraMode != null ? cameraMode.getDescription() : "Нет данных")
        );
    }
}
