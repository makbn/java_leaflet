package io.github.makbn.jlmap.layer;

import io.github.makbn.jlmap.JLMapCallbackHandler;
import javafx.scene.web.WebEngine;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * Represents the basic layer.
 *
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class JLLayer {
    WebEngine engine;
    JLMapCallbackHandler callbackHandler;

    protected JLLayer(WebEngine engine, JLMapCallbackHandler callbackHandler) {
        this.engine = engine;
        this.callbackHandler = callbackHandler;
    }

    /**
     * Forces the subclasses to implement
     * {@link #JLLayer(WebEngine, JLMapCallbackHandler)} constructor!
     */
    private JLLayer() {
        // NO-OP
    }
}
