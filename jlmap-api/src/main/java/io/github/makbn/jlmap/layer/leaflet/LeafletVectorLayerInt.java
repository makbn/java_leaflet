package io.github.makbn.jlmap.layer.leaflet;

import io.github.makbn.jlmap.model.*;

/**
 * The {@code LeafletVectorLayerInt} interface defines methods for adding and managing
 * vector-based elements such as polylines, polygons, circles, and circle markers in a
 * Leaflet map. Leaflet is a popular JavaScript library for creating interactive maps, and
 * this interface provides a Java API for working with vector-based layers within Leaflet.
 *
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
public interface LeafletVectorLayerInt {

    /**
     * Adds a polyline to the Leaflet map with the provided array of vertices.
     *
     * @param vertices An array of geographical coordinates (latitude and longitude) that define
     *                 the vertices of the polyline.
     * @return The {@link JLPolyline} representing the added polyline on the map.
     */
    JLPolyline addPolyline(JLLatLng[] vertices);

    /**
     * Adds a polyline to the Leaflet map with the provided array of vertices and custom options.
     *
     * @param vertices An array of geographical coordinates (latitude and longitude) that define
     *                 the vertices of the polyline.
     * @param options  Custom options for configuring the appearance and behavior of the polyline.
     * @return The {@link JLPolyline} representing the added polyline on the map.
     */
    JLPolyline addPolyline(JLLatLng[] vertices, JLOptions options);

    /**
     * Removes a polyline from the Leaflet map based on its identifier.
     *
     * @param id The unique identifier of the polyline to be removed.
     * @return {@code true} if the polyline was successfully removed, {@code false} if the
     *         polyline with the specified identifier was not found.
     */

    boolean removePolyline(int id);

    /**
     * Adds a multi-polyline to the Leaflet map with the provided array of arrays of vertices.
     *
     * @param vertices An array of arrays of geographical coordinates (latitude and longitude) that
     *                 define the vertices of multiple polylines.
     * @return The {@link JLMultiPolyline} representing the added multi-polyline on the map.
     */
    JLMultiPolyline addMultiPolyline(JLLatLng[][] vertices);

    /**
     * Adds a multi-polyline to the Leaflet map with the provided array of arrays of vertices and custom options.
     *
     * @param vertices An array of arrays of geographical coordinates (latitude and longitude) that
     *                 define the vertices of multiple polylines.
     * @param options  Custom options for configuring the appearance and behavior of the multi-polyline.
     * @return The {@link JLMultiPolyline} representing the added multi-polyline on the map.
     */
    JLMultiPolyline addMultiPolyline(JLLatLng[][] vertices, JLOptions options);

    /**
     * Removes a multi-polyline from the Leaflet map based on its identifier.
     *
     * @param id The unique identifier of the multi-polyline to be removed.
     * @return {@code true} if the multi-polyline was successfully removed, {@code false} if the
     *         multi-polyline with the specified identifier was not found.
     */
    boolean removeMultiPolyline(int id);

    /**
     * Adds a polygon to the Leaflet map with the provided array of arrays of vertices and custom options.
     *
     * @param vertices An array of arrays of geographical coordinates (latitude and longitude) that
     *                 define the vertices of the polygon.
     * @param options  Custom options for configuring the appearance and behavior of the polygon.
     * @return The {@link JLPolygon} representing the added polygon on the map.
     */
    JLPolygon addPolygon(JLLatLng[][][] vertices, JLOptions options);

    /**
     * Adds a polygon to the Leaflet map with the provided array of arrays of vertices.
     *
     * @param vertices An array of arrays of geographical coordinates (latitude and longitude) that
     *                 define the vertices of the polygon.
     * @return The {@link JLPolygon} representing the added polygon on the map.
     */
    JLPolygon addPolygon(JLLatLng[][][] vertices);

    /**
     * Removes a polygon from the Leaflet map based on its identifier.
     *
     * @param id The unique identifier of the polygon to be removed.
     * @return {@code true} if the polygon was successfully removed, {@code false} if the
     *         polygon with the specified identifier was not found.
     */
    boolean removePolygon(int id);

    /**
     * Adds a circle to the Leaflet map with the provided center coordinates, radius, and custom options.
     *
     * @param center  The geographical coordinates (latitude and longitude) of the circle's center.
     * @param radius  The radius of the circle in meters.
     * @param options Custom options for configuring the appearance and behavior of the circle.
     * @return The {@link JLCircle} representing the added circle on the map.
     */
    JLCircle addCircle(JLLatLng center, int radius, JLOptions options);

    /**
     * Adds a circle to the Leaflet map with the provided center coordinates and radius.
     *
     * @param center The geographical coordinates (latitude and longitude) of the circle's center.
     * @return The {@link JLCircle} representing the added circle on the map.
     */
    JLCircle addCircle(JLLatLng center);

    /**
     * Removes a circle from the Leaflet map based on its identifier.
     *
     * @param id The unique identifier of the circle to be removed.
     * @return {@code true} if the circle was successfully removed, {@code false} if the
     *         circle with the specified identifier was not found.
     */
    boolean removeCircle(int id);

    /**
     * Adds a circle marker to the Leaflet map with the provided center coordinates, radius, and custom options.
     *
     * @param center  The geographical coordinates (latitude and longitude) of the circle marker's center.
     * @param radius  The radius of the circle marker in pixels.
     * @param options Custom options for configuring the appearance and behavior of the circle marker.
     * @return The {@link JLCircleMarker} representing the added circle marker on the map.
     */
    JLCircleMarker addCircleMarker(JLLatLng center, int radius, JLOptions options);

    /**
     * Adds a circle marker to the Leaflet map with the provided center coordinates and radius.
     *
     * @param center The geographical coordinates (latitude and longitude) of the circle marker's center.
     * @return The {@link JLCircleMarker} representing the added circle marker on the map.
     */
    JLCircleMarker addCircleMarker(JLLatLng center);

    /**
     * Removes a circle marker from the Leaflet map based on its identifier.
     *
     * @param id The unique identifier of the circle marker to be removed.
     * @return {@code true} if the circle marker was successfully removed, {@code false} if the
     *         circle marker with the specified identifier was not found.
     */
    boolean removeCircleMarker(int id);
}

