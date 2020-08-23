## Java Leaflet
Java wrapper for Leaflet, JavaScript library for mobile-friendly interactive maps.

*  Current version: **v1.6.0**

![Java-Leaflet Test](http://tilin.ir/gnjlRNk)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fmakbn%2Fjava_leaflet.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Fmakbn%2Fjava_leaflet?ref=badge_shield)

> Leaflet is the leading open-source JavaScript library for mobile-friendly interactive maps. Weighing just about 38 KB of JS, it has all the mapping features most > developers ever need.
> Leaflet is designed with simplicity, performance and usability in mind. It works efficiently across all major desktop and mobile platforms, can be extended with > lots of plugins, has a beautiful, easy to use and well-documented API and a simple, readable source code that is a joy to contribute to.


## Getting start

First, you need to initialize an instance of `JLMapView`:

```java
final JLMapView map = JLMapView
        .builder()
        .mapType(JLProperties.MapType.DARK)
        .accessToken(ACCESS_TOKEN)
        .startCoordinate(JLLatLng.builder()
                .lat(43.54)
                .lng(22.54)
                .build())
        .build();

```

Based on Leaflet JS, you can interact with map in different layers. in this project, you can access different functions with this layer:
* `map` for direct changes on map
* `map.getUiLayer()` for changes on UI layer like markers.
* `map.getVectorLayer()` represents the Vector layer on Leaflet map.


### Map functions

Some map view functionalities are available in map layer like `setView` or `setMapListener` as a callback for map events:

* Change the current coordinate of map center:

```java
map.setView(JLLatLng.builder()
        .lng(10)
        .lat(10)
        .build()
        );
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
        public void onMove(Action action, JLLatLng center, JLBounds bounds, int zoomLevel) {
            super.onMove(action, center, bounds, zoomLevel);

            System.out.println("map on move: " + action + " c:" + center + " \t bounds:" + bounds + "\t z:" + zoomLevel);

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

Represents the Vector layer on Leaflet map. Poly lines, Polygons, and shapes are available in this layer.

```java
map.getVectorLayer()
        .addCircleMarker(JLLatLng.builder()
            .lat(35.63)
            .lng(40.45)
            .build()
        );
``` 

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

For the map itself, you can choose between these themes:

* `LIGHT` for mapbox/light-v10
* `DARK` for mapbox/dark-v10
* `OUTDOOR` for mapbox/outdoors-v11
* `SATELLITE` for mapbox/satellite-v9
* `SATELLITE_STREET` for mapbox/satellite-streets-v11
* `STREET` for mapbox/streets-v11


## TODO

- [ ] Adding GeoJson Support
- [ ] Adding SVG support
- [ ] Adding animation support
- [ ] Separating JS and HTML
- [ ] Publishing package on Github

**Disclaimer**: I've implemented this project for one of my academic paper in the area of geo-visualization. So, im not contributing actively! One more thing, I'm not a Javascript developer!




## License
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fmakbn%2Fjava_leaflet.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Fmakbn%2Fjava_leaflet?ref=badge_large)