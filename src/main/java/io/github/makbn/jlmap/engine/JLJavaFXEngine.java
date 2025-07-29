package io.github.makbn.jlmap.engine;

import javafx.scene.web.WebEngine;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class JLJavaFXEngine extends JLWebEngine {
    WebEngine jfxEngine;

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
