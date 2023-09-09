package io.github.makbn.jlmap.exception;

import lombok.Builder;

/**
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
public class JLGeoJsonParserException extends JLException {

    @Builder
    public JLGeoJsonParserException(String message) {
        super(message);
    }

    public JLGeoJsonParserException(Throwable cause) {
        super(cause);
    }
}
