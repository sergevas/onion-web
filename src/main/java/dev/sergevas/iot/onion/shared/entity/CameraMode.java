package dev.sergevas.iot.onion.shared.entity;

public enum CameraMode {
    NORM("дневное освещение"),
    NIGHT("ночное видение"),
    UNDEFINED("Не определён");

    private final String description;

    CameraMode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
