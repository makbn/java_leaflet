package io.github.makbn.jlmap.layer;

import io.github.makbn.jlmap.JLMapCallbackHandler;
import io.github.makbn.jlmap.engine.JLWebEngine;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Represents the basic layer.
 *
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class JLLayer {
    JLWebEngine engine;
    JLMapCallbackHandler callbackHandler;

    protected JLLayer(JLWebEngine engine, JLMapCallbackHandler callbackHandler) {
        this.engine = engine;
        this.callbackHandler = callbackHandler;
    }
}
