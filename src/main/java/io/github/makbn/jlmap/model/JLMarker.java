package io.github.makbn.jlmap.model;


import io.github.makbn.jlmap.listener.OnJLObjectActionListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * JLMarker is used to display clickable/draggable icons on the map!
 *
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class JLMarker extends JLObject<JLMarker> {
    /**
     * id of object! this is an internal id for JLMap Application and not
     * related to Leaflet!
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
        if (params != null && params.length > 0
                && String.valueOf(params[0]).equals(
                OnJLObjectActionListener.Action.MOVE_END.getJsEventName())
                && params[1] != null) {
            latLng = (JLLatLng) params[1];
        }
    }
}
