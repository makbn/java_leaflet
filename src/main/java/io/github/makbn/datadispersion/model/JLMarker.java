package io.github.makbn.datadispersion.model;


import lombok.*;

/**
 * JLMarker is used to display clickable/draggable icons on the map!
 * by: Mehdi Akbarian Rastaghi (@makbn)
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class JLMarker implements JLPath{
    /** id of object! this is an internal id for JLMap Application and not related to Leaflet! */
    protected int id;
    /** optional text for showing on created JLMarker tooltip. */
    private String text;
    /** Coordinates of the JLMarker on the map */
    private JLLatLng latLng;
}
