package io.github.makbn.jlmap.layer.leaflet;

import io.github.makbn.jlmap.exception.JLException;
import io.github.makbn.jlmap.geojson.JLGeoJsonObject;
import lombok.NonNull;

import java.io.File;

/**
 * The {@code LeafletGeoJsonLayerInt} interface defines methods for adding
 * and managing GeoJSON data layers in a Leaflet map.
 * <p>
 * Implementations of this interface should provide methods to add GeoJSON
 * data from various sources, such as files, URLs, or raw content, as well
 * as the ability to remove GeoJSON objects from the map.
 *
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
public interface LeafletGeoJsonLayerInt {

    /**
     * Adds a GeoJSON object from a file to the Leaflet map.
     *
     * @param file The {@link File} object representing the GeoJSON file to be
     *             added.
     * @return The {@link JLGeoJsonObject} representing the added GeoJSON data.
     * @throws JLException If there is an error while adding the GeoJSON data.
     */
    JLGeoJsonObject addFromFile(@NonNull File file) throws JLException;

    /**
     * Adds a GeoJSON object from a URL to the Leaflet map.
     *
     * @param url The URL of the GeoJSON data to be added.
     * @return The {@link JLGeoJsonObject} representing the added GeoJSON data.
     * @throws JLException If there is an error while adding the GeoJSON data.
     */
    JLGeoJsonObject addFromUrl(@NonNull String url) throws JLException;

    /**
     * Adds a GeoJSON object from raw content to the Leaflet map.
     *
     * @param content The raw GeoJSON content to be added.
     * @return The {@link JLGeoJsonObject} representing the added GeoJSON data.
     * @throws JLException If there is an error while adding the GeoJSON data.
     */
    JLGeoJsonObject addFromContent(@NonNull String content) throws JLException;

    /**
     * Removes a GeoJSON object from the Leaflet map.
     *
     * @param object The {@link JLGeoJsonObject} to be removed from the map.
     * @return {@code true} if the removal was successful, {@code false}
     * if the object was not found or could not be removed.
     */
    boolean removeGeoJson(@NonNull JLGeoJsonObject object);

}
