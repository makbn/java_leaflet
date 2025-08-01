package io.github.makbn.jlmap.listener;

import io.github.makbn.jlmap.model.JLObject;
import lombok.Getter;


public abstract class OnJLObjectActionListener<T extends JLObject<?>> {

    public abstract void click(T t, Action action);

    public abstract void move(T t, Action action);


    @Getter
    public enum Action {
        /**
         * Fired when the marker is moved via setLatLng or by dragging.
         * Old and new coordinates are included in event arguments as oldLatLng,
         * {@link io.github.makbn.jlmap.model.JLLatLng}.
         */
        MOVE("move"),
        MOVE_START("movestart"),
        MOVE_END("moveend"),
        /**
         * Fired when the user clicks (or taps) the layer.
         */
        CLICK("click"),
        /**
         * Fired when the user zooms.
         */
        ZOOM("zoom");

        final String jsEventName;

        Action(String name) {
            this.jsEventName = name;
        }
    }
}
