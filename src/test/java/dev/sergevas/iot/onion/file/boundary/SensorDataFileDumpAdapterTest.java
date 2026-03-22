package dev.sergevas.iot.onion.file.boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sergevas.iot.onion.renderer.ImageOverlayService;
import dev.sergevas.iot.onion.shared.entity.SensorReadings;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static dev.sergevas.iot.onion.file.boundary.SensorDataFileDumpAdapter.IMAGE_EXT;

@QuarkusTest
class SensorDataFileDumpAdapterTest {

    @ConfigProperty(name = "onion-app.raw-to-process.path")
    String rawToProcessPath;

    @Inject
    ImageOverlayService imageOverlayService;

    @Inject
    SensorDataFileDumpAdapter sensorDataFileDumpAdapter;

    @Test
    void givenSensorDataAndImage_whenWrite_thenShouldDumpIntoFileSuccessfully() throws Exception {
        var mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        var rawToProcessDataDir = Paths.get(rawToProcessPath, "data");
        Files.walk(rawToProcessDataDir, 1)
                .filter(path -> !path.equals(rawToProcessDataDir))
                .forEach(dateDirPath -> {
                    try {
                        Files.walk(dateDirPath, 1)
                                .filter(path -> !path.equals(dateDirPath))
                                .sorted()
                                .forEach(dataFilePath -> {
                                    try {
                                        var sensorReadingsSerialized = Files.readString(dataFilePath);
                                        var dateString = dateDirPath.getName(dateDirPath.getNameCount() - 1).toString();
                                        var timeString = dataFilePath.getName(dataFilePath.getNameCount() - 1).toString().substring(0, 6);
                                        var imagePath = Paths.get(rawToProcessPath, "images",
                                                dateString,
                                                timeString + ".jpeg");
                                        var sensorReadings = mapper.readValue(sensorReadingsSerialized, SensorReadings.class);
                                        var image = imageOverlayService.render(Files.readAllBytes(imagePath), IMAGE_EXT,
                                                sensorReadings.toStringFormatted());
                                        sensorDataFileDumpAdapter.writeDataToFile(sensorReadings, image,
                                                LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyyMMdd")),
                                                LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HHmmss")));
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}