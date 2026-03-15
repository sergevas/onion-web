package dev.sergevas.iot.onion;

public class OnionWebException extends RuntimeException {

    public OnionWebException(String message) {
        super(message);
    }

    public OnionWebException(String message, Throwable cause) {
        super(message, cause);
    }
}
