package io.github.makbn.jlmap;

import io.github.makbn.jlmap.listener.OnJLMapViewListener;
import io.github.makbn.jlmap.listener.OnJLObjectActionListener;
import io.github.makbn.jlmap.model.*;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.log4j.Log4j2;

/**
 * by: Mehdi Akbarian Rastaghi (@makbn)
 */
@Log4j2
public class Leaflet extends Application {
    private final String ACCESS_TOKEN = "pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw";
    @Override
    public void start(Stage stage) {

        stage.initStyle(StageStyle.TRANSPARENT);

        //building a new map view
        final JLMapView map = JLMapView
                .builder()
                .mapType(JLProperties.MapType.DARK)
                .accessToken(ACCESS_TOKEN)
                .startCoordinate(JLLatLng.builder()
                        .lat(43.54)
                        .lng(22.54)
                        .build())
                .build();

        //creating a window
        AnchorPane inside = createBasePane();
        AnchorPane root = new AnchorPane(inside);
        root.setBackground(Background.EMPTY);
        root.setMinHeight(JLProperties.INIT_MIN_HEIGHT_STAGE);
        root.setMinWidth(JLProperties.INIT_MIN_WIDTH_STAGE);
        Scene scene = new Scene(root);

        //adding map to window
        inside.getChildren().add(map);

        stage.setMinHeight(JLProperties.INIT_MIN_HEIGHT_STAGE);
        stage.setMinWidth(JLProperties.INIT_MIN_WIDTH_STAGE);
        scene.setFill(Color.TRANSPARENT);
        stage.setTitle("Java-Leaflet Test");
        stage.setScene(scene);
        stage.show();

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY(100);

        //set listener fo map events
        map.setMapListener(new OnJLMapViewListener() {
            @Override
            public void mapLoadedSuccessfully(JLMapView mapView) {
                addMultiPolyline(map);
                addPolyline(map);
                addPolygon(map);

                map.setView(JLLatLng.builder()
                        .lng(10)
                        .lat(10)
                        .build());

                map.getUiLayer()
                        .addMarker(JLLatLng.builder()
                                .lat(35.63)
                                .lng(51.45)
                                .name("tehran")
                                .build(), "tehran", true)
                        .setOnActionListener(getListener());

                map.getVectorLayer()
                        .addCircleMarker(JLLatLng.builder()
                                .lat(35.63)
                                .lng(40.45)
                                .name("sari")
                                .build());

                map.getVectorLayer()
                        .addCircle(JLLatLng.builder()
                                .lat(35.63)
                                .lng(51.45)
                                .name("tehran").build(), 30000, JLOptions.DEFAULT);
            }

            @Override
            public void mapFailed() {
                log.error("map failed!");
            }

            @Override
            public void onMove(Action action, JLLatLng center, JLBounds bounds, int zoomLevel) {
                super.onMove(action, center, bounds, zoomLevel);

                System.out.println("map on move: " + action + " c:" + center + " \t bounds:" + bounds + "\t z:" + zoomLevel);

            }
        });
    }

    private OnJLObjectActionListener<JLMarker> getListener() {
        return new OnJLObjectActionListener<JLMarker>() {
            @Override
            public void click(JLMarker object, Action action) {
                System.out.println("object click listener for marker:" + object);
            }

            @Override
            public void move(JLMarker object, Action action) {
                System.out.println("object move listener for marker:" + object);
            }
        };
    }

    private void addMultiPolyline(JLMapView map) {
        JLLatLng[][] verticesT = new JLLatLng[2][];

        verticesT[0] = new JLLatLng[]{
                new JLLatLng("v1", 41.509, 20.08),
                new JLLatLng("v2", 31.503, -10.06),
                new JLLatLng("v3", 21.51, -0.047)
        };

        verticesT[1] = new JLLatLng[]{
                new JLLatLng("v1", 51.509, 10.08),
                new JLLatLng("v2", 55.503, 15.06),
                new JLLatLng("v3", 42.51, 20.047)
        };

        map.getVectorLayer().addMultiPolyline(verticesT);
    }

    private void addPolyline(JLMapView map) {
        JLLatLng[] vertices = new JLLatLng[]{
                new JLLatLng("v1", 51.509, -0.08),
                new JLLatLng("v2", 51.503, -0.06),
                new JLLatLng("v3", 51.51, -0.047)
        };

        map.getVectorLayer().addPolyline(vertices);
    }

    private void addPolygon(JLMapView map) {

        JLLatLng[][][] vertices = new JLLatLng[2][][];

        vertices[0] = new JLLatLng[2][];
        vertices[1] = new JLLatLng[1][];
        //first part
        vertices[0][0] = new JLLatLng[]{
                new JLLatLng(null, 37, -109.05),
                new JLLatLng(null, 41, -109.03),
                new JLLatLng(null, 41, -102.05),
                new JLLatLng(null, 37, -102.04)
        };
        //hole inside the first part
        vertices[0][1] = new JLLatLng[]{
                new JLLatLng(null, 37.29, -108.58),
                new JLLatLng(null, 40.71, -108.58),
                new JLLatLng(null, 40.71, -102.50),
                new JLLatLng(null, 37.29, -102.50)
        };
        //second part
        vertices[1][0] = new JLLatLng[]{
                new JLLatLng(null, 41, -111.03),
                new JLLatLng(null, 45, -111.04),
                new JLLatLng(null, 45, -104.05),
                new JLLatLng(null, 41, -104.05)
        };
        map.getVectorLayer().addPolygon(vertices).setOnActionListener(new OnJLObjectActionListener<JLPolygon>() {
            @Override
            public void click(JLPolygon jlPolygon, Action action) {
                System.out.println("object click listener for jlPolygon:" + jlPolygon);
            }

            @Override
            public void move(JLPolygon jlPolygon, Action action) {
                System.out.println("object move listener for jlPolygon:" + jlPolygon);
            }
        });
    }


    private AnchorPane createBasePane() {
        AnchorPane inside = new AnchorPane();
        inside.setStyle("-fx-background-color: #555555");
        AnchorPane.setLeftAnchor(inside, (double) JLProperties.NORMAL_MARGIN);
        AnchorPane.setRightAnchor(inside, (double) JLProperties.NORMAL_MARGIN);
        AnchorPane.setTopAnchor(inside, (double) JLProperties.NORMAL_MARGIN);
        AnchorPane.setBottomAnchor(inside, (double) JLProperties.NORMAL_MARGIN);

        return inside;

    }
}