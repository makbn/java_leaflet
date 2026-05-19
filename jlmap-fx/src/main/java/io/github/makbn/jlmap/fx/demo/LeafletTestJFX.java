package io.github.makbn.jlmap.fx.demo;

import io.github.makbn.jlmap.JLProperties;
import io.github.makbn.jlmap.fx.JLMapView;
import io.github.makbn.jlmap.listener.JLAction;
import io.github.makbn.jlmap.listener.OnJLActionListener;
import io.github.makbn.jlmap.listener.event.ClickEvent;
import io.github.makbn.jlmap.listener.event.MapEvent;
import io.github.makbn.jlmap.listener.event.MoveEvent;
import io.github.makbn.jlmap.listener.event.ZoomEvent;
import io.github.makbn.jlmap.map.JLMapProvider;
import io.github.makbn.jlmap.model.*;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matt Akbarian  (@makbn)
 */
@Slf4j
public class LeafletTestJFX extends Application {

    public static final String MAP_API_KEY = "rNGhTaIpQWWH7C6QGKzF";
    public static final List<JLObject<?>> elements = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        //building a new map view
        final JLMapView map = JLMapView
                .builder()
                .jlMapProvider(JLMapProvider.MAP_TILER.parameter(new JLMapOption.Parameter("key", MAP_API_KEY)).build())
                .startCoordinate(new JLLatLng(48.864716, 2.349014)) // Paris
                .showZoomController(true)
                .startCoordinate(JLLatLng.builder()
                        .lat(51.044)
                        .lng(-114.07)
                        .build())
                .build();

        Button deleteButton = new Button("Delete Elements");
        //creating a window
        AnchorPane root = new AnchorPane(map);
        root.getChildren().add(deleteButton);
        AnchorPane.setRightAnchor(deleteButton, 24.0);
        AnchorPane.setBottomAnchor(deleteButton, 24.0);


        root.setBackground(Background.EMPTY);
        root.setMinHeight(JLProperties.INIT_MIN_HEIGHT_STAGE);
        root.setMinWidth(JLProperties.INIT_MIN_WIDTH_STAGE);
        Scene scene = new Scene(root);

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
        map.setOnActionListener((source, event) -> {
            log.info("onActionReceived!: {}", event);
            if (event instanceof MoveEvent moveEvent) {
                log.info("move event: {} c: {} \t bounds: {} \t z: {}", moveEvent.action(), moveEvent.center(),
                        moveEvent.bounds(), moveEvent.zoomLevel());
            } else if (event instanceof ClickEvent clickEvent) {
                log.info("click event: {}", clickEvent.center());
                map.getUiLayer().addPopup(clickEvent.center(), "New Click Event!", JLOptions.builder()
                        .closeButton(false)
                        .autoClose(false).build());
            } else if (event instanceof ZoomEvent zoomEvent) {
                log.info("zoom event: {}", zoomEvent.zoomLevel());
            } else if (event instanceof MapEvent mapEvent && mapEvent.action() == JLAction.MAP_LOADED) {
                loadDemoElement(map);
            }
        });

        deleteButton.setOnAction(event -> {
            elements.stream()
                    .filter(JLObjectBase.class::isInstance)
                    .map(JLObjectBase.class::cast)
                    .forEach(JLObjectBase::remove);
        });
    }

    private void loadDemoElement(JLMapView map) {
        log.info("map loaded!");
        addMultiPolyline(map);
        addPolyline(map);
        addPolygon(map);

        // Add marker with context menu
        JLMarker calgaryMarker = map.getUiLayer()
                .addMarker(JLLatLng.builder()
                        .lat(51.0447)
                        .lng(-114.0719)
                        .build(), "Calgary", true);

        // Add context menu to the marker
        calgaryMarker.addContextMenu()
                .addItem("delete", "Delete Marker", "https://img.icons8.com/material-outlined/24/000000/trash--v1.png")
                .addItem("info", "Show Info", "https://img.icons8.com/material-outlined/24/000000/info--v1.png")
                .setOnMenuItemListener(item -> {
                    log.info("Context menu item selected: {}", item.getText());
                    switch (item.getId()) {
                        case "delete" -> calgaryMarker.remove();
                        case "info" -> map.getUiLayer().addPopup(
                                JLLatLng.builder().lat(51.0447).lng(-114.0719).build(),
                                "Calgary - AB",
                                JLOptions.builder().autoClose(true).build()
                        );
                    }
                });

        calgaryMarker.setOnActionListener(getListener());
        elements.add(calgaryMarker);

        elements.add(map.getVectorLayer()
                .addCircleMarker(JLLatLng.builder()
                        .lat(51.0447)
                        .lng(-114.0719)
                        .build()));

        elements.add(map.getVectorLayer()
                .addCircle(JLLatLng.builder()
                        .lat(35.63)
                        .lng(51.45)
                        .build(), 30000, JLOptions.DEFAULT));

        // JLImageOverlay demo: Eiffel Tower image over Paris
        JLBounds eiffelBounds = JLBounds.builder()
                .southWest(JLLatLng.builder().lat(47.857).lng(3.293).build())
                .northEast(JLLatLng.builder().lat(49.860).lng(1.298).build())
                .build();

        elements.add(map.getUiLayer().addImage(
                eiffelBounds,
                "https://img.favpng.com/1/24/8/eiffel-tower-eiffel-tower-illustrated-landmark-L5szYqrZ_t.jpg",
                JLOptions.DEFAULT
        ));

        // map zoom functionalities
        map.getControlLayer().setZoom(3);
        map.getControlLayer().zoomIn(2);
        map.getControlLayer().zoomOut(3);

        JLGeoJson geoJsonObject = map.getGeoJsonLayer()
                .addFromUrl("https://pkgstore.datahub.io/examples/geojson-tutorial/example/data/db696b3bf628d9a273ca9907adcea5c9/example.geojson", JLGeoJsonOptions.builder()
                        .styleFunction(properties -> JLOptions.builder()
                                .color(JLColor.ORANGE)
                                .weight(2)
                                .fillColor(JLColor.PURPLE)
                                .fillOpacity(0.5)
                                .build())

                        .build());


        elements.add(geoJsonObject);
        geoJsonObject.addContextMenu().addItem("Remove")
                        .setOnMenuItemListener(selectedItem -> {
                            if ("remove".equalsIgnoreCase(selectedItem.getId())) {
                                geoJsonObject.remove();
                            }
                        });

        log.info("geojson loaded! id: {}", geoJsonObject.getJLId());
    }

    private OnJLActionListener<JLMarker> getListener() {
        return (jlMarker, event) -> log.info("object {} event for marker:{}", event.action(), jlMarker);
    }

    private void addMultiPolyline(JLMapView map) {
        JLLatLng[][] verticesT = new JLLatLng[2][];

        verticesT[0] = new JLLatLng[]{
                new JLLatLng(41.509, 20.08),
                new JLLatLng(31.503, -10.06),
                new JLLatLng(21.51, -0.047)
        };

        verticesT[1] = new JLLatLng[]{
                new JLLatLng(51.509, 10.08),
                new JLLatLng(55.503, 15.06),
                new JLLatLng(42.51, 20.047)
        };

        elements.add(map.getVectorLayer().addMultiPolyline(verticesT));
    }

    private void addPolyline(JLMapView map) {
        JLLatLng[] vertices = new JLLatLng[]{
                new JLLatLng(51.509, -0.08),
                new JLLatLng(51.503, -0.06),
                new JLLatLng(51.51, -0.047)
        };

        elements.add(map.getVectorLayer().addPolyline(vertices));
    }

    private void addPolygon(JLMapView map) {

        JLLatLng[][][] vertices = new JLLatLng[2][][];

        vertices[0] = new JLLatLng[2][];
        vertices[1] = new JLLatLng[1][];
        //first part
        vertices[0][0] = new JLLatLng[]{
                new JLLatLng(37, -109.05),
                new JLLatLng(41, -109.03),
                new JLLatLng(41, -102.05),
                new JLLatLng(37, -102.04)
        };
        //hole inside the first part
        vertices[0][1] = new JLLatLng[]{
                new JLLatLng(37.29, -108.58),
                new JLLatLng(40.71, -108.58),
                new JLLatLng(40.71, -102.50),
                new JLLatLng(37.29, -102.50)
        };
        //second part
        vertices[1][0] = new JLLatLng[]{
                new JLLatLng(41, -111.03),
                new JLLatLng(45, -111.04),
                new JLLatLng(45, -104.05),
                new JLLatLng(41, -104.05)
        };
        JLPolygon polygon = map.getVectorLayer().addPolygon(vertices);
        polygon.setOnActionListener((source, event) ->
                log.info("{} event for: {}", event.action(), source));
        elements.add(polygon);
    }
}