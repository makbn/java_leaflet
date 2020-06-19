package io.github.makbn.datadispersion.listener;

import io.github.makbn.datadispersion.JLMapView;

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
