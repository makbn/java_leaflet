package io.github.makbn.jlmap;

import com.sun.javafx.webkit.WebConsoleListener;
import io.github.makbn.jlmap.layer.JLLayer;
import io.github.makbn.jlmap.layer.JLUiLayer;
import io.github.makbn.jlmap.layer.JLVectorLayer;
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
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import netscape.javascript.JSObject;

import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * by: Mehdi Akbarian Rastaghi (@makbn)
 */
@Log4j2
public class JLMapView extends JLMapController {

    private WebView webView;
    private OnJLMapViewListener mapListener;
    private HashMap<String, JLLayer> layers;
    private boolean controllerAdded = false;
    private JLMapCallbackHandler jlMapCallbackHandler;

    @Builder
    private JLMapView(OnJLMapViewListener mapListener, JLProperties.MapType mapType, JLLatLng startCoordinate, String accessToken) {
        super(JLMapOption.builder()
                .startCoordinate(startCoordinate)
                .mapType(mapType)
                .accessToken(accessToken)
                .build());
        this.mapListener = mapListener;
        initialize();
    }

    private void initialize() {
        webView = new WebView();
        webView.getEngine().onStatusChangedProperty().addListener((observable, oldValue, newValue) -> System.out.println());
        webView.getEngine().onErrorProperty().addListener((observable, oldValue, newValue) -> System.out.println());
        webView.getEngine().getLoadWorker().stateProperty().addListener(
                (observable, oldValue, newValue) -> {
                    checkForBrowsing(webView.getEngine());
                    if (newValue == Worker.State.FAILED) {
                        System.out.println("failed");
                    } else if (newValue == Worker.State.SUCCEEDED) {
                        removeMapBlur();
                        webView.getEngine().executeScript("removeNativeAttr()");
                        addControllerToDocument();
                        mapListener.mapLoadedSuccessfully(this);
                    } else {
                        setBlurEffectForMap();
                    }
                });

        WebConsoleListener.setDefaultListener((webView, message, lineNumber, sourceId)
                -> log.error(String.format("sid: %s ln: %d m:%s", sourceId, lineNumber, message)));
        jlMapCallbackHandler = new JLMapCallbackHandler(this);

        InputStream in = getClass().getResourceAsStream("/index.html");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        File index = null;
        try {
            index = File.createTempFile("jlmapindex", ".html");
            Files.write(index.toPath(), reader.lines().collect(Collectors.joining("\n")).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }


        webView.getEngine()
                .load("file:" + index.getAbsolutePath() + getMapOptions());

        setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        getChildren().add(webView);
        customizeWebviewStyles();
    }

    private String getMapOptions() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("?mapid=%s", mapOption.getMapType().name()));
        sb.append(String.format("&access_token=%s", mapOption.getAccessToken()));

        return sb.toString();
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
                }else if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URL(address).toURI());
                }else {
                    Runtime.getRuntime().exec("xdg-open " + address);
                }
            } catch (IOException | URISyntaxException e) {
                log.debug(e);
            }
        }
    }

    private void removeMapBlur() {
        Transition gt = new Transition() {
            {
                setCycleDuration(Duration.millis(2000));
                setInterpolator(Interpolator.EASE_IN);
            }

            @Override
            protected void interpolate(double frac) {
                GaussianBlur eff = ((GaussianBlur) webView.getEffect());
                eff.setRadius(JLProperties.START_ANIMATION_RADIUS * (1 - frac));
            }
        };
        gt.play();
    }

    private void setBlurEffectForMap() {
        if(webView.getEffect() == null) {
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


    public WebView getWebView() {
        return webView;
    }

    @Override
    protected HashMap<String, JLLayer> getLayers() {
        if(layers == null) {
            layers = new HashMap<>();
            layers.put(JLUiLayer.class.getSimpleName(),
                    new JLUiLayer(getWebView().getEngine(), jlMapCallbackHandler));
            layers.put(JLVectorLayer.class.getSimpleName(),
                    new JLVectorLayer(getWebView().getEngine(), jlMapCallbackHandler));
        }
        return layers;
    }

    @Override
    protected void addControllerToDocument() {
        JSObject window = (JSObject) webView.getEngine().executeScript("window");
        if(!controllerAdded) {
            window.setMember("app", jlMapCallbackHandler);
            log.debug("controller added to js scripts");
            controllerAdded = true;
        }
    }

    public OnJLMapViewListener getMapListener() {
        return mapListener;
    }

    public void setMapListener(OnJLMapViewListener mapListener) {
        this.mapListener = mapListener;
    }
}