package io.github.makbn.jlmap;

import io.github.makbn.jlmap.listener.OnJLMapViewListener;
import io.github.makbn.jlmap.model.JLLatLng;
import io.github.makbn.jlmap.model.JLOptions;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.*;
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

    @Override
    public void start(Stage stage) {

        stage.initStyle(StageStyle.TRANSPARENT);

        final JLMapView map = JLMapView
                .builder()
                .mapType(JLProperties.MapType.LIGHT)
                .build();
        AnchorPane inside = createBasePane();
        AnchorPane root = new AnchorPane(inside);

        root.setBackground(Background.EMPTY);
        root.setMinHeight(JLProperties.INIT_MIN_HEIGHT_STAGE);
        root.setMinWidth(JLProperties.INIT_MIN_WIDTH_STAGE);


        Scene scene = new Scene(root);
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

        map.setMapListener(new OnJLMapViewListener() {
            @Override
            public void mapLoadedSuccessfully(JLMapView mapView) {
                addMultiPolyline(map);
                addPolyline(map);

                map.setView(JLLatLng.builder().lng(10).lat(10).build());
                map.getUiLayer()
                        .addMarker(JLLatLng.builder().lat(35.63).lng(51.45).name("tehran-pint").build(), "tehran");
                map.getVectorLayer()
                        .addCircleMarker(JLLatLng.builder().lat(35.63).lng(40.45).name("sari-pint").build());
                map.getVectorLayer().addCircle(JLLatLng.builder().lat(35.63).lng(51.45).name("tehran-pint").build(),
                        30000, JLOptions.DEFAULT);
            }

            @Override
            public void mapFailed() {
                log.debug("map failed!");
            }
        });
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