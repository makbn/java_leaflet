package io.github.makbn.jlmap.listener;

import io.github.makbn.jlmap.model.JLObject;


public abstract class OnJLObjectActionListener<T extends JLObject> {

    public abstract void click(T t, Action action);

    public abstract void move(T t, Action action);


    public enum Action {
        /**
         * Fired when the marker is moved via setLatLng or by dragging.
         * Old and new coordinates are included in event arguments as oldLatLng, {{@link io.github.makbn.jlmap.model.JLLatLng}}.
         */
        MOVE, MOVE_START, MOVE_END,
        /**
         * Fired when the user clicks (or taps) the layer.
         */
        CLICK,
        /**
         * Fired when the user double-clicks (or double-taps) the layer.
         */
        DOUBLE_CLICK,
    }
}
