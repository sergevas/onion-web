package dev.sergevas.iot.onion.plant.entity;

public enum CameraMode {
    NORM("Дневное освещение"),
    NIGHT("Ночной режим"),
    UNDEFINED("Не определён");

    private final String description;

    CameraMode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
