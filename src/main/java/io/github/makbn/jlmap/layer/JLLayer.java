package io.github.makbn.jlmap.layer;

import io.github.makbn.jlmap.JLMapCallbackHandler;
import javafx.scene.web.WebEngine;

/**
 * Represents the basic layer.
 * by: Mehdi Akbarian Rastaghi (@makbn)
 */
public abstract class JLLayer {
    protected WebEngine engine;
    protected JLMapCallbackHandler callbackHandler;

    public JLLayer(WebEngine engine, JLMapCallbackHandler callbackHandler) {
        this.engine = engine;
        this.callbackHandler = callbackHandler;
    }

    private JLLayer(){
        //do nothing! just force subclasses to implement upper constructor!
    }
}
