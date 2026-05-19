package io.github.makbn.jlmap.listener.event;

import io.github.makbn.jlmap.listener.JLAction;

/**
 * Represents a resize event for the map view.
 * <p>
 * This event is fired when the map container is resized. It contains information about the new and old dimensions
 * of the map, as well as the current zoom level. This is similar to Leaflet's <a href="https://leafletjs.com/reference.html#resizeevent">ResizeEvent</a>.
 * </p>
 * <ul>
 *   <li><b>action</b>: The JLAction that triggered the event.</li>
 *   <li><b>newWidth</b>: The new width of the map container in pixels.</li>
 *   <li><b>newHeight</b>: The new height of the map container in pixels.</li>
 *   <li><b>oldWidth</b>: The previous width of the map container in pixels.</li>
 *   <li><b>oldHeight</b>: The previous height of the map container in pixels.</li>
 *   <li><b>zoom</b>: The current zoom level of the map.</li>
 * </ul>
 * <p>
 * This event can be used to handle UI adjustments or custom logic when the map size changes.
 * </p>
 *
 * @author Matt Akbarian  (@makbn)
 * @see <a href="https://leafletjs.com/reference.html#resizeevent">Leaflet ResizeEvent Documentation</a>
 */
public record ResizeEvent(JLAction action, int newWidth, int newHeight, int oldWidth, int oldHeight, double zoom)
        implements Event {
}
