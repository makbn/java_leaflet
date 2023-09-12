package io.github.makbn.jlmap.layer;

import io.github.makbn.jlmap.JLMapCallbackHandler;
import io.github.makbn.jlmap.layer.leaflet.LeafletUILayerInt;
import io.github.makbn.jlmap.model.JLLatLng;
import io.github.makbn.jlmap.model.JLMarker;
import io.github.makbn.jlmap.model.JLOptions;
import io.github.makbn.jlmap.model.JLPopup;
import javafx.scene.web.WebEngine;

/**
 * Represents the UI layer on Leaflet map.
 *
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
public class JLUiLayer extends JLLayer implements LeafletUILayerInt {

    public JLUiLayer(WebEngine engine, JLMapCallbackHandler callbackHandler) {
        super(engine, callbackHandler);
    }

    /**
     * Add a {{@link JLMarker}} to the map with given text as content and {{@link JLLatLng}} as position.
     *
     * @param latLng position on the map.
     * @param text   content of the related popup if available!
     * @return the instance of added {{@link JLMarker}} on the map.
     */
    @Override
    public JLMarker addMarker(JLLatLng latLng, String text, boolean draggable) {
        String result = engine.executeScript(String.format("addMarker(%f, %f, '%s', %b)", latLng.getLat(), latLng.getLng(), text, draggable))
                .toString();
        int index = Integer.parseInt(result);
        JLMarker marker = new JLMarker(index, text, latLng);
        callbackHandler.addJLObject(marker);
        return marker;
    }

    /**
     * Remove a {{@link JLMarker}} from the map.
     *
     * @param id of the marker for removing.
     * @return {{@link Boolean#TRUE}} if removed successfully.
     */
    @Override
    public boolean removeMarker(int id) {
        String result = engine.executeScript(String.format("removeMarker(%d)", id)).toString();
        callbackHandler.remove(JLMarker.class, id);
        return Boolean.parseBoolean(result);
    }

    /**
     * Add a {{@link JLPopup}} to the map with given text as content and
     * {@link JLLatLng} as position.
     *
     * @param latLng  position on the map.
     * @param text    content of the popup.
     * @param options see {{@link JLOptions}} for customizing
     * @return the instance of added {{@link JLPopup}} on the map.
     */
    @Override
    public JLPopup addPopup(JLLatLng latLng, String text, JLOptions options) {
        String result = engine.executeScript(String.format("addPopup(%f, %f, \"%s\", %b , %b)", latLng.getLat(), latLng.getLng(), text, options.isCloseButton(), options.isAutoClose()))
                .toString();

        int index = Integer.parseInt(result);
        return new JLPopup(index, text, latLng, options);
    }

    /**
     * Add popup with {{@link JLOptions#DEFAULT}} options
     *
     * @see JLUiLayer#addPopup(JLLatLng, String, JLOptions)
     */
    @Override
    public JLPopup addPopup(JLLatLng latLng, String text) {
        return addPopup(latLng, text, JLOptions.DEFAULT);
    }

    /**
     * Remove a {@link JLPopup} from the map.
     *
     * @param id of the marker for removing.
     * @return true if removed successfully.
     */
    @Override
    public boolean removePopup(int id) {
        String result = engine.executeScript(String.format("removePopup(%d)", id))
                .toString();
        return Boolean.parseBoolean(result);
    }
}
