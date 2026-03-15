package dev.sergevas.iot.onion.plant.boundary;

import dev.sergevas.iot.onion.plant.control.PlantState;
import io.quarkus.logging.Log;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/plant/ui")
public class PlantUiResource {

    @Inject
    Template plantUI;
    @Inject
    PlantState state;

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance fullPage() {
        Log.infof("Rendering full page with sensor readings: %s", state.getReadings());
        return plantUI.data("sensorReadings", state.getReadings());
    }


    @GET
    @Path("/updates")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getUpdates() {
        Log.info("Rendering updates fragment with sensor readings");
        // Uses getFragment() to render ONLY the code inside {#fragment id=sync_content}
        return plantUI.getFragment("sync_content")
                .data("sensorReadings", state.getReadings());
    }

    @GET
    @Path("/latest-photo.jpg")
    @Produces("image/jpeg")
    public byte[] getLatestPhoto() {
        Log.info("Serving latest photo");
        return state.getPhoto();
    }

    // --- IOT DEVICE ENDPOINT ---

    /*@POST
    @Path("/api/update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response receiveData(
            @RestForm("data") SensorReadings data,
            @RestForm("photo") FileUpload file) throws IOException {

        if (file != null && file.filePath() != null) {
            byte[] imageBytes = Files.readAllBytes(file.filePath());
            state.update(data, imageBytes);
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }*/
}
