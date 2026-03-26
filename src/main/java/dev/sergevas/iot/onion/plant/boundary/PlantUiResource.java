package dev.sergevas.iot.onion.plant.boundary;

import dev.sergevas.iot.onion.file.boundary.VideoFileAdapter;
import dev.sergevas.iot.onion.plant.control.PlantState;
import io.quarkus.logging.Log;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.Response.status;

@Path("")
public class PlantUiResource {

    @Inject
    Template plantUI;
    @Inject
    PlantState state;
    @Inject
    VideoFileAdapter videoFileAdapter;

    @GET
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

    @GET
    @Path("/latest-video.mp4")
    @Produces("video/mp4")
    public Response getLatestVideo() {
        return videoFileAdapter.getVideo()
                .map(content -> Response.ok(content)
                        .header("Content-Disposition", "inline")
                        .build())
                .orElseGet(() -> status(Response.Status.NOT_FOUND).build());
    }
}
