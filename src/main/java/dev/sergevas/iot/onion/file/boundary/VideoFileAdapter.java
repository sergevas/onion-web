package dev.sergevas.iot.onion.file.boundary;

import dev.sergevas.iot.onion.OnionWebException;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.Optional;

@ApplicationScoped
public class VideoFileAdapter {

    @ConfigProperty(name = "onion-app.video.storage.path")
    String videoStoragePath;

    public Optional<byte[]> getVideo() {
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.mp4");
        try {
            return Files.find(Paths.get(videoStoragePath), 1, (path, attr) -> attr.isRegularFile() && matcher.matches(path))
                    .findFirst()
                    .map(p -> {
                        try {
                            return Files.readAllBytes(p);
                        } catch (IOException e) {
                            throw new OnionWebException("Unable to read video file from [%s]".formatted(p), e);
                        }
                    });

        } catch (IOException e) {
            throw new OnionWebException("Unable to get video file from [%s]".formatted(videoStoragePath), e);
        }
    }
}
