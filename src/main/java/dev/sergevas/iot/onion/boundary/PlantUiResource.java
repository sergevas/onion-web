package dev.sergevas.iot.onion.boundary;

import dev.sergevas.iot.onion.control.PlantState;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("plant/ui")
public class PlantUiResource {

    @Inject
    Template plantUI;
    @Inject
    PlantState state;

    @GET
    @Path("/updates")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getUpdates() {
        return plantUI.fragment("sync_content")
                .data("sensorReadings", state.getReadings());
    }

    // 2. Returns the actual Image bytes to the <img> tag
    @GET
    @Path("/latest-photo.jpg")
    @Produces("image/jpeg")
    public byte[] getLatestPhoto() {
        return state.getPhoto();
    }
}
