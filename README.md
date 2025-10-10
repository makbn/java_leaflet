# Java Leaflet (JLMAP): A Java Interactive Map For Vaadin and JavaFx

A Java library for integrating [Leaflet](https://github.com/Leaflet/Leaflet) maps into Java applications with full Java Platform Module System (JPMS) support.
Now supporting both [**JavaFX**](https://openjfx.io/) and [**Vaadin**](https://vaadin.com/) implementations with a unified API.

* Current version: **v2.0.0**

Project Source Code: https://github.com/makbn/java_leaflet

Project Wiki: https://github.com/makbn/java_leaflet/wiki

![Java-Leaflet Test](https://github.com/makbn/java_leaflet/blob/master/.github/doc/app.png?raw=true)

## 🏗️ Project Structure

This project is now organized as a multi-module Maven project:

```
java_leaflet/
├── jlmap-parent/          # Parent POM
├── jlmap-api/             # Core API and abstractions
├── jlmap-fx/              # JavaFX implementation
├── jlmap-vaadin/          # Vaadin component implementation
└── jlmap-vaadin-demo/     # Vaadin demo application
```

### Module Overview

- **`jlmap-api`**: Core abstractions, interfaces, and models used by all implementations
- **`jlmap-fx`**: JavaFX-specific implementation using WebView
- **`jlmap-vaadin`**: Vaadin component implementation for web applications
- **`jlmap-vaadin-demo`**: Complete Vaadin demo application showcasing the fluent API

## ✨ Features

- **Multi-Framework Support**: JavaFX and Vaadin implementations
- **Java Platform Module System (JPMS) Compatible**: Fully modularized for Java 17+
- **Unified API**: Consistent interface across different UI frameworks
- **Multiple Map Providers**: Support for OpenStreetMap, Mapnik, and other tile providers with the ability to add custom
  providers
- **Interactive Features**: Markers, polygons, polylines, circles, and more
- **Event Handling**: Comprehensive bi-directional event system for map interactions, receiving events from client and
  sending commands to the map
- **GeoJSON Support**: Load and display GeoJSON data with support for custom styling and filtering
- **Customizable**: Extensive customization options for map appearance and behavior
- **Fluent API**: Builder pattern and method chaining for easy configuration
- **Context Menus**: Support for native context menus on map and objects for both implementations

The goal is to match almost all the native Leaflet features across both implementations while maintaining a clean and
modular architecture.
However, some features may be available at the moment. To see which features are supported in each implementation,
refer to the [Feature Comparison Table](Feature.md).

## 📋 Requirements

- **Java**: 17 or higher
- **Maven**: 3.6+ (for building)
- **JavaFX**: 19.0.2.1 or higher (for JavaFX implementation)
- **Vaadin**: 24 or higher (for Vaadin implementation)

## 🚀 Quick Start

### JavaFX Implementation

Add the JavaFX dependency to your `pom.xml`:

```xml

<dependency>
    <groupId>io.github.makbn</groupId>
    <artifactId>jlmap-fx</artifactId>
    <version>2.0.0</version>
</dependency>
```
- https://central.sonatype.com/artifact/io.github.makbn/jlmap-fx

### Vaadin Implementation

Add the Vaadin dependency to your `pom.xml`:

```xml

<dependency>
    <groupId>io.github.makbn</groupId>
    <artifactId>jlmap-vaadin</artifactId>
    <version>2.0.0</version>
</dependency>
```
- https://central.sonatype.com/artifact/io.github.makbn/jlmap-vaadin

Also rememebr to allow the module in your properties file:

```properties
# For more information https://vaadin.com/docs/latest/flow/integrations/spring/configuration#special-configuration-parameters
vaadin.allowed-packages=io.github.makbn.jlmap.vaadin
```

Read more about Vaadin
configuration [here!](https://vaadin.com/docs/latest/flow/integrations/spring/configuration#configure-the-scanning-of-packages)

## 📖 Usage Examples

### JavaFX Implementation

```java
import io.github.makbn.jlmap.fx.JLMapView;
import io.github.makbn.jlmap.JLProperties;
import io.github.makbn.jlmap.model.JLLatLng;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class JavaFXMapExample extends Application {

    @Override
    public void start(Stage stage) {
        // Create a map view
        JLMapView map = JLMapView.builder()
                .jlMapProvider(JLMapProvider.MAP_TILER.parameter(new JLMapOption.Parameter("key", MAP_API_KEY)).build())
                .startCoordinate(JLLatLng.builder()
                        .lat(51.044)
                        .lng(114.07)
                        .build())
                .showZoomController(true)
                .build();

        // Create the scene
        AnchorPane root = new AnchorPane(map);
        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("Java Leaflet Map (JavaFX)");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

### Vaadin Implementation

```java
import io.github.makbn.jlmap.vaadin.JLMapView;
import io.github.makbn.jlmap.JLProperties;
import io.github.makbn.jlmap.model.JLLatLng;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class VaadinMapExample extends VerticalLayout {

    public VaadinMapExample() {
        setSizeFull();

        // Create a map view
        mapView = JLMapView.builder()
                .jlMapProvider(JLMapProvider.MAP_TILER.parameter(new JLMapOption.Parameter("key", MAP_API_KEY)).build())
                .startCoordinate(new JLLatLng(48.864716, 2.349014)) // Paris
                .showZoomController(false)
                .build();

        add(map);
        expand(map);
    }
}
```

## 🎯 Core API Features

### Map Control

```jshelllanguage
// Change the current coordinate
    mapView.setView(JLLatLng.builder()
            .lng(48.864716)
            .lat(2.349014)
            .build());
    // Map zoom functionalities
    map.

            getControlLayer().setZoom(5);
    map.getControlLayer().zoomIn(2);
    map.getControlLayer().zoomOut(1);
```

### Adding Markers

```jshelllanguage
// Add a marker to the UI layer
    JLMarker marker = map.getUiLayer()
            .addMarker(JLLatLng.builder()
                    .lat(35.63)
                    .lng(51.45)
                    .build(), "Tehran", true);

// Add event listeners
    marker.setOnActionListener((jlMarker, event) -> {
        if (event instanceof ClickEvent) {
            log.info("Marker clicked");
        }
    });

    marker.remove(); // Remove the marker
```

### Adding GeoJSON

```jshelllanguage
import io.github.makbn.jlmap.model.JLGeoJsonOptions;

// Load GeoJSON with custom styling
    JLGeoJsonOptions options = JLGeoJsonOptions.builder()
            .styleFunction(features -> JLOptions.builder()
                    .fill(true)
                    .fillColor(JLColor.fromHex((String) features.get(0).get("fill")))
                    .fillOpacity((Double) features.get(0).get("fill-opacity"))
                    .stroke(true)
                    .color(JLColor.fromHex((String) features.get(0).get("stroke")))
                    .build())
            .build();

    JLGeoJson styledGeoJson = map.getGeoJsonLayer()
            .addFromUrl("https://example.com/data.geojson", options);
```

Read more about examples in
the [Examples and Tutorials](https://github.com/makbn/java_leaflet/wiki/Examples-and-Tutorials) page.

### Layer Management

The API provides access to different map layers:

- **`map.getUiLayer()`**: UI elements like markers, popups
- **`map.getVectorLayer()`**: Vector graphics (polygons, polylines, circles)
- **`map.getControlLayer()`**: Map controls (zoom, pan, bounds)
- **`map.getGeoJsonLayer()`**: GeoJSON data loading and display

## 🏃‍♂️ Running the Demos

### JavaFX Demo

```bash
cd jlmap-fx
mvn javafx:run
```

### Vaadin Demo

```bash
cd jlmap-vaadin-demo
mvn spring-boot:run
```

Then open your browser to `http://localhost:8080`

## 🔧 Building from Source

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Node.js (for Vaadin frontend compilation)

### Build Commands

```bash
# Build all modules
mvn clean install

# Build specific module
mvn clean install -pl jlmap-api
mvn clean install -pl jlmap-fx
mvn clean install -pl jlmap-vaadin

# Run tests
mvn test

# Package
mvn package
```

If you're migrating from version 1.x:

1. **Update Dependencies**: Change from `jlmap` to `jlmap-fx` or `jlmap-vaadin`
2. **Package Updates**: Update imports to use the new module structure
3. **Module Declaration**: Ensure your project has proper module configuration
4. **Build Configuration**: Update Maven configuration for the new dependencies

** [Complete Migration Guide](MIGRATION_GUIDE.md)** - Detailed step-by-step instructions for migrating from v1.x to
v2.0.0

### Example Migration

**Before (v1.x):**

```xml

<dependency>
    <groupId>io.github.makbn</groupId>
    <artifactId>jlmap</artifactId>
    <version>1.9.5</version>
</dependency>
```

**After (v2.0.0):**

```xml
<!-- For JavaFX -->
<dependency>
    <groupId>io.github.makbn</groupId>
    <artifactId>jlmap-fx</artifactId>
    <version>2.0.0</version>
</dependency>

        <!-- For Vaadin -->
<dependency>
<groupId>io.github.makbn</groupId>
<artifactId>jlmap-vaadin</artifactId>
<version>2.0.0</version>
</dependency>
```

## Troubleshooting

### Common Issues

1. **Module Not Found**: Ensure the correct module is in your dependencies
2. **JavaFX Issues**: Verify JavaFX is properly configured for your Java version
3. **Vaadin Issues**: Ensure Node.js is installed for frontend compilation
4. **Lombok Issues**: Verify annotation processing is properly configured

### Module Path Issues

If you encounter module path issues, verify:

```bash
# Check if the module is properly packaged
jar --describe-module --file target/jlmap-fx-2.0.0.jar
jar --describe-module --file target/jlmap-vaadin-2.0.0.jar
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Ensure all tests pass
5. Submit a pull request


## Roadmap

- [X] Multi-module architecture
- [X] Vaadin implementation
- [X] Unified API design
- [X] Enhanced modularity
- [X] Enhanced GeoJSON support
- [X] Better map provider support
- [X] Support receiving events on Map and Objects
- [X] Support calling methods on JLObjects to set or update value on Js side
- [ ] Publish to Vaadin Directory
- [ ] SVG support
- [ ] Animation support
- [ ] implement object specific `JLOptions`
- [ ] Performance optimizations


## License

Since v2.0.0 This project is licensed under the GNU LESSER GENERAL PUBLIC LICENSE Version 2.1 - see
the [LICENSE](LICENSE) file for details.

## Author

**Matt Akbarian** (@makbn)
