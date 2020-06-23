package io.github.makbn.jlmap.model;

import lombok.*;

/**
 * A circle of a fixed size with radius specified in pixels.
 * by: Mehdi Akbarian Rastaghi (@makbn)
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class JLCircleMarker extends JLObject<JLCircleMarker> {
    /**
     * id of object! this is an internal id for JLMap Application and not related to Leaflet!
     */
    protected int id;
    /**
     * Radius of the circle, in meters.
     */
    private double radius;
    /**
     * Coordinates of the JLCircleMarker on the map
     */
    private JLLatLng latLng;
    /**
     * theming options for JLCircleMarker. all options are not available!
     */
    private JLOptions options;
}
