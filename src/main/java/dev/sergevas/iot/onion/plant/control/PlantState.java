package dev.sergevas.iot.onion.plant.control;

import dev.sergevas.iot.onion.plant.entity.SensorReadings;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
public class PlantState {
    private final AtomicReference<SensorReadings> currentReadings = new AtomicReference<>(new SensorReadings());
    private final AtomicReference<byte[]> lastPhoto = new AtomicReference<>(new byte[0]);

    public void update(SensorReadings readings, byte[] photo) {
        this.currentReadings.set(readings);
        this.lastPhoto.set(photo);
    }

    public SensorReadings getReadings() {
        return currentReadings.get();
    }

    public byte[] getPhoto() {
        return lastPhoto.get();
    }
}
