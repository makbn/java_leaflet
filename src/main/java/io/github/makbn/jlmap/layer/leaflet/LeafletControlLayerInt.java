package io.github.makbn.jlmap.layer.leaflet;

import io.github.makbn.jlmap.model.JLBounds;
import io.github.makbn.jlmap.model.JLLatLng;

/**
 * The {@code LeafletControlLayerInt} interface defines methods for controlling
 * the zoom and view of a Leaflet map. Leaflet is a popular JavaScript library
 * for creating interactive maps, and this interface provides a Java API for
 * manipulating the map's zoom level, view, and geographical bounds.
 *
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
public interface LeafletControlLayerInt {

    /**
     * Increases the zoom of the map by delta 
     *
     * @see <a href="https://leafletjs.com/reference.html#map-zoomin">leafletjs.com/reference.html#map-zoomin</a>
     */
    void zoomIn(int delta);

    /**
     * Decreases the zoom of the map by delta 
     *
     * @see <a href="https://leafletjs.com/reference.html#map-zoomout">
     *     leafletjs.com/reference.html#map-zoomout</a>
     */
    void zoomOut(int delta);

    /**
     * Sets the zoom of the map.
     *
     * @see <a href="https://leafletjs.com/reference.html#map-setzoom">
     *     leafletjs.com/reference.html#map-setzoom</a>
     */
    void setZoom(int level);

    /**
     * Zooms the map while keeping a specified geographical point on the map
     * stationary (e.g. used internally for scroll zoom and double-click zoom)
     *
     * @see <a href="https://leafletjs.com/reference.html#map-setzoomaround">
     *     leafletjs.com/reference.html#map-setzoomaround</a>
     */
    void setZoomAround(JLLatLng latLng, int zoom);


    /**
     * Sets a map view that contains the given geographical bounds with the
     * maximum zoom level possible.
     *
     * @param bounds The geographical bounds to fit.
     * @see <a href="https://leafletjs.com/reference.html#map-fitbounds">
     *     leafletjs.com/reference.html#map-fitbounds</a>
     */
    void fitBounds(JLBounds bounds);

    /**
     * Sets a map view that mostly contains the whole world with the maximum
     * zoom level possible.
     *
     * @see <a href="https://leafletjs.com/reference.html#map-fitworld">
     *     leafletjs.com/reference.html#map-fitworld</a>
     */
    void fitWorld();

    /**
     * Pans the map to a given center.
     *
     * @param latLng The new center of the map.
     * @see <a href="https://leafletjs.com/reference.html#map-panto">
     *     leafletjs.com/reference.html#map-panto</a>
     */
    void panTo(JLLatLng latLng);

    /**
     * Sets the view of the map (geographical center and zoom) performing a
     * smooth pan-zoom animation.
     *
     * @param latLng The new center of the map.
     * @param zoom   The new zoom level (optional).
     * @see <a href="https://leafletjs.com/reference.html#map-flyto">
     *     leafletjs.com/reference.html#map-flyto</a>
     */
    void flyTo(JLLatLng latLng, int zoom);

    /**
     * Sets the view of the map with a smooth animation like flyTo, but
     * takes a bounds parameter like fitBounds.
     *
     * @param bounds The bounds to fit the map view to.
     * @see <a href="https://leafletjs.com/reference.html#map-flytobounds">
     *     leafletjs.com/reference.html#map-flytobounds</a>
     */
    void flyToBounds(JLBounds bounds);

    /**
     * Restricts the map view to the given bounds.
     *
     * @param bounds The geographical bounds to restrict the map view to.
     * @see <a href="https://leafletjs.com/reference.html#map-setmaxbounds">
     *     leafletjs.com/reference.html#map-setmaxbounds</a>
     */
    void setMaxBounds(JLBounds bounds);

    /**
     * Sets the lower limit for the available zoom levels.
     *
     * @param zoom The minimum zoom level.
     * @see <a href="https://leafletjs.com/reference.html#map-setminzoom">
     *     leafletjs.com/reference.html#map-setminzoom</a>
     */
    void setMinZoom(int zoom);

    /**
     * Sets the upper limit for the available zoom levels.
     *
     * @param zoom The maximum zoom level.
     * @see <a href="https://leafletjs.com/reference.html#map-setmaxzoom">
     *     leafletjs.com/reference.html#map-setmaxzoom</a>
     */
    void setMaxZoom(int zoom);

    /**
     * Pans the map to the closest view that would lie inside the given bounds.
     *
     * @param bounds The bounds to pan inside.
     * @see <a href="https://leafletjs.com/reference.html#map-paninsidebounds">
     *     leafletjs.com/reference.html#map-paninsidebounds</a>
     */
    void panInsideBounds(JLBounds bounds);

    /**
     * Pans the map the minimum amount to make the latLng visible.
     *
     * @param latLng The geographical point to make visible.
     * @see <a href="https://leafletjs.com/reference.html#map-paninside">
     *     leafletjs.com/reference.html#map-paninside</a>
     */
    void panInside(JLLatLng latLng);

}
