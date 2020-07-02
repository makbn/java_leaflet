package io.github.makbn.jlmap.geojson;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class JLFeature {
    @Getter
    private String type;
    private HashMap<String, Object> properties;
    private JLGeometry geometry;

    private String rawGeometry;
    private String rawProperties;
    private boolean lazy = true;

    public JLFeature() {
    }

    public JLFeature(boolean lazy) {
        this.lazy = lazy;
        if (!lazy) {
            loadProperties();
            loadGeometry();
        }
    }

    public HashMap<String, Object> getProperties() {
        return properties;
    }

    private void loadProperties() {
    }

    public JLGeometry getGeometry() {
        if (geometry == null)
            loadGeometry();
        return geometry;
    }

    private void loadGeometry() {

    }


    @Getter
    @Setter
    public static class JLProperty<T> {
        private String key;
        private T value;
    }

    public static class JLGeometry {
        @Getter
        private String type;
        private Object coordinates;

    }

    public static class JLGeometry2D extends JLGeometry {
        @Getter
        private String type;
        private Double[][] coordinates;

    }

    public static class JLGeometry3D extends JLGeometry {
        @Getter
        private String type;
        private Double[][][] coordinates;

    }
}
