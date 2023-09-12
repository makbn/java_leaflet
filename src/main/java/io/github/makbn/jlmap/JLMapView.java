package io.github.makbn.jlmap;

import com.sun.javafx.webkit.WebConsoleListener;
import io.github.makbn.jlmap.layer.*;
import io.github.makbn.jlmap.listener.OnJLMapViewListener;
import io.github.makbn.jlmap.model.JLLatLng;
import io.github.makbn.jlmap.model.JLMapOption;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.scene.effect.GaussianBlur;
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
import lombok.extern.log4j.Log4j2;
import netscape.javascript.JSObject;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
@Log4j2
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class JLMapView extends JLMapController {

    @Builder
    public JLMapView(@NonNull JLProperties.MapType mapType,
                     @NonNull JLLatLng startCoordinate, boolean showZoomController) {
        super(JLMapOption.builder()
                .startCoordinate(startCoordinate)
                .mapType(mapType)
                .additionalParameter(Set.of(new JLMapOption.Parameter("zoomControl",
                        Objects.toString(showZoomController))))
                .build());
        this.webView = new WebView();
        this.jlMapCallbackHandler = new JLMapCallbackHandler(this);
        initialize();
    }

    @Getter
    WebView webView;
    JLMapCallbackHandler jlMapCallbackHandler;
    @NonFinal
    HashMap<Class<? extends JLLayer>, JLLayer> layers;
    @NonFinal
    boolean controllerAdded = false;
    @NonFinal
    @Nullable
    OnJLMapViewListener mapListener;

    private void removeMapBlur() {
        Transition gt = new MapTransition(webView);
        gt.play();
    }

    private void initialize() {

        webView.getEngine().onStatusChangedProperty().addListener((observable, oldValue, newValue)
                -> log.debug(String.format("Old Value: %s\tNew Value: %s", oldValue, newValue)));
        webView.getEngine().onErrorProperty().addListener((observable, oldValue, newValue)
                -> log.debug(String.format("Old Value: %s\tNew Value: %s", oldValue, newValue)));
        webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            checkForBrowsing(webView.getEngine());
            if (newValue == Worker.State.FAILED) {
                log.info("failed to load!");
            } else if (newValue == Worker.State.SUCCEEDED) {
                removeMapBlur();
                webView.getEngine().executeScript("removeNativeAttr()");
                addControllerToDocument();

                if (mapListener != null) {
                    mapListener.mapLoadedSuccessfully(this);
                }

            } else {
                setBlurEffectForMap();
            }
        });

        WebConsoleListener.setDefaultListener((view, message, lineNumber, sourceId)
                -> log.error(String.format("sid: %s ln: %d m:%s", sourceId, lineNumber, message)));

        File index = null;
        try (InputStream in = getClass().getResourceAsStream("/index.html")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(in)));
            index = File.createTempFile("jlmapindex", ".html");
            Files.write(index.toPath(), reader.lines().collect(Collectors.joining("\n")).getBytes());
        } catch (IOException e) {
            log.error(e);
        }

        webView.getEngine()
                .load(String.format("file:%s%s", Objects.requireNonNull(index).getAbsolutePath(), mapOption.toQueryString()));

        setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        getChildren().add(webView);
        customizeWebviewStyles();
    }

    private void checkForBrowsing(WebEngine engine) {
        String address =
                engine.getLoadWorker().getMessage().trim();
        log.debug("link: " + address);
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
                log.debug(e);
            }
        }
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

    @Override
    protected HashMap<Class<? extends JLLayer>, JLLayer> getLayers() {
        if (layers == null) {
            layers = new HashMap<>();
            layers.put(JLUiLayer.class, new JLUiLayer(getWebView().getEngine(), jlMapCallbackHandler));
            layers.put(JLVectorLayer.class, new JLVectorLayer(getWebView().getEngine(), jlMapCallbackHandler));
            layers.put(JLControlLayer.class, new JLControlLayer(getWebView().getEngine(), jlMapCallbackHandler));
            layers.put(JLGeoJsonLayer.class, new JLGeoJsonLayer(getWebView().getEngine(), jlMapCallbackHandler));
        }
        return layers;
    }

    @Override
    protected void addControllerToDocument() {
        JSObject window = (JSObject) webView.getEngine().executeScript("window");
        if (!controllerAdded) {
            window.setMember("app", jlMapCallbackHandler);
            log.debug("controller added to js scripts");
            controllerAdded = true;
        }
        webView.getEngine().setOnError(webErrorEvent -> log.error(webErrorEvent.getMessage()));
    }

    public Optional<OnJLMapViewListener> getMapListener() {
        return Optional.ofNullable(mapListener);
    }

    public void setMapListener(@NonNull OnJLMapViewListener mapListener) {
        this.mapListener = mapListener;
    }
}