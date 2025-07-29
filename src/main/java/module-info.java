module io.github.makbn.jlmap {
    // JavaFX modules
    requires javafx.controls;
    requires javafx.base;
    requires javafx.swing;
    requires javafx.web;
    requires javafx.graphics;

    // JDK modules
    requires jdk.jsobject;

    // Logging
    requires org.slf4j;

    // JSON processing
    requires com.google.gson;
    requires com.fasterxml.jackson.databind;

    // Annotations
    requires static org.jetbrains.annotations;
    requires static lombok;

    // Exports for public API
    exports io.github.makbn.jlmap;
    exports io.github.makbn.jlmap.demo;
    exports io.github.makbn.jlmap.layer;
    exports io.github.makbn.jlmap.layer.leaflet;
    exports io.github.makbn.jlmap.listener;
    exports io.github.makbn.jlmap.model;
    exports io.github.makbn.jlmap.exception;
    exports io.github.makbn.jlmap.geojson;
    exports io.github.makbn.jlmap.engine;

    // Opens for reflection (if needed by frameworks)
    opens io.github.makbn.jlmap to javafx.graphics;
    opens io.github.makbn.jlmap.layer to javafx.graphics;
    opens io.github.makbn.jlmap.model to javafx.graphics;
    opens io.github.makbn.jlmap.geojson to javafx.graphics;
    opens io.github.makbn.jlmap.engine to javafx.graphics;
    opens io.github.makbn.jlmap.demo to javafx.graphics;
} 