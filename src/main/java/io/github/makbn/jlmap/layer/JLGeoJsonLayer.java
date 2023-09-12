package io.github.makbn.jlmap.layer;

import io.github.makbn.jlmap.JLMapCallbackHandler;
import io.github.makbn.jlmap.exception.JLException;
import io.github.makbn.jlmap.geojson.JLGeoJsonContent;
import io.github.makbn.jlmap.geojson.JLGeoJsonFile;
import io.github.makbn.jlmap.geojson.JLGeoJsonObject;
import io.github.makbn.jlmap.geojson.JLGeoJsonURL;
import io.github.makbn.jlmap.layer.leaflet.LeafletGeoJsonLayerInt;
import javafx.scene.web.WebEngine;
import lombok.NonNull;
import netscape.javascript.JSObject;

import java.io.File;
import java.util.UUID;

/**
 * Represents the GeoJson (other) layer on Leaflet map.
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
public class JLGeoJsonLayer extends JLLayer implements LeafletGeoJsonLayerInt {
    private static final String MEMBER_PREFIX = "geoJson";
    private static final String WINDOW_OBJ = "window";
    JLGeoJsonURL fromUrl;
    JLGeoJsonFile fromFile;
    JLGeoJsonContent fromContent;
    JSObject window;

    public JLGeoJsonLayer(WebEngine engine,
                          JLMapCallbackHandler callbackHandler) {
        super(engine, callbackHandler);
        this.fromUrl = new JLGeoJsonURL();
        this.fromFile = new JLGeoJsonFile();
        this.fromContent = new JLGeoJsonContent();
        this.window = (JSObject) engine.executeScript(WINDOW_OBJ);
    }

    @Override
    public JLGeoJsonObject addFromFile(@NonNull File file) throws JLException {
        String json = fromFile.load(file);
        return addGeoJson(json);
    }

    @Override
    public JLGeoJsonObject addFromUrl(@NonNull String url) throws JLException {
        String json = fromUrl.load(url);
        return addGeoJson(json);
    }

    @Override
    public JLGeoJsonObject addFromContent(@NonNull String content)
            throws JLException {
        String json = fromContent.load(content);
        return addGeoJson(json);
    }

    @Override
    public boolean removeGeoJson(@NonNull JLGeoJsonObject object) {
        return Boolean.parseBoolean(engine.executeScript(
                String.format("removeGeoJson(%d)", object.getId())).toString());
    }

    private JLGeoJsonObject addGeoJson(String jlGeoJsonObject) {
        try {
            String identifier = MEMBER_PREFIX + UUID.randomUUID();
            this.window.setMember(identifier, jlGeoJsonObject);
            String returnedId = engine.executeScript(
                    String.format("addGeoJson(\"%s\")", identifier)).toString();

            return JLGeoJsonObject.builder()
                    .id(Integer.parseInt(returnedId))
                    .geoJsonContent(jlGeoJsonObject)
                    .build();
        } catch (Exception e) {
            throw new JLException(e);
        }

    }
}
