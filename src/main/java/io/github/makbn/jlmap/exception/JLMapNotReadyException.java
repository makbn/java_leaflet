package io.github.makbn.jlmap.exception;

import lombok.Builder;

/**
 * JLMap Exception which is thrown when changing the map before it gets ready!
 * Leaflet map gets fully ready after the {{@link javafx.concurrent.Worker.State}} of {{@link javafx.scene.web.WebEngine}}
 * changes to {{@link javafx.concurrent.Worker.State#SUCCEEDED}}
 * by: Mehdi Akbarian Rastaghi (@makbn)
 */
public class JLMapNotReadyException extends JLException{

    @Builder
    public JLMapNotReadyException() {
        super("map is not ready yet!");
    }
}
