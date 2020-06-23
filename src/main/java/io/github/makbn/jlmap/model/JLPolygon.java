package io.github.makbn.jlmap.model;

import lombok.*;

/**
 * A class for drawing polygon overlays on the map.
 * Note that points you pass when creating a polygon shouldn't
 * have an additional last point equal to the first one.
 * by: Mehdi Akbarian Rastaghi (@makbn)
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class JLPolygon extends JLObject<JLPolygon> {
    /**
     * id of JLPolygon! this is an internal id for JLMap Application and not related to Leaflet!
     */
    private int id;
    /**
     * theming options for JLMultiPolyline. all options are not available!
     */
    private JLOptions options;

    /**
     * The arrays of latlngs, with the first array representing the outer shape and the other arrays representing
     * holes in the outer shape. Additionally, you can pass a multi-dimensional array to represent a MultiPolygon shape.
     */
    private JLLatLng[][][] vertices;
}
