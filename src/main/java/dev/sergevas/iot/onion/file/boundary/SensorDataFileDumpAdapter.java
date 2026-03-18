package dev.sergevas.iot.onion.file.boundary;


import dev.sergevas.iot.onion.OnionWebException;
import dev.sergevas.iot.onion.shared.entity.SensorReadings;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@ApplicationScoped
public class SensorDataFileDumpAdapter {

    public static final String IMAGE_EXT = "jpeg";

    @ConfigProperty(name = "onion-app.storage.path")
    String storagePath;

    public void writeDataToFile(SensorReadings readings, byte[] image) {
        Log.infof("Enter writeDataToFile with readings: %s", readings);
        if (isNull(readings) && isNull(image)) {
            Log.warn("No sensor data or image provided, skipping file dump");
            return;
        }
        var currentDate = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());
        var currentTime = DateTimeFormatter.ofPattern("HHmmss").format(LocalTime.now());
        if (nonNull(readings)) {
            var dataFilePath = Path.of(storagePath, "data", currentDate, currentTime + ".json");
            try {
                Files.createDirectories(dataFilePath.getParent());
            } catch (IOException e) {
                throw new OnionWebException("Failed to create directory for data", e);
            }
            try (var jsonGenerator = Json.createGenerator(Files.newOutputStream(dataFilePath))) {
                Files.createDirectories(dataFilePath.getParent());
                jsonGenerator.writeStartObject();
                jsonGenerator.write("temperature", readings.temperature());
                jsonGenerator.write("humidity", readings.humidity());
                jsonGenerator.write("pressure", readings.pressure());
                jsonGenerator.write("light", readings.light());
                jsonGenerator.write("timestamp", readings.timestamp().toString());
                jsonGenerator.write("cameraMode", readings.cameraMode().toString());
                jsonGenerator.writeEnd();
                Log.infof("Successfully wrote sensor data to file: %s", dataFilePath);
            } catch (IOException e) {
                throw new OnionWebException("Failed to create directory for data dump", e);
            }
        } else {
            Log.warn("No sensor data provided, skipping data file dump");
        }
        if (nonNull(image)) {
            var imageFilePath = Path.of(storagePath, "images", currentDate, currentTime + "." + IMAGE_EXT);
            try {
                Files.createDirectories(imageFilePath.getParent());
            } catch (IOException e) {
                throw new OnionWebException("Failed to create directory for image dump", e);
            }
            try (var imageOutputStream = Files.newOutputStream(imageFilePath)) {
                imageOutputStream.write(image);
                Log.infof("Successfully wrote image to file: %s", imageFilePath);
            } catch (IOException e) {
                throw new OnionWebException("Failed to create image dump", e);
            }
        } else {
            Log.warn("No image provided, skipping data file dump");
        }
    }
}
