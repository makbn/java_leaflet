package io.github.makbn.jlmap.layer;

import io.github.makbn.jlmap.JLMapCallbackHandler;
import io.github.makbn.jlmap.JLProperties;
import io.github.makbn.jlmap.engine.JLWebEngine;
import io.github.makbn.jlmap.layer.leaflet.LeafletVectorLayerInt;
import io.github.makbn.jlmap.model.*;

/**
 * Represents the Vector layer on Leaflet map.
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
public class JLVectorLayer extends JLLayer implements LeafletVectorLayerInt {

    public JLVectorLayer(JLWebEngine engine,
                         JLMapCallbackHandler callbackHandler) {
        super(engine, callbackHandler);
    }

    /**
     * Drawing polyline overlays on the map with {@link JLOptions#DEFAULT}
     * options
     *
     * @see JLVectorLayer#addPolyline(JLLatLng[], JLOptions)
     */
    @Override
    public JLPolyline addPolyline(JLLatLng[] vertices) {
        return addPolyline(vertices, JLOptions.DEFAULT);
    }

    /**
     * Drawing polyline overlays on the map.
     *
     * @param vertices arrays of LatLng points
     * @param options  see {@link JLOptions} for customizing
     * @return the added {@link JLPolyline}  to map
     */
    @Override
    public JLPolyline addPolyline(JLLatLng[] vertices, JLOptions options) {
        String latlngs = convertJLLatLngToString(vertices);
        String hexColor = convertColorToString(options.getColor());
        String result = engine.executeScript(
                        String.format("addPolyLine(%s, '%s', %d, %b, %f, %f)",
                                latlngs, hexColor, options.getWeight(),
                                options.isStroke(), options.getOpacity(),
                                options.getSmoothFactor()))
                .toString();

        int index = Integer.parseInt(result);
        JLPolyline polyline = new JLPolyline(index, options, vertices);
        callbackHandler.addJLObject(polyline);
        return polyline;
    }

    /**
     * Remove a polyline from the map by id.
     *
     * @param id of polyline
     * @return {@link Boolean#TRUE} if removed successfully
     */
    @Override
    public boolean removePolyline(int id) {
        String result = engine.executeScript(
                String.format("removePolyLine(%d)", id)).toString();

        callbackHandler.remove(JLPolyline.class, id);
        callbackHandler.remove(JLMultiPolyline.class, id);

        return Boolean.parseBoolean(result);
    }

    /**
     * Drawing multi polyline overlays on the map with
     * {@link JLOptions#DEFAULT} options.
     *
     * @return the added {@link JLMultiPolyline}  to map
     * @see JLVectorLayer#addMultiPolyline(JLLatLng[][], JLOptions)
     */
    @Override
    public JLMultiPolyline addMultiPolyline(JLLatLng[][] vertices) {
        return addMultiPolyline(vertices, JLOptions.DEFAULT);
    }

    /**
     * Drawing MultiPolyline shape overlays on the map with
     * multi-dimensional array.
     *
     * @param vertices arrays of LatLng points
     * @param options  see {@link JLOptions} for customizing
     * @return the added {@link JLMultiPolyline}  to map
     */
    @Override
    public JLMultiPolyline addMultiPolyline(JLLatLng[][] vertices,
                                            JLOptions options) {
        String latlngs = convertJLLatLngToString(vertices);
        String hexColor = convertColorToString(options.getColor());
        String result = engine.executeScript(
                        String.format("addPolyLine(%s, '%s', %d, %b, %f, %f)",
                                latlngs, hexColor, options.getWeight(),
                                options.isStroke(), options.getOpacity(),
                                options.getSmoothFactor()))
                .toString();

        int index = Integer.parseInt(result);
        JLMultiPolyline multiPolyline = new JLMultiPolyline(index, options, vertices);
        callbackHandler.addJLObject(multiPolyline);
        return multiPolyline;
    }

    /**
     * Remove a multi polyline from the map by id.
     *
     * @param id of multi polyline
     * @return {@link Boolean#TRUE} if removed successfully
     */
    @Override
    public boolean removeMultiPolyline(int id) {
        String result = engine.executeScript(
                String.format("removePolyLine(%d)", id)).toString();

        callbackHandler.remove(JLMultiPolyline.class, id);

        return Boolean.parseBoolean(result);
    }

    /**
     * Drawing polygon overlays on the map with {@link JLOptions#DEFAULT}
     * options.
     *
     * @see JLVectorLayer#addPolygon(JLLatLng[][][], JLOptions)
     */
    @Override
    public JLPolygon addPolygon(JLLatLng[][][] vertices, JLOptions options) {
        String latlngs = convertJLLatLngToString(vertices);
        String hexColor = convertColorToString(options.getColor());
        String fillHexColor = convertColorToString(options.getFillColor());
        String result = engine.executeScript(
                        String.format("addPolygon(%s, '%s', '%s', %d, %b, %b, %f, %f)",
                                latlngs, hexColor, fillHexColor, options.getWeight(),
                                options.isStroke(), options.isFill(), options.getOpacity(),
                                options.getFillOpacity()))
                .toString();

        int index = Integer.parseInt(result);
        JLPolygon polygon = new JLPolygon(index, options, vertices);
        callbackHandler.addJLObject(polygon);
        return polygon;
    }

    /**
     * Drawing polygon overlays on the map with {@link JLOptions#DEFAULT}
     * options.
     *
     * @see JLVectorLayer#addPolygon(JLLatLng[][][], JLOptions)
     */
    @Override
    public JLPolygon addPolygon(JLLatLng[][][] vertices) {
        return addPolygon(vertices, JLOptions.DEFAULT);
    }

    /**
     * Remove a polygon from the map by id.
     *
     * @param id of polygon
     * @return {@link Boolean#TRUE} if removed successfully
     */
    @Override
    public boolean removePolygon(int id) {
        String result = engine.executeScript(
                String.format("removePolygon(%d)", id)).toString();

        callbackHandler.remove(JLPolygon.class, id);

        return Boolean.parseBoolean(result);
    }

    /**
     * Drawing circle overlays on the map.
     *
     * @param center  center point of circle
     * @param radius  radius of circle in meters
     * @param options see {@link JLOptions} for customizing
     * @return the added {@link JLCircle}  to map
     */
    @Override
    public JLCircle addCircle(JLLatLng center, int radius, JLOptions options) {
        String hexColor = convertColorToString(options.getColor());
        String fillHexColor = convertColorToString(options.getFillColor());
        String result = engine.executeScript(
                        String.format("addCircle([%f, %f], %d, '%s', '%s', %d, %b, %b, %f, %f)",
                                center.getLat(), center.getLng(), radius, hexColor, fillHexColor,
                                options.getWeight(), options.isStroke(), options.isFill(),
                                options.getOpacity(), options.getFillOpacity()))
                .toString();

        int index = Integer.parseInt(result);
        JLCircle circle = new JLCircle(index, radius, center, options);
        callbackHandler.addJLObject(circle);
        return circle;
    }

    /**
     * Drawing circle overlays on the map with {@link JLOptions#DEFAULT}
     * options.
     *
     * @see JLVectorLayer#addCircle(JLLatLng, int, JLOptions)
     */
    @Override
    public JLCircle addCircle(JLLatLng center) {
        return addCircle(center, 1000, JLOptions.DEFAULT);
    }

    /**
     * Remove a circle from the map by id.
     *
     * @param id of circle
     * @return {@link Boolean#TRUE} if removed successfully
     */
    @Override
    public boolean removeCircle(int id) {
        String result = engine.executeScript(
                String.format("removeCircle(%d)", id)).toString();

        callbackHandler.remove(JLCircle.class, id);

        return Boolean.parseBoolean(result);
    }

    /**
     * Drawing circle marker overlays on the map.
     *
     * @param center  center point of circle marker
     * @param radius  radius of circle marker in pixels
     * @param options see {@link JLOptions} for customizing
     * @return the added {@link JLCircleMarker}  to map
     */
    @Override
    public JLCircleMarker addCircleMarker(JLLatLng center, int radius,
                                          JLOptions options) {
        String hexColor = convertColorToString(options.getColor());
        String fillHexColor = convertColorToString(options.getFillColor());
        String result = engine.executeScript(
                        String.format("addCircleMarker([%f, %f], %d, '%s', '%s', %d, %b, %b, %f, %f)",
                                center.getLat(), center.getLng(), radius, hexColor, fillHexColor,
                                options.getWeight(), options.isStroke(), options.isFill(),
                                options.getOpacity(), options.getFillOpacity()))
                .toString();

        int index = Integer.parseInt(result);
        JLCircleMarker circleMarker = new JLCircleMarker(index, radius, center, options);
        callbackHandler.addJLObject(circleMarker);
        return circleMarker;
    }

    /**
     * Drawing circle marker overlays on the map with {@link JLOptions#DEFAULT}
     * options.
     *
     * @see JLVectorLayer#addCircleMarker(JLLatLng, int, JLOptions)
     */
    @Override
    public JLCircleMarker addCircleMarker(JLLatLng center) {
        return addCircleMarker(center, 6, JLOptions.DEFAULT);
    }

    /**
     * Remove a circle marker from the map by id.
     *
     * @param id of circle marker
     * @return {@link Boolean#TRUE} if removed successfully
     */
    @Override
    public boolean removeCircleMarker(int id) {
        String result = engine.executeScript(
                String.format("removeCircleMarker(%d)", id)).toString();

        callbackHandler.remove(JLCircleMarker.class, id);

        return Boolean.parseBoolean(result);
    }

    private String convertJLLatLngToString(JLLatLng[] latLngs) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < latLngs.length; i++) {
            sb.append("[").append(latLngs[i].getLat()).append(", ")
                    .append(latLngs[i].getLng()).append("]");
            if (i < latLngs.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private String convertJLLatLngToString(JLLatLng[][] latLngsList) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < latLngsList.length; i++) {
            sb.append(convertJLLatLngToString(latLngsList[i]));
            if (i < latLngsList.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private String convertJLLatLngToString(JLLatLng[][][] latLngList) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < latLngList.length; i++) {
            sb.append(convertJLLatLngToString(latLngList[i]));
            if (i < latLngList.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private String convertColorToString(JLColor c) {
        return c.toHexString();
    }
}
