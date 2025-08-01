package io.github.makbn.jlmap.exception;

import lombok.Builder;

/**
 * JLMap Exception which is thrown when changing the map before it gets ready!
 *
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
public class JLMapNotReadyException extends JLException {
    private static final String MAP_IS_NOT_READY_YET = "Map is not ready yet!";

    @Builder
    public JLMapNotReadyException() {
        super(MAP_IS_NOT_READY_YET);
    }

    public JLMapNotReadyException(String message) {
        super(message);
    }
}
