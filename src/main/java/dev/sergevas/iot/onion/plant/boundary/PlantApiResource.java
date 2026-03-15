package dev.sergevas.iot.onion.plant.boundary;

import dev.sergevas.iot.onion.plant.control.PlantState;
import dev.sergevas.iot.onion.plant.entity.SensorReadings;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.nio.file.Files;

@Path("plant/api")
public class PlantApiResource {

    @Inject
    PlantState state;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response receiveData(MultipartBody body) throws IOException {
        Log.infof("Received data from plant: %s", body.readings);
        if (body.file != null && body.file.filePath() != null) {
            byte[] imageBytes = Files.readAllBytes(body.file.filePath());
            // Если данные JSON не пришли, создаем пустые, чтобы не было NullPointerException
            SensorReadings data = body.readings != null ? body.readings : new SensorReadings();

            state.update(data, imageBytes);
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
