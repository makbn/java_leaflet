package io.github.makbn.jlmap;

import io.github.makbn.jlmap.exception.JLMapNotReadyException;
import io.github.makbn.jlmap.layer.JLLayer;
import io.github.makbn.jlmap.layer.JLUiLayer;
import io.github.makbn.jlmap.layer.JLVectorLayer;
import io.github.makbn.jlmap.model.JLLatLng;
import io.github.makbn.jlmap.model.JLMapOption;
import javafx.concurrent.Worker;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

import java.util.HashMap;

/**
 * by: Mehdi Akbarian Rastaghi (@makbn)
 */
abstract class JLMapController extends AnchorPane {

    protected abstract WebView getWebView();

    protected abstract void addControllerToDocument();

    protected abstract HashMap<String, JLLayer> getLayers();

    protected JLMapOption mapOption;

    JLMapController(JLMapOption mapOption) {
        this.mapOption = JLMapOption.builder().build();
        if (mapOption.getMapType() != null)
            this.mapOption.setMapType(mapOption.getMapType());
        if (mapOption.getStartCoordinate() != null)
            this.mapOption.setStartCoordinate(mapOption.getStartCoordinate());
        if (mapOption.getAccessToken() != null)
            this.mapOption.setAccessToken(mapOption.getAccessToken());
    }

    private JLMapController(Node... children) {
        super(children);
        //do nothing
    }

    /**
     * handle all functions for add/remove layers from UI layer
     * @return current instance of {{@link JLUiLayer}}
     */
    public JLUiLayer getUiLayer(){
        checkMapState();
        return (JLUiLayer) getLayers().get(JLUiLayer.class.getSimpleName());
    }

    /**
     * handle all functions for add/remove layers from Vector layer
     * @return current instance of {{@link JLVectorLayer}}
     */
    public JLVectorLayer getVectorLayer(){
        checkMapState();
        return (JLVectorLayer) getLayers().get(JLVectorLayer.class.getSimpleName());
    }

    /**
     * Sets the view of the map (geographical center).
     * @param latLng Represents a geographical point with a certain latitude and longitude.
     */
    public void setView(JLLatLng latLng){
        checkMapState();
        getWebView().getEngine()
                .executeScript(String.format("jlmap.panTo([%f, %f]);", latLng.getLat(), latLng.getLng()));
    }

    /**
     * Sets the view of the map (geographical center) with animation duration.
     * @param duration Represents the duration of transition animation.
     * @param latLng Represents a geographical point with a certain latitude and longitude.
     */
    public void setView(JLLatLng latLng, int duration){
        checkMapState();
        getWebView().getEngine()
                .executeScript(String.format("setLatLng(%f, %f,%d);", latLng.getLat(), latLng.getLng(), duration));
    }

    private void checkMapState() {
        if(getWebView() == null || getWebView().getEngine().getLoadWorker().getState() != Worker.State.SUCCEEDED){
            throw JLMapNotReadyException.builder().build();
        }
    }

}
