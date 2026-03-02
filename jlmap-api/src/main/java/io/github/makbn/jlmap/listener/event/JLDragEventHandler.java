package io.github.makbn.jlmap.listener.event;

import com.google.gson.Gson;
import io.github.makbn.jlmap.JLMap;
import io.github.makbn.jlmap.listener.JLAction;
import io.github.makbn.jlmap.listener.OnJLActionListener;
import io.github.makbn.jlmap.model.JLBounds;
import io.github.makbn.jlmap.model.JLLatLng;
import io.github.makbn.jlmap.model.JLObject;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author Matt Akbarian  (@makbn)
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JLDragEventHandler implements JLEventHandler<JLObject<?>> {
    /**
     * Fired when the marker is moved via setLatLng or by dragging. Old and new coordinates are included in event arguments as oldLatLng, latlng.
     */
    public static final String FUNCTION_MOVE = "move";
    /**
     * Fired when the marker starts moving (because of dragging).
     */
    public static final String FUNCTION_MOVE_START = "movestart";
    /**
     * Fired when the marker stops moving (because of dragging).
     */
    public static final String FUNCTION_MOVE_END = "moveend";

    /**
     * Fired repeatedly while the user drags the marker.
     */
    public static final String FUNCTION_DRAG = "drag";

    /**
     * Fired when the user starts dragging the marker.
     */
    public static final String FUNCTION_DRAG_START = "dragstart";
    /**
     * Fired when the user stops dragging the marker.
     */
    public static final String FUNCTION_DRAG_END = "dragend";

    public static final Set<String> FUNCTIONS = Set.of(FUNCTION_MOVE, FUNCTION_MOVE_START, FUNCTION_MOVE_END, FUNCTION_DRAG, FUNCTION_DRAG_START, FUNCTION_DRAG_END);

    Gson gson = new Gson();

    @Override
    public void handle(@NonNull JLMap<?> map, @NonNull JLObject<?> source, @NonNull String functionName,
                       OnJLActionListener<JLObject<?>> listener, Object param1, Object param2,
                       Object param3, Object param4, Object param5) {
        switch (functionName) {
            case FUNCTION_MOVE -> listener
                    .onAction(source, getMoveEvent(JLAction.MOVE, param4, param5, param3));
            case FUNCTION_MOVE_START -> listener
                    .onAction(source, getMoveEvent(JLAction.MOVE_START, param4, param5, param3));
            case FUNCTION_MOVE_END -> listener
                    .onAction(source, getMoveEvent(JLAction.MOVE_END, param4, param5, param3));
            case FUNCTION_DRAG -> listener
                    .onAction(source, getDragEvent(JLAction.DRAG, param4, param5, param3));
            case FUNCTION_DRAG_START -> listener
                    .onAction(source, getDragEvent(JLAction.DRAG_START, param4, param5, param3));
            case FUNCTION_DRAG_END -> listener
                    .onAction(source, getDragEvent(JLAction.DRAG_END, param4, param5, param3));

            default -> log.error("{} not implemented!", functionName);
        }
    }

    private @NotNull MoveEvent getMoveEvent(JLAction action, Object param4, Object param5, Object param3) {
        return new MoveEvent(action,
                gson.fromJson(String.valueOf(param4), JLLatLng.class),
                gson.fromJson(String.valueOf(param5), JLBounds.class),
                Double.parseDouble(String.valueOf(param3)));
    }

    private @NotNull DragEvent getDragEvent(JLAction action, Object param4, Object param5, Object param3) {
        return new DragEvent(action,
                gson.fromJson(String.valueOf(param4), JLLatLng.class),
                gson.fromJson(String.valueOf(param5), JLBounds.class),
                Double.parseDouble(String.valueOf(param3)));
    }

    @Override
    public boolean canHandle(@NonNull String functionName) {
        return FUNCTIONS.contains(functionName);
    }
}
