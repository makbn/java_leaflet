package io.github.makbn.jlmap.layer;

import io.github.makbn.jlmap.JLMapCallbackHandler;
import io.github.makbn.jlmap.JLProperties;
import io.github.makbn.jlmap.model.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;

/**
 * Represents the Vector layer on Leaflet map.
 * by: Mehdi Akbarian Rastaghi (@makbn)
 */
public class JLVectorLayer extends JLLayer {

    public JLVectorLayer(WebEngine engine, JLMapCallbackHandler callbackHandler) {
        super(engine, callbackHandler);
    }

    /**
     * Drawing polyline overlays on the map with {{@link JLOptions#DEFAULT}} options
     *
     * @see {{@link JLVectorLayer#addPolyline(JLLatLng[], JLOptions)}}
     */
    public JLPolyline addPolyline(JLLatLng[] vertices) {
        return addPolyline(vertices, JLOptions.DEFAULT);
    }

    /**
     * Drawing polyline overlays on the map.
     * @param vertices arrays of LatLng points
     * @param options see {{@link JLOptions}} for customizing
     * @return the added {{@link JLPolyline}}  to map
     */
    public JLPolyline addPolyline(JLLatLng[] vertices, JLOptions options) {
        String latlngs = convertJLLatLngToString(vertices);
        String hexColor = convertColorToString(options.getColor());
        String result = engine.executeScript(String.format("addPolyLine(%s, '%s', %d, %b, %f, %f)", latlngs, hexColor,
                options.getWeight(), options.isStroke(), options.getOpacity(), options.getSmoothFactor())).toString();

        int index = Integer.parseInt(result);
        JLPolyline polyline = new JLPolyline(index, options, vertices);
        callbackHandler.addJLObject(polyline);
        return polyline;
    }

    /**
     * Remove a polyline from the map by id.
     * @param id of polyline
     * @return {{@link Boolean#TRUE}} if removed successfully
     */
    public boolean removePolyline(int id) {
        String result = engine.executeScript(String.format("removePolyLine(%d)", id))
                .toString();

        callbackHandler.remove(JLPolyline.class, id);
        callbackHandler.remove(JLMultiPolyline.class, id);

        return Boolean.parseBoolean(result);
    }

    /**
     * Drawing multi polyline overlays on the map with {{@link JLOptions#DEFAULT}} options.
     * @see {{@link JLVectorLayer#addMultiPolyline(JLLatLng[][], JLOptions)}}
     * @return the added {{@link JLMultiPolyline}}  to map
     */
    public JLMultiPolyline addMultiPolyline(JLLatLng[][] vertices){
        return addMultiPolyline(vertices, JLOptions.DEFAULT);
    }

    /**
     * Drawing MultiPolyline shape overlays on the map with multi-dimensional array.
     * @param vertices arrays of LatLng points
     * @param options see {{@link JLOptions}} for customizing
     * @return the added {{@link JLMultiPolyline}}  to map
     */
    public JLMultiPolyline addMultiPolyline(JLLatLng[][] vertices, JLOptions options) {
        String latlngs = convertJLLatLngToString(vertices);
        String hexColor = convertColorToString(options.getColor());
        String result = engine.executeScript(String.format("addPolyLine(%s, '%s', %d, %b, %f, %f)", latlngs, hexColor,
                options.getWeight(), options.isStroke(), options.getOpacity(), options.getSmoothFactor())).toString();

        int index = Integer.parseInt(result);
        JLMultiPolyline multiPolyline = new JLMultiPolyline(index, options, vertices);
        callbackHandler.addJLObject(multiPolyline);
        return multiPolyline;
    }


    /**
     * @see {{@link JLVectorLayer#removePolyline(int)}}
     */
    public boolean removeMultiPolyline(int id){
        return removePolyline(id);
    }

    /**
     * Drawing polygon overlays on the map.
     * Note that points you pass when creating a polygon shouldn't have an additional
     * last point equal to the first one.
     * You can also pass an array of arrays of {{@link JLLatLng}},
     * with the first dimension representing the outer shape and the other
     * dimension representing holes in the outer shape!
     * Additionally, you can pass a multi-dimensional array to represent a MultiPolygon shape!
     * @param vertices array of {{@link JLLatLng}} points
     * @param options see {{@link JLOptions}}
     * @return Instance of the created {{@link JLPolygon}}
     */
    public JLPolygon addPolygon(JLLatLng[][][] vertices, JLOptions options) {
        String latlngs = convertJLLatLngToString(vertices);

        String result = engine.executeScript(String.format("addPolygon(%s, '%s', '%s', %d, %b, %b, %f, %f, %f)",
                latlngs, convertColorToString(options.getColor()), convertColorToString(options.getFillColor()),
                options.getWeight(), options.isStroke(), options.isFill(), options.getOpacity(),
                options.getFillOpacity(), options.getSmoothFactor()))
                .toString();

        int index = Integer.parseInt(result);
        JLPolygon polygon = new JLPolygon(index, options, vertices);
        callbackHandler.addJLObject(polygon);
        return polygon;
    }

    /**
     * Drawing polygon overlays on the map with {{@link JLOptions#DEFAULT}} option.
     * @see {{@link JLVectorLayer#addMultiPolyline(JLLatLng[][], JLOptions)}}
     */
    public JLPolygon addPolygon(JLLatLng[][][] vertices){
        return addPolygon(vertices, JLOptions.DEFAULT);
    }

    /**
     * Remove a {{@link JLPolygon}} from the map by id.
     * @param id of Polygon
     * @return {{@link Boolean#TRUE}} if removed successfully
     */
    public boolean removePolygon(int id){
        String result = engine.executeScript(String.format("removePolygon(%d)", id))
                .toString();
        callbackHandler.remove(JLPolygon.class, id);
        return Boolean.parseBoolean(result);
    }

    /**
     * Add a {{@link JLCircle}} to the map;
     * @param center coordinate of the circle.
     * @param radius radius of circle in meter.
     * @param options see {{@link JLOptions}}
     * @return the instance of created {{@link JLCircle}}
     */
    public JLCircle addCircle(JLLatLng center, int radius, JLOptions options) {
        String result = engine.executeScript(String.format("addCircle([%f, %f], %d, '%s', '%s', %d, %b, %b, %f, %f, %f)",
                center.getLat(), center.getLng(), radius, convertColorToString(options.getColor()), convertColorToString(options.getFillColor()),
                options.getWeight(), options.isStroke(), options.isFill(), options.getOpacity(),
                options.getFillOpacity(), options.getSmoothFactor()))
                .toString();

        int index = Integer.parseInt(result);
        JLCircle circle = new JLCircle(index, radius, center, options);
        callbackHandler.addJLObject(circle);
        return circle;
    }

    /**
     * Add {{@link JLCircle}} to the map with {{@link JLOptions#DEFAULT}} options.
     * Default value for radius is {{@link JLProperties#DEFAULT_CIRCLE_RADIUS}}
     * @see {{@link JLVectorLayer#addCircle(JLLatLng, int, JLOptions)}}
     */
    public JLCircle addCircle(JLLatLng center){
        return addCircle(center, JLProperties.DEFAULT_CIRCLE_RADIUS, JLOptions.DEFAULT);
    }

    /**
     * Remove a {{@link JLCircle}} from the map by id.
     * @param id of Circle
     * @return {{@link Boolean#TRUE}} if removed successfully
     */
    public boolean removeCircle(int id){
        String result = engine.executeScript(String.format("removeCircle(%d)", id))
                .toString();
        callbackHandler.remove(JLCircle.class, id);
        return Boolean.parseBoolean(result);
    }

    /**
     * Add a {{@link JLCircleMarker}} to the map;
     * @param center coordinate of the circle.
     * @param radius radius of circle in meter.
     * @param options see {{@link JLOptions}}
     * @return the instance of created {{@link JLCircleMarker}}
     */
    public JLCircleMarker addCircleMarker(JLLatLng center, int radius, JLOptions options) {
        String result = engine.executeScript(String.format("addCircleMarker([%f, %f], %d, '%s', '%s', %d, %b, %b, %f, %f, %f)",
                center.getLat(), center.getLng(), radius, convertColorToString(options.getColor()), convertColorToString(options.getFillColor()),
                options.getWeight(), options.isStroke(), options.isFill(), options.getOpacity(),
                options.getFillOpacity(), options.getSmoothFactor()))
                .toString();

        int index = Integer.parseInt(result);
        JLCircleMarker circleMarker = new JLCircleMarker(index, radius, center, options);
        callbackHandler.addJLObject(circleMarker);
        return circleMarker;
    }

    /**
     * Add {{@link JLCircleMarker}} to the map with {{@link JLOptions#DEFAULT}} options.
     * Default value for radius is {{@link JLProperties#DEFAULT_CIRCLE_MARKER_RADIUS}}
     *
     * @see {{@link JLVectorLayer#addCircle(JLLatLng, int, JLOptions)}}
     */
    public JLCircleMarker addCircleMarker(JLLatLng center) {
        return addCircleMarker(center, JLProperties.DEFAULT_CIRCLE_MARKER_RADIUS, JLOptions.DEFAULT);
    }


    /**
     * Remove a {{@link JLCircleMarker}} from the map by id.
     *
     * @param id of Circle
     * @return {{@link Boolean#TRUE}} if removed successfully
     */
    public boolean removeCircleMarker(int id) {
        String result = engine.executeScript(String.format("removeCircleMarker(%d)", id))
                .toString();
        callbackHandler.remove(JLCircleMarker.class, id);
        return Boolean.parseBoolean(result);
    }

    private String convertJLLatLngToString(JLLatLng[] latLngs) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (JLLatLng latLng : latLngs) {
            sb.append(String.format("[%f, %f], ", latLng.getLat(), latLng.getLng()));
        }
        sb.append("]");
        return sb.toString();
    }

    private String convertJLLatLngToString(JLLatLng[][] latLngsList){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (JLLatLng[] latLngs : latLngsList){
            sb.append(convertJLLatLngToString(latLngs)).append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private String convertJLLatLngToString(JLLatLng[][][] latLngsList){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (JLLatLng[][] latLngs : latLngsList){
            sb.append(convertJLLatLngToString(latLngs)).append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private String convertColorToString(Color c){
        int r = (int)Math.round(c.getRed() * 255.0);
        int g = (int)Math.round(c.getGreen() * 255.0);
        int b = (int)Math.round(c.getBlue() * 255.0);
        int a = (int)Math.round(c.getOpacity() * 255.0);
        return String.format("#%02x%02x%02x%02x", r, g, b, a);
    }
}
