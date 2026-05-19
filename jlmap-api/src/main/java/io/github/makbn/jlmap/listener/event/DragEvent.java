package io.github.makbn.jlmap.listener.event;

import io.github.makbn.jlmap.listener.JLAction;
import io.github.makbn.jlmap.model.JLBounds;
import io.github.makbn.jlmap.model.JLLatLng;

/**
 * Represents a drag event for a map marker or object, analogous to Leaflet's
 * <a href="https://leafletjs.com/reference.html#marker-drag">drag</a>,
 * <a href="https://leafletjs.com/reference.html#marker-dragstart">dragstart</a>, and
 * <a href="https://leafletjs.com/reference.html#marker-dragend">dragend</a> events.
 * <p>
 * This event is fired when a marker or object is dragged, starts dragging, or ends dragging.
 * It contains information about the action (DRAG, DRAG_START, DRAG_END), the new center coordinate,
 * the map bounds, and the zoom level at the time of the event.
 * </p>
 * <ul>
 *   <li><b>action</b>: The {@link JLAction} performed</li>
 *   <li><b>center</b>: The new coordinate of the marker/object after the drag event.</li>
 *   <li><b>bounds</b>: The map bounds after the drag event.</li>
 *   <li><b>zoomLevel</b>: The zoom level of the map after the drag event.</li>
 * </ul>
 * <p>
 * Usage in {@link JLDragEventHandler}:
 * <ul>
 *   <li>When a marker/object is dragged, a DragEvent is fired with JLAction.DRAG.</li>
 *   <li>When dragging starts, a DragEvent is fired with JLAction.DRAG_START.</li>
 *   <li>When dragging ends, a DragEvent is fired with JLAction.DRAG_END.</li>
 * </ul>
 * <p>
 * This event allows listeners to react to drag actions, such as updating UI, analytics, or custom logic.
 * </p>
 * @see <a href="https://leafletjs.com/reference.html#marker-drag">Leaflet drag event</a>
 * @see <a href="https://leafletjs.com/reference.html#marker-dragstart">Leaflet dragstart event</a>
 * @see <a href="https://leafletjs.com/reference.html#marker-dragend">Leaflet dragend event</a>
 *
 * @author Matt Akbarian  (@makbn)
 */
public record DragEvent(JLAction action, JLLatLng center,
                        JLBounds bounds, double zoomLevel) implements Event {
}
