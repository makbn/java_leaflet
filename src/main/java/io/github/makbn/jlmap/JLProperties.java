package io.github.makbn.jlmap;

import io.github.makbn.jlmap.model.JLMapOption;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

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
        public static final MapType STADIA_ALIDADE_SMOOTH_LIGHT = new MapType("Stadia.AlidadeSmooth");
        public static final MapType STADIA_ALIDADE_SMOOTH_DARK = new MapType("Stadia.AlidadeSmoothDark");
        public static final MapType STADIA_OSM_BRIGHT = new MapType("Stadia.OSMBright");
        public static final MapType STADIA_WATERCOLOR = new MapType("Stadia.StamenWatercolor");
        public static final MapType STADIA_STAMEN_TONER_LITE = new MapType("Stadia.StamenTonerLite");
        public static final MapType STADIA_STAMEN_TERRAIN = new MapType("Stadia.StamenTerrain");

        public static final MapType OPNV_KARTE = new MapType("OPNVKarte");
        public static final MapType THUNDERFOREST_CYCLE = new MapType("Thunderforest.OpenCycleMap");
        public static final MapType THUNDERFOREST_TRANSPORT = new MapType("Thunderforest.Transport");
        public static final MapType THUNDERFOREST_SPINAL = new MapType("Thunderforest.SpinalMap");

        public static final MapType JAWG_TERRAIN = new MapType("Jawg.Terrain");
        public static final MapType JAWG_LIGHT = new MapType("Jawg.Light");
        public static final MapType JAWG_DARK = new MapType("Jawg.Dark");
        public static final MapType JAWG_MATRIX = new MapType("Jawg.Matrix");

        public static MapType getDefault() {
            return OSM_MAPNIK;
        }
    }
}
