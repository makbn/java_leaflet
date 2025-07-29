# Java Leaflet (JLeaflet)

A Java library for integrating Leaflet maps into JavaFX applications with full Java Platform Module System (JPMS) support.

* Current version: **v1.9.5**

Project Source Code: https://github.com/makbn/java_leaflet

![Java-Leaflet Test](https://github.com/makbn/java_leaflet/blob/master/.github/doc/app.png?raw=true)

> Leaflet is the leading open-source JavaScript library for mobile-friendly interactive maps. Weighing just about 38 KB of JS, it has all the mapping features most > developers ever need.
> Leaflet is designed with simplicity, performance and usability in mind. It works efficiently across all major desktop and mobile platforms, can be extended with > lots of plugins, has a beautiful, easy to use and well-documented API and a simple, readable source code that is a joy to contribute to.

## Features

- **Java Platform Module System (JPMS) Compatible**: Fully modularized for Java 17+
- **JavaFX Integration**: Native JavaFX WebView integration
- **Multiple Map Providers**: Support for OpenStreetMap, Mapnik, and other tile providers
- **Interactive Features**: Markers, polygons, polylines, circles, and more
- **Event Handling**: Comprehensive event system for map interactions
- **GeoJSON Support**: Load and display GeoJSON data
- **Customizable**: Extensive customization options for map appearance and behavior

## Requirements

- **Java**: 17 or higher
- **JavaFX**: 19.0.2.1 or higher
- **Maven**: 3.6+ (for building)

## Module Information

This project is fully modularized using the Java Platform Module System (JPMS). The module name is `io.github.makbn.jlmap`.

### Module Dependencies

The module requires the following dependencies:

- **JavaFX Modules**: `javafx.controls`, `javafx.base`, `javafx.swing`, `javafx.web`, `javafx.graphics`
- **JDK Modules**: `jdk.jsobject`
- **Logging**: `org.apache.logging.log4j`, `org.apache.logging.log4j.core`
- **JSON Processing**: `com.google.gson`, `com.fasterxml.jackson.databind`
- **Annotations**: `org.jetbrains.annotations`, `lombok`

### Module Exports

The following packages are exported for public use:

- `io.github.makbn.jlmap` - Main package
- `io.github.makbn.jlmap.layer` - Layer management
- `io.github.makbn.jlmap.layer.leaflet` - Leaflet-specific layer interfaces
- `io.github.makbn.jlmap.listener` - Event listeners
- `io.github.makbn.jlmap.model` - Data models
- `io.github.makbn.jlmap.exception` - Custom exceptions
- `io.github.makbn.jlmap.geojson` - GeoJSON support

## Installation

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.makbn</groupId>
    <artifactId>jlmap</artifactId>
    <version>1.9.5</version>
</dependency>
```

### Module Path

When running your application, ensure you include the module in your module path:

```bash
java --module-path /path/to/jlmap-1.9.5.jar --add-modules io.github.makbn.jlmap your.main.Class
```

## Quick Start

### Basic Map Setup

```java
import io.github.makbn.jlmap.*;
import io.github.makbn.jlmap.model.JLLatLng;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MapExample extends Application {
    
    @Override
    public void start(Stage stage) {
        // Create a map view
        JLMapView map = JLMapView.builder()
                .mapType(JLProperties.MapType.OSM_MAPNIK)
                .startCoordinate(JLLatLng.builder()
                        .lat(51.044)
                        .lng(114.07)
                        .build())
                .showZoomController(true)
                .build();
        
        // Create the scene
        AnchorPane root = new AnchorPane(map);
        Scene scene = new Scene(root, 800, 600);
        
        stage.setTitle("Java Leaflet Map");
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
```

Based on Leaflet JS, you can interact with map in different layers. in this project, you can access different functions with this layer:

* `map` for direct changes on map
* `map.getUiLayer()` for changes on UI layer like markers.
* `map.getVectorLayer()` represents the Vector layer on Leaflet map.
* `map.getControlLayer()` represents the control layer for setting the zoom level.
* `map.getGeoJsonLayer()` represents the GeoJson layer.

### Adding Markers

```java
// Add a marker to the UI layer
map.getUiLayer()
    .addMarker(JLLatLng.builder()
        .lat(35.63)
        .lng(51.45)
        .build(), "Tehran", true);
```


Controlling map's zoom level and coordinate:
```java
// change the current coordinate
map.setView(JLLatLng.builder()
        .lng(10)
        .lat(10)
        .build());

// map zoom functionalities
map.getControlLayer().setZoom(5);
map.getControlLayer().zoomIn(2);
map.getControlLayer().zoomOut(1);
```

### Adding Shapes

```java
// Add a circle
map.getVectorLayer()
    .addCircle(JLLatLng.builder()
        .lat(35.63)
        .lng(51.45)
        .build(), 
        30000, 
        JLOptions.builder()
                .color(Color.BLACK)
                .build());
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

## Building from Source

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Build Commands

```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package
mvn package

# Install to local repository
mvn install
```

### Module-Aware Building

The project uses Maven's module-aware compilation. The `module-info.java` file defines the module structure and dependencies.


## Migration from Non-Modular Version

If you're migrating from a non-modular version:

1. **Update Dependencies**: Ensure all dependencies are module-compatible
2. **Module Declaration**: Add `module-info.java` to your project
3. **Import Updates**: Update any internal JavaFX imports
4. **Build Configuration**: Update Maven configuration for module support

## Troubleshooting

### Common Issues

1. **Module Not Found**: Ensure the module is in your module path
2. **Internal API Access**: Some JavaFX internal APIs are no longer accessible
3. **Lombok Issues**: Ensure annotation processing is properly configured

### Module Path Issues

If you encounter module path issues, verify:

```bash
# Check if the module is properly packaged
jar --describe-module --file target/jlmap-1.9.5.jar
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Ensure all tests pass
5. Submit a pull request

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.

## Author

**Mehdi Akbarian Rastaghi** (@makbn)

## Changelog

### Version 1.9.5
- **Major**: Upgraded to Java Platform Module System (JPMS)
- **Major**: Updated to Java 17 compatibility
- **Major**: Removed internal JavaFX API dependencies
- **Enhancement**: Improved module structure and encapsulation
- **Enhancement**: Updated Maven configuration for module support
- **Fix**: Resolved Lombok annotation processing in module environment

## TODO

- [X] Adding GeoJson Support
- [ ] Adding better support for Map providers
- [ ] Adding SVG support
- [ ] Adding animation support
- [ ] Separating JS and HTML
- [ ] Publishing package on GitHub


**Disclaimer**: I've implemented this project for one of my academic paper in the area of geo-visualization. So, im not contributing actively! One more thing, I'm not a Javascript developer!

### Previous Versions
See the [GitHub Branches](https://github.com/makbn/java_leaflet/branches) for previous version information.
