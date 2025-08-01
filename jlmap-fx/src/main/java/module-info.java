module io.github.makbn.jlmap.fx {
    // API dependency
    requires io.github.makbn.jlmap.api;

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
    exports io.github.makbn.jlmap.fx;
    exports io.github.makbn.jlmap.fx.demo;

    // Opens for reflection (if needed by frameworks)
    opens io.github.makbn.jlmap to javafx.graphics;
    opens io.github.makbn.jlmap.layer to javafx.graphics;
    opens io.github.makbn.jlmap.model to javafx.graphics;
    opens io.github.makbn.jlmap.geojson to javafx.graphics;
    opens io.github.makbn.jlmap.fx.engine to javafx.graphics;
    opens io.github.makbn.jlmap.fx.demo to javafx.graphics;
    opens io.github.makbn.jlmap.fx to javafx.graphics;
} 