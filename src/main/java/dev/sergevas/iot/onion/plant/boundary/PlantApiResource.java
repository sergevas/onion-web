package dev.sergevas.iot.onion.plant.boundary;

import dev.sergevas.iot.onion.file.boundary.SensorDataFileDumpAdapter;
import dev.sergevas.iot.onion.plant.control.PlantState;
import dev.sergevas.iot.onion.renderer.ImageOverlayService;
import dev.sergevas.iot.onion.shared.entity.SensorReadings;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.multipart.FilePart;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import static dev.sergevas.iot.onion.file.boundary.SensorDataFileDumpAdapter.IMAGE_EXT;

@Path("plant/api")
public class PlantApiResource {

    @Inject
    PlantState state;

    @Inject
    ImageOverlayService imageOverlayService;

    @Inject
    SensorDataFileDumpAdapter sensorDataFileDumpAdapter;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response receiveData(MultipartBody body) {
        Log.infof("Received data from plant: %s", body.readings);
        try {
            final var sensorReadings = Optional.ofNullable(body.readings).orElse(new SensorReadings());
            var imageWithOverlay = Optional.ofNullable(body.file)
                    .map(FilePart::filePath)
                    .map(p -> {
                        try {
                            return Files.readAllBytes(p);
                        } catch (IOException ioe) {
                            Log.error("Unable to read image bytes", ioe);
                            return null;
                        }
                    })
                    .map(img -> imageOverlayService.render(img, IMAGE_EXT, sensorReadings.toStringFormatted()))
                    .orElse(new byte[0]);
            sensorDataFileDumpAdapter.writeDataToFile(sensorReadings, imageWithOverlay);
            state.update(sensorReadings, imageWithOverlay);
            return Response.ok().build();
        } catch (Exception e) {
            Log.error("Unable to process request", e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
