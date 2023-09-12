package io.github.makbn.jlmap.layer;

import io.github.makbn.jlmap.JLMapCallbackHandler;
import io.github.makbn.jlmap.layer.leaflet.LeafletControlLayerInt;
import io.github.makbn.jlmap.model.JLBounds;
import io.github.makbn.jlmap.model.JLLatLng;
import javafx.scene.web.WebEngine;

/**
 * Represents the Control layer on Leaflet map.
 *
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
public class JLControlLayer extends JLLayer implements LeafletControlLayerInt {
    public JLControlLayer(WebEngine engine,
                          JLMapCallbackHandler callbackHandler) {
        super(engine, callbackHandler);
    }

    @Override
    public void zoomIn(int delta) {
        engine.executeScript(String.format("getMap().zoomIn(%d)", delta));
    }

    @Override
    public void zoomOut(int delta) {
        engine.executeScript(String.format("getMap().zoomOut(%d)", delta));
    }

    @Override
    public void setZoom(int level) {
        engine.executeScript(String.format("getMap().setZoom(%d)", level));
    }

    @Override
    public void setZoomAround(JLLatLng latLng, int zoom) {
        engine.executeScript(
                String.format("getMap().setZoomAround(L.latLng(%f, %f), %d)",
                        latLng.getLat(), latLng.getLng(), zoom));
    }

    @Override
    public void fitBounds(JLBounds bounds) {
        engine.executeScript(String.format("getMap().fitBounds(%s)",
                bounds.toString()));
    }

    @Override
    public void fitWorld() {
        engine.executeScript("getMap().fitWorld()");
    }

    @Override
    public void panTo(JLLatLng latLng) {
        engine.executeScript(String.format("getMap().panTo(L.latLng(%f, %f))",
                latLng.getLat(), latLng.getLng()));
    }

    @Override
    public void flyTo(JLLatLng latLng, int zoom) {
        engine.executeScript(
                String.format("getMap().flyTo(L.latLng(%f, %f), %d)",
                        latLng.getLat(), latLng.getLng(), zoom));
    }

    @Override
    public void flyToBounds(JLBounds bounds) {
        engine.executeScript(String.format("getMap().flyToBounds(%s)",
                bounds.toString()));
    }

    @Override
    public void setMaxBounds(JLBounds bounds) {
        engine.executeScript(String.format("getMap().setMaxBounds(%s)",
                bounds.toString()));
    }

    @Override
    public void setMinZoom(int zoom) {
        engine.executeScript(String.format("getMap().setMinZoom(%d)", zoom));
    }

    @Override
    public void setMaxZoom(int zoom) {
        engine.executeScript(String.format("getMap().setMaxZoom(%d)", zoom));
    }

    @Override
    public void panInsideBounds(JLBounds bounds) {
        engine.executeScript(String.format("getMap().panInsideBounds(%s)",
                bounds.toString()));
    }

    @Override
    public void panInside(JLLatLng latLng) {
        engine.executeScript(
                String.format("getMap().panInside(L.latLng(%f, %f))",
                        latLng.getLat(), latLng.getLng()));
    }
}
