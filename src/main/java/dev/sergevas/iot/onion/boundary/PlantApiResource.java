package dev.sergevas.iot.onion.boundary;

import dev.sergevas.iot.onion.control.PlantState;
import dev.sergevas.iot.onion.entity.SensorReadings;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.nio.file.Files;

@Path("plant/api")
public class PlantApiResource {

    @Inject
    PlantState state;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response receiveData(
            @RestForm("data") SensorReadings readings, // Quarkus maps JSON field to Object
            @RestForm("photo") FileUpload file) throws IOException {

        byte[] imageBytes = Files.readAllBytes(file.filePath());
        state.update(readings, imageBytes);

        return Response.ok().build();
    }
}
