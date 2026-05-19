package io.github.makbn.jlmap.listener.event;

import io.github.makbn.jlmap.listener.JLAction;
import io.github.makbn.jlmap.model.JLBounds;
import io.github.makbn.jlmap.model.JLLatLng;

/**
 * Represents a map movement event, similar to Leaflet's move, movestart, and moveend events.
 * <p>
 * This event is fired when the map view is moved, including panning or programmatic changes to the center.
 * It contains information about the movement action, the new center coordinate, the map bounds, and the zoom level.
 * </p>
 * <ul>
 *   <li><b>action</b>: The JLAction that triggered the movement.</li>
 *   <li><b>center</b>: The new center coordinate of the map after movement.</li>
 *   <li><b>bounds</b>: The new bounds of the map after movement.</li>
 *   <li><b>zoomLevel</b>: The current zoom level of the map.</li>
 * </ul>
 * <p>
 * This event can be used to handle UI updates, analytics, or custom logic when the map is moved. It is analogous to:
 * <ul>
 *   <li><a href="https://leafletjs.com/reference.html#map-move">move</a>: Fired repeatedly while the map is moving.</li>
 *   <li><a href="https://leafletjs.com/reference.html#map-movestart">movestart</a>: Fired once when movement starts.</li>
 *   <li><a href="https://leafletjs.com/reference.html#map-moveend">moveend</a>: Fired once when movement ends.</li>
 * </ul>
 * </p>
 * @see <a href="https://leafletjs.com/reference.html#map-move">Leaflet move event</a>
 * @see <a href="https://leafletjs.com/reference.html#map-movestart">Leaflet movestart event</a>
 * @see <a href="https://leafletjs.com/reference.html#map-moveend">Leaflet moveend event</a>
 *
 * @author Matt Akbarian  (@makbn)
 */
public record MoveEvent(JLAction action, JLLatLng center,
                        JLBounds bounds, double zoomLevel) implements Event {
}
