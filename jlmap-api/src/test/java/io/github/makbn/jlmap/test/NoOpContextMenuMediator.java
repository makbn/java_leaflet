package io.github.makbn.jlmap.test;

import io.github.makbn.jlmap.JLMap;
import io.github.makbn.jlmap.element.menu.JLContextMenuMediator;
import io.github.makbn.jlmap.model.JLObject;
import lombok.NonNull;

public class NoOpContextMenuMediator implements JLContextMenuMediator {

    @Override
    public <T extends JLObject<T>> void showContextMenu(@NonNull JLMap<?> map, @NonNull JLObject<T> object, double x, double y) {
        // no-op for testing
    }

    @Override
    public <T extends JLObject<T>> void hideContextMenu(@NonNull JLMap<?> map, @NonNull JLObject<T> object) {
        // no-op for testing
    }

    @Override
    public boolean supportsObjectType(@NonNull Class<? extends JLObject<?>> objectType) {
        return true;
    }

    @Override
    public @NonNull String getName() {
        return "NoOp Context Menu Mediator";
    }
}
