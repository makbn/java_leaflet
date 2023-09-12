package io.github.makbn.jlmap;

import io.github.makbn.jlmap.model.JLMapOption;

import java.util.Collections;
import java.util.Set;

/**
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
public class JLProperties {
    public static final int INIT_MIN_WIDTH = 1024;
    public static final int INIT_MIN_HEIGHT = 576;
    public static final int EARTH_RADIUS = 6367;
    public static final int DEFAULT_CIRCLE_RADIUS = 200;
    public static final int DEFAULT_CIRCLE_MARKER_RADIUS = 10;
    public static final int INIT_MIN_WIDTH_STAGE = INIT_MIN_WIDTH;
    public static final int INIT_MIN_HEIGHT_STAGE = INIT_MIN_HEIGHT;
    public static final double START_ANIMATION_RADIUS = 10;

    public record MapType(String name, Set<JLMapOption.Parameter> parameters) {

        public MapType(String name) {
            this(name, Collections.emptySet());
        }

        public static final MapType OSM_MAPNIK = new MapType("OpenStreetMap.Mapnik");
        public static final MapType OSM_HOT = new MapType("OpenStreetMap.HOT");
        public static final MapType OPEN_TOPO = new MapType("OpenTopoMap");

        public static MapType getDefault() {
            return OSM_MAPNIK;
        }
    }
}
