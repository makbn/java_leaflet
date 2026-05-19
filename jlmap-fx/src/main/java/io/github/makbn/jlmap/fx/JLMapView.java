package io.github.makbn.jlmap.fx;

import io.github.makbn.jlmap.JLMap;
import io.github.makbn.jlmap.JLMapEventHandler;
import io.github.makbn.jlmap.JLProperties;
import io.github.makbn.jlmap.element.menu.JLContextMenu;
import io.github.makbn.jlmap.engine.JLWebEngine;
import io.github.makbn.jlmap.fx.engine.JLJavaFXEngine;
import io.github.makbn.jlmap.fx.internal.JLFxMapRenderer;
import io.github.makbn.jlmap.fx.layer.JLControlLayer;
import io.github.makbn.jlmap.fx.layer.JLGeoJsonLayer;
import io.github.makbn.jlmap.fx.layer.JLUiLayer;
import io.github.makbn.jlmap.fx.layer.JLVectorLayer;
import io.github.makbn.jlmap.layer.leaflet.LeafletLayer;
import io.github.makbn.jlmap.listener.JLAction;
import io.github.makbn.jlmap.listener.OnJLActionListener;
import io.github.makbn.jlmap.listener.event.MapEvent;
import io.github.makbn.jlmap.map.JLMapProvider;
import io.github.makbn.jlmap.model.JLLatLng;
import io.github.makbn.jlmap.model.JLMapOption;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import netscape.javascript.JSObject;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

/**
 * @author Matt Akbarian  (@makbn)
 */
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class JLMapView extends AnchorPane implements JLMap<Object> {
    JLMapOption mapOption;
    JLWebEngine<Object> jlWebEngine;
    @Getter
    WebView webView;
    JLMapEventHandler jlMapCallbackHandler;
    HashMap<Class<? extends LeafletLayer>, LeafletLayer> layers;

    @NonFinal
    boolean controllerAdded = false;
    @NonFinal
    @Nullable
    OnJLActionListener<JLMap<Object>> mapListener;

    @Builder
    public JLMapView(@NonNull JLMapProvider jlMapProvider,
                     @NonNull JLLatLng startCoordinate, boolean showZoomController) {
        super();
        this.mapOption = JLMapOption.builder()
                .startCoordinate(startCoordinate)
                .jlMapProvider(jlMapProvider)
                .additionalParameter(Set.of(new JLMapOption.Parameter("zoomControl",
                        Objects.toString(showZoomController))))
                .build();
        this.layers = new HashMap<>();
        this.webView = new WebView();
        this.webView.setCache(false);
        this.jlWebEngine = new JLJavaFXEngine(webView.getEngine());
        this.jlMapCallbackHandler = new JLMapEventHandler();
        initialize();
    }

    private void removeMapBlur() {
        Transition gt = new MapTransition(webView);
        gt.play();
    }

    private void initialize() {

        webView.getEngine().onStatusChangedProperty().addListener((observable, oldValue, newValue)
                -> log.debug("Old Value: {} New Value: {}", oldValue, newValue));
        webView.getEngine().onErrorProperty().addListener((observable, oldValue, newValue)
                -> log.debug("Old Value: {} New Value: {}}", oldValue, newValue));
        webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            checkForBrowsing(webView.getEngine());
            if (newValue == Worker.State.FAILED) {
                log.info("failed to load!");
            } else if (newValue == Worker.State.SUCCEEDED) {
                removeMapBlur();
                addControllerToDocument();
                webView.getEngine().setOnError(webErrorEvent -> log.error(webErrorEvent.getMessage()));
                webView.getEngine().setOnAlert(webErrorEvent -> log.error(webErrorEvent.getData()));

                if (mapListener != null) {
                    mapListener.onAction(this, new MapEvent(JLAction.MAP_LOADED));
                }

            } else {
                setBlurEffectForMap();
            }
        });

        // Note: WebConsoleListener is an internal JavaFX API and not available in the module system
        // Web console logging is disabled for module compatibility
        webView.getEngine().getLoadWorker().exceptionProperty().addListener((observableValue, throwable, t1) ->
                log.error("observable value: {}, exception: {}", observableValue, t1.toString()));

        File index = null;
        try {
            index = File.createTempFile("jlmapindex", ".html");
            index.deleteOnExit();
            Files.write(index.toPath(), new JLFxMapRenderer().render(mapOption).getBytes());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        webView.getEngine()
                .load(String.format("file:%s", Objects.requireNonNull(index).getAbsolutePath()));

        setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        getChildren().add(webView);
        customizeWebviewStyles();
    }

    private void checkForBrowsing(WebEngine engine) {
        String address =
                engine.getLoadWorker().getMessage().trim();
        log.debug("link: {}", address);
        if (address.contains("http://") || address.contains("https://")) {
            engine.getLoadWorker().cancel();
            try {
                String os = System.getProperty("os.name", "generic");
                if (os.toLowerCase().contains("mac")) {
                    Runtime.getRuntime().exec("open " + address);
                } else if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URL(address).toURI());
                } else {
                    Runtime.getRuntime().exec("xdg-open " + address);
                }
            } catch (IOException | URISyntaxException e) {
                log.debug(e.getMessage(), e);
            }
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public OnJLActionListener<JLMap<Object>> getOnActionListener() {
        return mapListener;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setOnActionListener(OnJLActionListener<JLMap<Object>> listener) {
        this.mapListener = listener;
    }

    @Override
    public @NonNull JLContextMenu<JLMap<Object>> addContextMenu() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public @NonNull JLContextMenu<JLMap<Object>> getContextMenu() {
        return null;
    }

    @Override
    public void setContextMenu(@NonNull JLContextMenu<JLMap<Object>> contextMenu) {

    }

    @Override
    public boolean hasContextMenu() {
        return false;
    }

    @Override
    public boolean isContextMenuEnabled() {
        return false;
    }

    @Override
    public void setContextMenuEnabled(boolean enabled) {

    }

    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    private static class MapTransition extends Transition {
        WebView webView;

        public MapTransition(WebView webView) {
            this.webView = webView;
            setCycleDuration(Duration.millis(2000));
            setInterpolator(Interpolator.EASE_IN);
        }

        @Override
        protected void interpolate(double frac) {
            GaussianBlur eff = ((GaussianBlur) webView.getEffect());
            eff.setRadius(JLProperties.START_ANIMATION_RADIUS * (1 - frac));
        }
    }

    private void setBlurEffectForMap() {
        if (webView.getEffect() == null) {
            GaussianBlur gaussianBlur = new GaussianBlur();
            gaussianBlur.setRadius(JLProperties.START_ANIMATION_RADIUS);
            webView.setEffect(gaussianBlur);
        }
    }

    private void customizeWebviewStyles() {
        setLeftAnchor(webView, 0.0);
        setRightAnchor(webView, 0.0);
        setTopAnchor(webView, 0.0);
        setBottomAnchor(webView, 0.0);

        setLeftAnchor(this, 0.5);
        setRightAnchor(this, 0.5);
        setTopAnchor(this, 0.5);
        setBottomAnchor(this, 0.5);
    }

    /**
     * @inheritDoc
     */
    @Override
    public HashMap<Class<? extends LeafletLayer>, LeafletLayer> getLayers() {
        layers.clear();
        layers.put(JLUiLayer.class, new JLUiLayer(jlWebEngine, jlMapCallbackHandler));
        layers.put(JLVectorLayer.class, new JLVectorLayer(jlWebEngine, jlMapCallbackHandler));
        layers.put(JLControlLayer.class, new JLControlLayer(jlWebEngine, jlMapCallbackHandler));
        layers.put(JLGeoJsonLayer.class, new JLGeoJsonLayer(jlWebEngine, jlMapCallbackHandler));
        return layers;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void addControllerToDocument() {
        JSObject window = (JSObject) webView.getEngine().executeScript("window");
        if (!controllerAdded) {
            // passing this to javascript is a security concern, should be reviewed later
            window.setMember("serverCallback", this);
            log.debug("controller added to js scripts");
            controllerAdded = true;
        }
    }

    @SuppressWarnings("unused")
    public void functionCalled(String functionName, String jlType, String uuid,
                               String param1, String param2, String param3) {
        jlMapCallbackHandler.functionCalled(this, functionName, jlType, uuid, param1, param2, param3);
    }

    /**
     * @inheritDoc
     */
    @Override
    public JLWebEngine<Object> getJLEngine() {
        return jlWebEngine;
    }


}