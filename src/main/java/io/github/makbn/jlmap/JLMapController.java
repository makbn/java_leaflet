package io.github.makbn.jlmap;

import io.github.makbn.jlmap.exception.JLMapNotReadyException;
import io.github.makbn.jlmap.layer.*;
import io.github.makbn.jlmap.model.JLLatLng;
import io.github.makbn.jlmap.model.JLMapOption;
import javafx.concurrent.Worker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;

/**
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
abstract class JLMapController extends AnchorPane {
    JLMapOption mapOption;

    JLMapController(@NonNull JLMapOption mapOption) {
        this.mapOption = mapOption;
    }

    protected abstract WebView getWebView();

    protected abstract void addControllerToDocument();

    protected abstract HashMap<Class<? extends JLLayer>, JLLayer> getLayers();

    /**
     * handle all functions for add/remove layers from UI layer
     * @return current instance of {{@link JLUiLayer}}
     */
    public JLUiLayer getUiLayer(){
        checkMapState();
        return (JLUiLayer) getLayers().get(JLUiLayer.class);
    }

    /**
     * handle all functions for add/remove layers from Vector layer
     * @return current instance of {{@link JLVectorLayer}}
     */
    public JLVectorLayer getVectorLayer(){
        checkMapState();
        return (JLVectorLayer) getLayers().get(JLVectorLayer.class);
    }

    public JLControlLayer getControlLayer() {
        checkMapState();
        return (JLControlLayer) getLayers().get(JLControlLayer.class);
    }

    public JLGeoJsonLayer getGeoJsonLayer() {
        checkMapState();
        return (JLGeoJsonLayer) getLayers().get(JLGeoJsonLayer.class);
    }

    /**
     * Sets the view of the map (geographical center).
     * @param latLng Represents a geographical point with a certain latitude
     *               and longitude.
     */
    public void setView(JLLatLng latLng){
        checkMapState();
        getWebView().getEngine()
                .executeScript(String.format("jlmap.panTo([%f, %f]);",
                        latLng.getLat(), latLng.getLng()));
    }

    /**
     * Sets the view of the map (geographical center) with animation duration.
     * @param duration Represents the duration of transition animation.
     * @param latLng Represents a geographical point with a certain latitude
     *               and longitude.
     */
    public void setView(JLLatLng latLng, int duration){
        checkMapState();
        getWebView().getEngine()
                .executeScript(String.format("setLatLng(%f, %f,%d);",
                        latLng.getLat(), latLng.getLng(), duration));
    }

    private void checkMapState() {
        if (getWebView() == null ||
                getWebView().getEngine()
                        .getLoadWorker().getState() != Worker.State.SUCCEEDED) {
            throw JLMapNotReadyException.builder().build();
        }
    }

}
