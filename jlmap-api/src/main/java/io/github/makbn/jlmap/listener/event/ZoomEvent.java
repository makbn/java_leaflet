package io.github.makbn.jlmap.listener.event;

import io.github.makbn.jlmap.listener.JLAction;
import io.github.makbn.jlmap.model.JLBounds;

/**
 * Represents a zoom event for the map view, analogous to Leaflet's
 * <a href="https://leafletjs.com/reference.html#map-zoom">zoom</a> event.
 * <p>
 * This event is fired when the map zoom level changes, either by user interaction or programmatically.
 * It contains information about the triggering action, the new zoom level, and the map bounds after zooming.
 * </p>
 * <ul>
 *   <li><b>action</b>: The JLAction that triggered the zoom.</li>
 *   <li><b>zoomLevel</b>: The new zoom level of the map after the event.</li>
 *   <li><b>bounds</b>: The map bounds after the zoom event.</li>
 * </ul>
 * <p>
 * This event can be used to handle UI updates, analytics, or custom logic when the map zoom level changes.
 * </p>
 * @see <a href="https://leafletjs.com/reference.html#map-zoom">Leaflet zoom event</a>
 *
 * @author Matt Akbarian  (@makbn)
 * */
public record ZoomEvent(JLAction action, double zoomLevel, JLBounds bounds) implements Event {

}
