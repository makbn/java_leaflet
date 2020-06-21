package io.github.makbn.jlmap.model;

import lombok.*;

/**
 * Used to open popups in certain places of the map.
 * by: Mehdi Akbarian Rastaghi (@makbn)
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class JLPopup {
    /** id of JLPopup! this is an internal id for JLMap Application and not related to Leaflet! */
    private int id;
    /** Content of the popup.*/
    private String text;
    /** Coordinates of the popup on the map. */
    private JLLatLng latLng;
    /** Theming options for JLPopup. all options are not available! */
    private JLOptions options;
}
