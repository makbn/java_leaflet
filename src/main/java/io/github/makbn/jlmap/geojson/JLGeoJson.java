package io.github.makbn.jlmap.geojson;


public class JLGeoJson {


    private static JLGeoJsonURL.JLGeoJsonURLBuilder fromUrl() {
        return JLGeoJsonURL.builder();
    }

    private static JLGeoJsonFile.JLGeoJsonFileBuilder fromFile() {
        return JLGeoJsonFile.builder();
    }

    private static JLGeoJsonContent.JLGeoJsonContentBuilder fromContent() {
        return JLGeoJson.fromContent();
    }
}
