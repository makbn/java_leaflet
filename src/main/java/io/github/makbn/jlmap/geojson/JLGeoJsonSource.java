package io.github.makbn.jlmap.geojson;

public abstract class JLGeoJsonSource {

    protected abstract String getAddress();

    protected abstract void load();

    public abstract JLGeoJsonObject getObject();

}
