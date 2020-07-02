package io.github.makbn.jlmap.geojson;

import lombok.Builder;


public class JLGeoJsonURL extends JLGeoJsonSource {
    private final String url;


    @Builder
    public JLGeoJsonURL(String url) {
        this.url = url;
    }

    @Override
    protected String getAddress() {
        return this.url;
    }

    @Override
    protected void load() {

    }

    @Override
    public JLGeoJsonObject getObject() {
        return null;
    }
}
