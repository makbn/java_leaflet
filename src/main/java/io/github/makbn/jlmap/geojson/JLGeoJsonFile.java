package io.github.makbn.jlmap.geojson;

import lombok.Builder;

import java.io.File;

public class JLGeoJsonFile extends JLGeoJsonSource {

    private final File file;


    @Builder
    public JLGeoJsonFile(File file) {
        this.file = file;
    }

    @Override
    protected String getAddress() {
        return file.getAbsolutePath();
    }

    @Override
    protected void load() {

    }

    @Override
    public JLGeoJsonObject getObject() {
        return null;
    }
}
