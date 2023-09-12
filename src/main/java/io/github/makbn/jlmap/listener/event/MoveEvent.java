package io.github.makbn.jlmap.listener.event;

import io.github.makbn.jlmap.listener.OnJLMapViewListener;
import io.github.makbn.jlmap.model.JLBounds;
import io.github.makbn.jlmap.model.JLLatLng;

/**
 *
 * @param action    movement action
 * @param center    coordinate of map
 * @param bounds    bounds of map
 * @param zoomLevel zoom level of map
 */
public record MoveEvent(OnJLMapViewListener.Action action, JLLatLng center,
                        JLBounds bounds, int zoomLevel) implements Event {
}
