module io.github.makbn.jlmap {
    requires com.google.gson;
    requires java.datatransfer;
    requires java.desktop;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.web;
    requires jdk.jsobject;
    requires static lombok;
    requires org.jetbrains.annotations;
    requires org.slf4j;

    exports io.github.makbn.jlmap;
    exports io.github.makbn.jlmap.listener;
    exports io.github.makbn.jlmap.engine;
    exports io.github.makbn.jlmap.model;
    exports io.github.makbn.jlmap.geojson;
    exports io.github.makbn.jlmap.layer;
    exports io.github.makbn.jlmap.exception;
}