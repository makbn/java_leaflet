package io.github.makbn.jlmap;

import com.sun.javafx.webkit.WebConsoleListener;
import io.github.makbn.jlmap.layer.JLLayer;
import io.github.makbn.jlmap.layer.JLUiLayer;
import io.github.makbn.jlmap.layer.JLVectorLayer;
import io.github.makbn.jlmap.listener.OnJLMapViewListener;
import javafx.animation.Transition;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import lombok.extern.log4j.Log4j2;
import netscape.javascript.JSObject;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

@Log4j2
public class JLMapView extends JLMapController {

    private WebView webView;
    private OnJLMapViewListener mapListener;
    private HashMap<String, JLLayer> layers;
    private boolean controllerAdded = false;
    private JLMapCallbackHandler jlMapCallbackHandler = new JLMapCallbackHandler();
    public JLMapView(OnJLMapViewListener listener){
        this();
        this.setMapListener(listener);
    }

    public JLMapView() {
        webView = new WebView();
        webView.getEngine().onStatusChangedProperty().addListener((observable, oldValue, newValue) -> System.out.println(""));
        webView.getEngine().onErrorProperty().addListener((observable, oldValue, newValue) -> System.out.println(""));
        webView.getEngine().getLoadWorker().stateProperty().addListener(
                (observable, oldValue, newValue) -> {
                    checkForBrowsing(webView.getEngine());
                    if(newValue == Worker.State.FAILED){
                        System.out.println("failed");
                    } else if( newValue == Worker.State.SUCCEEDED) {
                        removeMapBlur();
                        webView.getEngine().executeScript("removeNativeAttr()");
                        addControllerToDocument();
                        mapListener.mapLoadedSuccessfully(this);
                    } else {
                        setBlurEffectForMap();
                    }
                });

        WebConsoleListener.setDefaultListener((webView, message, lineNumber, sourceId)
                -> log.debug(String.format("sid: %s ln: %d m:%s", sourceId, lineNumber, message)));

        webView.getEngine().load(getClass().getResource("/index.html").toString());

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
                if(os.toLowerCase().contains("mac")){
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
            }

            @Override
            protected void interpolate(double frac) {
                GaussianBlur eff = ((GaussianBlur) webView.getEffect());
                eff.setRadius(eff.getRadius() - 0.5);
            }
        };
        gt.play();
    }

    private void setBlurEffectForMap() {
        if(webView.getEffect() == null) {
            GaussianBlur gaussianBlur = new GaussianBlur();
            gaussianBlur.setRadius(10);
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
        if(layers == null){
            layers = new HashMap<>();
            layers.put(JLUiLayer.class.getSimpleName(),new JLUiLayer(getWebView().getEngine()));
            layers.put(JLVectorLayer.class.getSimpleName(),new JLVectorLayer(getWebView().getEngine()));
        }
        return layers;
    }

    @Override
    protected void addControllerToDocument() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSObject window = (JSObject) webView.getEngine().executeScript("window");


        if(!controllerAdded) {
            window.setMember("app", jlMapCallbackHandler);
            String functionName = "jlMapCallbackListener";
            String script = "var fun = " + functionName + " ;"
                    + functionName + " = function() {"
                    + "     console.log('start adding'); "
                    + "     app.functionCalled('" + functionName + "', arguments[0], arguments[1]);"
                    + "     console.log('added');"
                    + "     fun.apply(this, arguments);"
                    + "}";
            System.out.println(script);
            //webView.getEngine().executeScript("jlMapCallbackRegister");
            System.out.println("controller added!");
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