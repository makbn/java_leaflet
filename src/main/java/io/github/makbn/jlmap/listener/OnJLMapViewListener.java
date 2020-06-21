package io.github.makbn.jlmap.listener;

import io.github.makbn.jlmap.JLMapView;

public interface OnJLMapViewListener {

    /**
     * called after the map is fully loaded
     * @param mapView
     */
    void mapLoadedSuccessfully(JLMapView mapView);

    /**
     * called after the map got an exception on loading
     */
    void mapFailed();
}
