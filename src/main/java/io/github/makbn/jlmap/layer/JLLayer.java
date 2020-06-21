package io.github.makbn.jlmap.layer;

import javafx.scene.web.WebEngine;

/**
 * Represents the basic layer.
 * by: Mehdi Akbarian Rastaghi (@makbn)
 */
public abstract class JLLayer {
    protected WebEngine engine;

    public JLLayer(WebEngine engine) {
        this.engine = engine;
    }

    private JLLayer(){
        //do nothing! just force subclasses to implement upper constructor!
    }
}
