package io.github.makbn.jlmap.exception;

import lombok.Builder;

public class JLGeoJsonParserException extends JLException {

    @Builder
    public JLGeoJsonParserException(String message) {
        super(message);
    }
}
