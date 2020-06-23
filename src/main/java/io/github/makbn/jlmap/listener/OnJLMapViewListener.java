package io.github.makbn.jlmap.listener;

import io.github.makbn.jlmap.JLMapView;
import io.github.makbn.jlmap.model.JLBounds;
import io.github.makbn.jlmap.model.JLLatLng;


public abstract class OnJLMapViewListener {

    /**
     * called after the map is fully loaded
     *
     * @param mapView
     */
    public abstract void mapLoadedSuccessfully(JLMapView mapView);

    /**
     * called after the map got an exception on loading
     */
    public abstract void mapFailed();

    /**
     * Fired repeatedly during any movement of the map, including pan and fly animations.
     *
     * @param action    action of movement
     * @param center    coordinate of map
     * @param bounds    of map
     * @param zoomLevel of map
     */
    public void onMove(Action action, JLLatLng center, JLBounds bounds, int zoomLevel) {

    }

    /**
     * Fired repeatedly during any movement of the map, including pan and fly animations.
     * Fired when the map has changed, after any animations.
     * Fired when the center of the map stats/stops changing (e.g. user starts/stopped dragging the map).
     *
     * @param action zoom action {{@link Action#ZOOM}}
     *               or {{@link Action#ZOOM_START}} or {{@link Action#ZOOM_END}}
     */
    public void onZoom(Action action, int zoomLevel) {

    }

    public enum Action {
        /**
         * zoom level changes continuously
         */
        ZOOM,
        /**
         * zoom level stats to change
         */
        ZOOM_START,
        /**
         * zoom leve changes end
         */
        ZOOM_END,

        /**
         * map center changes continuously
         */
        MOVE,
        /**
         * user starts to move the map
         */
        MOVE_START,
        /**
         * user ends to move the map
         */
        MOVE_END

    }
}
