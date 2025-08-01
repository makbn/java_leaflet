package io.github.makbn.jlmap.fx.engine;

import io.github.makbn.jlmap.engine.JLWebEngine;
import javafx.scene.web.WebEngine;
import lombok.NonNull;

import java.util.Optional;

public class JLJavaFXEngine extends JLWebEngine {
    private final WebEngine jfxEngine;

    public JLJavaFXEngine(WebEngine jfxEngine) {
        this.jfxEngine = jfxEngine;
    }

    @Override
    public <T> T executeScript(@NonNull String script, @NonNull Class<T> type) {
        return Optional.ofNullable(jfxEngine.executeScript(script))
                .map(type::cast)
                .orElse(null);
    }

    @Override
    public Status getStatus() {
        return jfxEngine.getLoadWorker().getState().name().equals("SUCCEEDED") ? Status.SUCCEEDED : Status.FAILED;
    }
}
