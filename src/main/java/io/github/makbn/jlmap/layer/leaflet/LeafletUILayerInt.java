package io.github.makbn.jlmap.layer.leaflet;

import io.github.makbn.jlmap.model.JLLatLng;
import io.github.makbn.jlmap.model.JLMarker;
import io.github.makbn.jlmap.model.JLOptions;
import io.github.makbn.jlmap.model.JLPopup;

/**
 * The {@code LeafletUILayerInt} interface defines methods for adding and
 * managing user interface elements like markers and popups in a Leaflet map.
 * Leaflet is a popular JavaScript library for creating interactive maps,
 * and this interface provides a Java API for working with user interface
 * elements within Leaflet.
 *
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
public interface LeafletUILayerInt {

    /**
     * Adds a marker to the Leaflet map at the specified geographical
     * coordinates.
     *
     * @param latLng   The geographical coordinates (latitude and longitude)
     *                 where the marker should be placed.
     * @param text     The text content associated with the marker.
     * @param draggable {@code true} if the marker should be draggable,
     *                  {@code false} otherwise.
     * @return The {@link JLMarker} representing the added marker on the map.
     */
    JLMarker addMarker(JLLatLng latLng, String text, boolean draggable);

    /**
     * Removes a marker from the Leaflet map based on its identifier.
     *
     * @param id The unique identifier of the marker to be removed.
     * @return {@code true} if the marker was successfully removed,
     * {@code false} if the marker with the specified identifier was not found.
     *
     */
    boolean removeMarker(int id);

    /**
     * Adds a popup to the Leaflet map at the specified geographical
     * coordinates with custom options.
     *
     * @param latLng  The geographical coordinates (latitude and longitude)
     *                where the popup should be displayed.
     * @param text    The text content of the popup.
     * @param options Custom options for configuring the appearance and
     *                behavior of the popup.
     * @return The {@link JLPopup} representing the added popup on the map.
     */
    JLPopup addPopup(JLLatLng latLng, String text, JLOptions options);

    /**
     * Adds a popup to the Leaflet map at the specified geographical
     * coordinates with default options.
     *
     * @param latLng The geographical coordinates (latitude and longitude)
     *               where the popup should be displayed.
     * @param text   The text content of the popup.
     * @return The {@link JLPopup} representing the added popup on the map.
     */
    JLPopup addPopup(JLLatLng latLng, String text);

    /**
     * Removes a popup from the Leaflet map based on its identifier.
     *
     * @param id The unique identifier of the popup to be removed.
     * @return {@code true} if the popup was successfully removed,
     *         {@code false} if the popup with the specified identifier
     *         was not found.
     */
    boolean removePopup(int id);
}

