package io.github.makbn.jlmap;

import io.github.makbn.jlmap.listener.OnJLMapViewListener;
import io.github.makbn.jlmap.model.JLLatLng;
import io.github.makbn.jlmap.model.JLMarker;
import io.github.makbn.jlmap.model.JLOptions;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;



public class Leaflet extends Application {

    @Override
    public void start(Stage stage) {

       stage.initStyle(StageStyle.TRANSPARENT);

        final JLMapView map = new JLMapView();

       // BorderPane root = createBasePane();
        ListView<JLLatLng> listView = getAddressListView(map);

        AnchorPane inside = createBasePane();

        AnchorPane root = new AnchorPane(inside);
        //#191a1a
        root.setBackground(Background.EMPTY);
       // root.setStyle("-fx-background-color: #191a1a");


        root.setMinHeight(Properties.INIT_MIN_HEIGHT_STAGE);
        root.setMinWidth(Properties.INIT_MIN_WIDTH_STAGE);


        Scene scene = new Scene(root);

        inside.getChildren().add(map);
        inside.getChildren().add(listView);

        stage.setMinHeight(Properties.INIT_MIN_HEIGHT_STAGE);
        stage.setMinWidth(Properties.INIT_MIN_WIDTH_STAGE);


        scene.setFill(Color.TRANSPARENT);


        stage.setTitle("JavaFX and DukeScript");
        stage.setScene(scene);
        stage.show();

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY(100);

        System.out.println("map w: "+ map.getWidth() + " h: "+ map.getHeight());
        System.out.println("inside w: "+ inside.getWidth() + " h: "+ inside.getHeight());
        System.out.println("root w: "+ root.getWidth() + " h: "+ root.getHeight());

        map.setMapListener(new OnJLMapViewListener() {
            @Override
            public void mapLoadedSuccessfully(JLMapView mapView) {


                JLLatLng[] vertices = new JLLatLng[]{
                        new JLLatLng("v1", 51.509, -0.08),
                        new JLLatLng("v2", 51.503, -0.06),
                        new JLLatLng("v3", 51.51, -0.047)
                };

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


                map.getVectorLayer().addPolyline(vertices);
                map.getVectorLayer().addMultiPolyline(verticesT);

                map.setView(JLLatLng.builder()
                        .lng(10)
                        .lat(10)
                        .name("test")
                        .build());
                JLMarker jlMarker = map.getUiLayer()
                        .addMarker(JLLatLng.builder().lat(35.63).lng(51.45).name("tehran-pint").build(), "tehran");
                System.out.println(jlMarker);

                map.getVectorLayer().addCircle(JLLatLng.builder().lat(35.63).lng(51.45).name("tehran-pint").build(),
                        30000, JLOptions.DEFAULT);
            }

            @Override
            public void mapFailed() {

            }
        });



       /* System.out.println(map.getUiLayer()
                .removeMarker(jlMarker.getId()));*/
    }

    private ListView<JLLatLng> getAddressListView(JLMapView map) {
        // a regular JavaFX ListView
        ListView<JLLatLng> listView = new ListView<>();
        listView.getItems().addAll(new JLLatLng("Toni", 48.1322840, 11.5361690),
                new JLLatLng("Jarda", 50.0284060, 34.4934400),
                new JLLatLng("JUG Münster", 11.94906770000001, 7.613701100000071));
        // we listen for the selected item and update the map accordingly
        // as a demo of how to interact between JavaFX and DukeScript
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<JLLatLng>() {
            @Override
            public void changed(ObservableValue<? extends JLLatLng> observable, JLLatLng oldValue, JLLatLng newValue) {
                int d = (int) (newValue.distanceTo(oldValue) / 1000000);
                System.out.println(d);
                map.setView(newValue, d);
            }
        });

        return listView;
    }

    private AnchorPane createBasePane() {
        AnchorPane inside = new AnchorPane();
        inside.setStyle("-fx-background-color: #555555");
        AnchorPane.setLeftAnchor(inside, (double) Properties.NORMAL_MARGIN);
        AnchorPane.setRightAnchor(inside, (double) Properties.NORMAL_MARGIN);
        AnchorPane.setTopAnchor(inside, (double) Properties.NORMAL_MARGIN);
        AnchorPane.setBottomAnchor(inside, (double) Properties.NORMAL_MARGIN);

        return inside;

    }

    private static class Address {

        private final String name;
        private final double lat;
        private final double lng;

        public Address(String name, double lat, double lng) {
            this.name = name;
            this.lat = lat;
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }

        @Override
        public String toString() {
            return name;
        }



    }

}