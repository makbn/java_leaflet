## Java Leaflet
Java wrapper for Leaflet, JavaScript library for mobile-friendly interactive maps.

*  Current version: **v1.9.4**
* Previous version: [v1..6.0](https://github.com/makbn/java_leaflet/tree/release/1.6.0)

![Java-Leaflet Test](https://github.com/makbn/java_leaflet/blob/master/.github/doc/app.png?raw=true)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fmakbn%2Fjava_leaflet.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Fmakbn%2Fjava_leaflet?ref=badge_shield)

> Leaflet is the leading open-source JavaScript library for mobile-friendly interactive maps. Weighing just about 38 KB of JS, it has all the mapping features most > developers ever need.
> Leaflet is designed with simplicity, performance and usability in mind. It works efficiently across all major desktop and mobile platforms, can be extended with > lots of plugins, has a beautiful, easy to use and well-documented API and a simple, readable source code that is a joy to contribute to.


## Getting start

Since you are working on JavaFX application you know you need to have the JavaFX runtime. 
Read more about installing JavaFx:
* https://openjfx.io/openjfx-docs/#introduction

To run the [Leaflet example](src/test/java/io/github/makbn/jlmap/Leaflet.java) class you need to set the module path
as VM options:

```java
--module-path [PATH_TO_JAVA_FX_LIB_FOLDER]
        --add-exports javafx.web/com.sun.javafx.webkit=ALL-UNNAMED
        --add-modules=javafx.graphics,javafx.web
```

There is an issue with JavaFX v17.X javafx.web engine and OSM tiles, I tried JavaFX v19.0.2.1 and it works fine!

First, you need to initialize an instance of `JLMapView`:

```java
final JLMapView map = JLMapView
        .builder()
        .mapType(JLProperties.MapType.OSM_MAPNIK)
        .showZoomController(true)
        .startCoordinate(JLLatLng.builder()
            .lat(51.044)
            .lng(114.07)
            .build())
        .build();

```

Based on Leaflet JS, you can interact with map in different layers. in this project, you can access different functions with this layer:
* `map` for direct changes on map
* `map.getUiLayer()` for changes on UI layer like markers.
* `map.getVectorLayer()` represents the Vector layer on Leaflet map.
* `map.getControlLayer()` represents the control layer for setting the zoom level.
* `map.getGeoJsonLayer()` represents the GeoJson layer.


### Map functions

Some map view functionalities are available in map layer like `setView` or `setMapListener` as a callback for map events:

* Change the current coordinate of map center:

```java
map.setView(JLLatLng.builder()
        .lng(10)
        .lat(10)
        .build());
```

* Add a listener for map events:

```java
map.setMapListener(new OnJLMapViewListener() {
        @Override
        public void mapLoadedSuccessfully(JLMapView mapView) {
            
        }

        @Override
        public void mapFailed() {
            log.error("map failed!");
        }

        @Override
        public void onAction(Event event) {
            if (event instanceof MoveEvent moveEvent) {
                log.info("move event: " + moveEvent.action() + " c:" + moveEvent.center()
                        + " \t bounds:" + moveEvent.bounds() + "\t z:" + moveEvent.zoomLevel());
            } else if (event instanceof ClickEvent clickEvent) {
                log.info("click event: " + clickEvent.center());
                map.getUiLayer().addPopup(clickEvent.center(), "New Click Event!", JLOptions.builder()
                        .closeButton(false)
                        .autoClose(false).build());
            } else if (event instanceof ZoomEvent zoomEvent) {
                log.info("zoom event: " + zoomEvent.zoomLevel());
            }
        }
}
```

Read more about map events from `OnJLMapViewListener.Action`.

### UI Layer

Layer for adding/removing markers and popup. you can access UI layer from `map.getUiLayer()`. As an example:

```java
map.getUiLayer()
    .addMarker(JLLatLng.builder()
                        .lat(35.63)
                        .lng(51.45)
                        .build(), "Tehran", true)
    .setOnActionListener(getListener());
```

Controlling map's zoom level:
```java
// map zoom functionalities
map.getControlLayer().setZoom(5);
map.getControlLayer().zoomIn(2);
map.getControlLayer().zoomOut(1);
```

you can add a listener for some Objects on the map:

```java
marker.setOnActionListener(new OnJLObjectActionListener<JLMarker>() {
       @Override
       public void click(JLMarker object, Action action) {
           System.out.println("object click listener for marker:" + object);
       }

       @Override
       public void move(JLMarker object, Action action) {
           System.out.println("object move listener for marker:" + object);
       }
   });
```


### Vector layer

Represents the Vector layer for Leaflet map. Poly lines, Polygons, and shapes are available in this layer.

```java
map.getVectorLayer()
        .addCircleMarker(JLLatLng.builder()
            .lat(35.63)
            .lng(40.45)
            .build()
        );
``` 

### GeoJson layer
Represents the GeoJson layer for Leaflet map and defines methods for adding and managing
GeoJSON data layers in a Leaflet map.
```java
JLGeoJsonObject geoJsonObject = map.getGeoJsonLayer()
                        .addFromUrl("https://pkgstore.datahub.io/examples/geojson-tutorial/example/data/db696b3bf628d9a273ca9907adcea5c9/example.geojson");

```
You can add GeoJson data from three different sources:
* From a file using `map.getGeoJsonLayer().addFromFile([FILE])`
* From a URL using `map.getGeoJsonLayer().addFromUrl([URL])`
* From a GeoJson content `map.getGeoJsonLayer().addFromContent([CONTENT])`
### Styling

You can pass `JLOptions` to each method for changing the default style! 

```java
 map.getVectorLayer()
        .addCircle(JLLatLng.builder()
            .lat(35.63)
            .lng(51.45)
            .build(), 30000,
            
            JLOptions.builder()
                .color(Color.BLACK)
                .build()
        );
```

For the map itself, you can choose between themes available in `JLProperties.MapType` class. The `JLProperties.MapType.OSM_MAPNIK` is available to be used without any access key but for the rest of them, you need to define your own map using `JLProperties.MapType` and passing proper list of key-values containing all the necessary access keys. 
```java
JLProperties.MapType myMapType = new JLProperties.MapType("HEREv3.terrainDay",
            Set.of(new JLMapOption.Parameter("apiKey", "<insert apiKey here>")));
    
    JLMapView map = JLMapView
            .builder()
            .mapType(myMapType)
            .showZoomController(true)
            .startCoordinate(JLLatLng.builder()
                    .lat(51.044)
                    .lng(114.07)
                    .build())
            .build();
```

Read more:

* https://github.com/leaflet-extras/leaflet-providers!


## TODO

- [X] Adding GeoJson Support
- [ ] Adding better support for Map providers
- [ ] Adding SVG support
- [ ] Adding animation support
- [ ] Separating JS and HTML
- [ ] Publishing package on GitHub

**Disclaimer**: I've implemented this project for one of my academic paper in the area of geo-visualization. So, im not contributing actively! One more thing, I'm not a Javascript developer!




## License
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fmakbn%2Fjava_leaflet.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Fmakbn%2Fjava_leaflet?ref=badge_large)
