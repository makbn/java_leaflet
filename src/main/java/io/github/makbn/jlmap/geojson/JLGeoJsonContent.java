package io.github.makbn.jlmap.geojson;

import io.github.makbn.jlmap.exception.JLGeoJsonParserException;
import lombok.Builder;


@Builder
public class JLGeoJsonContent extends JLGeoJsonSource {

    private final String content;

    public JLGeoJsonContent(String content) {
        this.content = content;
    }

    @Override
    protected String getAddress() {
        return null;
    }

    @Override
    protected void load() {
        if (content == null || content.isEmpty())
            throw JLGeoJsonParserException.builder()
                    .message("json is empty!")
                    .build();
    }

    @Override
    public JLGeoJsonObject getObject() {
        return null;
    }
}
