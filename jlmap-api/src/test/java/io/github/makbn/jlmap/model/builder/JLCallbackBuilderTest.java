package io.github.makbn.jlmap.model.builder;

import io.github.makbn.jlmap.listener.JLAction;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JLCallbackBuilderTest {

    @Test
    void onClick_containsClickEventAndElementInfo() {
        var cb = new JLCallbackBuilder("jlmarker", "marker_1");

        cb.on(JLAction.CLICK);
        var callbacks = cb.build();

        assertThat(callbacks).hasSize(1);
        assertThat(callbacks.get(0))
                .contains("'click'")
                .contains("'jlmarker'")
                .contains("marker_1");
    }

    @Test
    void onAdd_usesAddTemplateWithSetAttribute() {
        var cb = new JLCallbackBuilder("jlmarker", "marker_1");

        cb.on(JLAction.ADD);
        var callbacks = cb.build();

        assertThat(callbacks).hasSize(1);
        assertThat(callbacks.get(0))
                .contains("'add'")
                .contains("getElement().setAttribute");
    }

    @Test
    void onRemove_usesRemoveTemplateWithSetAttribute() {
        var cb = new JLCallbackBuilder("jlmarker", "marker_1");

        cb.on(JLAction.REMOVE);
        var callbacks = cb.build();

        assertThat(callbacks).hasSize(1);
        assertThat(callbacks.get(0))
                .contains("'remove'")
                .contains("getElement().setAttribute");
    }

    @Test
    void onContextMenu_usesContextMenuTemplateWithStopPropagation() {
        var cb = new JLCallbackBuilder("jlmarker", "marker_1");

        cb.on(JLAction.CONTEXT_MENU);
        var callbacks = cb.build();

        assertThat(callbacks).hasSize(1);
        assertThat(callbacks.get(0))
                .contains("'contextmenu'")
                .contains("L.DomEvent.stopPropagation");
    }

    @Test
    void onResize_usesResizeTemplateWithSizeFields() {
        var cb = new JLCallbackBuilder("jlmarker", "marker_1");

        cb.on(JLAction.RESIZE);
        var callbacks = cb.build();

        assertThat(callbacks).hasSize(1);
        assertThat(callbacks.get(0))
                .contains("'resize'")
                .contains("oldSize")
                .contains("newSize");
    }

    @Test
    void chaining_multipleCallbacks_returnsAll() {
        var cb = new JLCallbackBuilder("jlmarker", "marker_1");

        var result = cb.on(JLAction.CLICK).on(JLAction.ADD).build();

        assertThat(result).hasSize(2);
        assertThat(result.get(0)).contains("'click'");
        assertThat(result.get(1)).contains("'add'");
    }
}
