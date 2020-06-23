package io.github.makbn.jlmap.model;

import lombok.*;

/**
 * A class for drawing circle overlays on a map
 * by: Mehdi Akbarian Rastaghi (@makbn)
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class JLCircle extends JLObject<JLCircle> {
    /**
     * id of object! this is an internal id for JLMap Application and not related to Leaflet!
     */
    protected int id;
    /**
     * Radius of the circle, in meters.
     */
    private double radius;
    /**
     * Coordinates of the JLMarker on the map
     */
    private JLLatLng latLng;
    /**
     * theming options for JLCircle. all options are not available!
     */
    private JLOptions options;
}
