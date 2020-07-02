package io.github.makbn.jlmap;

/**
 * by: Mehdi Akbarian Rastaghi (@makbn)
 */
public class JLProperties {

    public final static int INIT_MIN_WIDTH = 1024;
    public final static int INIT_MIN_HEIGHT = 576;
    public final static int NORMAL_MARGIN = 0;
    public final static int EARTH_RADIUS = 6371;
    public final static int DEFAULT_CIRCLE_RADIUS = 200;
    public final static int DEFAULT_CIRCLE_MARKER_RADIUS = 10;

    public final static int INIT_MIN_WIDTH_STAGE = INIT_MIN_WIDTH;
    public final static int INIT_MIN_HEIGHT_STAGE = INIT_MIN_HEIGHT;
    public static final double START_ANIMATION_RADIUS = 10;


    public enum MapType{
        LIGHT("mapbox/light-v10"), DARK("mapbox/dark-v10"), OUTDOOR("mapbox/outdoors-v11"),
        SATELLITE("mapbox/satellite-v9"), SATELLITE_STREET("mapbox/satellite-streets-v11"),
        STREET("mapbox/streets-v11");

        private final String value;

        MapType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
