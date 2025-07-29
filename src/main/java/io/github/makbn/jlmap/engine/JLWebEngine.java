package io.github.makbn.jlmap.engine;

import lombok.NonNull;

public abstract class JLWebEngine {
    public abstract <T> T executeScript(String script, Class<T> type);

    public abstract Status getStatus();

    public Object executeScript(@NonNull String script) {
        return this.executeScript(script, Object.class);
    }

    public enum Status {
        SUCCEEDED,
        FAILED
    }
}
