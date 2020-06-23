package io.github.makbn.jlmap.model;


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
public class JLMarker extends JLObject<JLMarker> {
    /**
     * id of object! this is an internal id for JLMap Application and not related to Leaflet!
     */
    protected int id;
    /**
     * optional text for showing on created JLMarker tooltip.
     */
    private String text;
    /**
     * Coordinates of the JLMarker on the map
     */
    private JLLatLng latLng;


    @Override
    public void update(Object... params) {
        super.update(params);
        if (params != null && params.length > 0) {
            if (String.valueOf(params[0]).equals("moveend") && params[1] != null) {
                latLng = (JLLatLng) params[1];
            }
        }
    }
}
