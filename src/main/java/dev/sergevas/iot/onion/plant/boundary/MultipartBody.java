package dev.sergevas.iot.onion.plant.boundary;

import dev.sergevas.iot.onion.shared.entity.SensorReadings;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public class MultipartBody {

    @RestForm("data")
    @PartType(MediaType.APPLICATION_JSON)
    public SensorReadings readings;

    @RestForm("photo")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public FileUpload file;
}
