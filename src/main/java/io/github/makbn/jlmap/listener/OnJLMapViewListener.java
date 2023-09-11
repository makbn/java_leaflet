package io.github.makbn.jlmap.listener;

import io.github.makbn.jlmap.JLMapView;
import io.github.makbn.jlmap.listener.event.Event;
import lombok.NonNull;


public interface OnJLMapViewListener {

    /**
     * called after the map is fully loaded
     *
     * @param mapView loaded map
     */
    void mapLoadedSuccessfully(@NonNull JLMapView mapView);

    /**
     * called after the map got an exception on loading
     */
    void mapFailed();

    default void onAction(Event event) {

    }


    enum Action {
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
        MOVE_END,
        /**
         * user click on the map
         */
        CLICK

    }
}
