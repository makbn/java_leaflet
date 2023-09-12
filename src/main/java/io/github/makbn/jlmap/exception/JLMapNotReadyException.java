package io.github.makbn.jlmap.exception;

import lombok.Builder;

/**
 * JLMap Exception which is thrown when changing the map before it gets ready!
 * Leaflet map gets fully ready after the {@link javafx.concurrent.Worker.State}
 * of {@link javafx.scene.web.WebEngine} changes to
 * {@link javafx.concurrent.Worker.State#SUCCEEDED}
 *
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
public class JLMapNotReadyException extends JLException {
    private static final String MAP_IS_NOT_READY_YET = "Map is not ready yet!";

    @Builder
    public JLMapNotReadyException() {
        super(MAP_IS_NOT_READY_YET);
    }
}
