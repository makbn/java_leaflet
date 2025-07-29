package io.github.makbn.jlmap;

import io.github.makbn.jlmap.engine.JLWebEngine;
import io.github.makbn.jlmap.exception.JLMapNotReadyException;
import io.github.makbn.jlmap.layer.*;
import io.github.makbn.jlmap.model.JLLatLng;

import java.util.HashMap;

/**
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
interface JLMapController {

    JLWebEngine getJLEngine();

    void addControllerToDocument();

    HashMap<Class<? extends JLLayer>, JLLayer> getLayers();

    /**
     * handle all functions for add/remove layers from UI layer
     *
     * @return current instance of {{@link JLUiLayer}}
     */
    default JLUiLayer getUiLayer() {
        checkMapState();
        return (JLUiLayer) getLayers().get(JLUiLayer.class);
    }

    /**
     * handle all functions for add/remove layers from Vector layer
     *
     * @return current instance of {{@link JLVectorLayer}}
     */
    default JLVectorLayer getVectorLayer() {
        checkMapState();
        return (JLVectorLayer) getLayers().get(JLVectorLayer.class);
    }

    default JLControlLayer getControlLayer() {
        checkMapState();
        return (JLControlLayer) getLayers().get(JLControlLayer.class);
    }

    default JLGeoJsonLayer getGeoJsonLayer() {
        checkMapState();
        return (JLGeoJsonLayer) getLayers().get(JLGeoJsonLayer.class);
    }

    /**
     * Sets the view of the map (geographical center).
     *
     * @param latLng Represents a geographical point with a certain latitude
     *               and longitude.
     */
    default void setView(JLLatLng latLng) {
        checkMapState();
        getJLEngine()
                .executeScript(String.format("jlmap.panTo([%f, %f]);",
                        latLng.getLat(), latLng.getLng()));
    }

    /**
     * Sets the view of the map (geographical center) with animation duration.
     *
     * @param duration Represents the duration of transition animation.
     * @param latLng   Represents a geographical point with a certain latitude
     *                 and longitude.
     */
    default void setView(JLLatLng latLng, int duration) {
        checkMapState();
        getJLEngine()
                .executeScript(String.format("setLatLng(%f, %f,%d);",
                        latLng.getLat(), latLng.getLng(), duration));
    }

    private void checkMapState() {
        if (getJLEngine() == null || getJLEngine().getStatus() != JLWebEngine.Status.SUCCEEDED) {
            throw JLMapNotReadyException.builder().build();
        }
    }

}
