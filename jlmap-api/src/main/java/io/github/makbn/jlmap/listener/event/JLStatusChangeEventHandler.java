package io.github.makbn.jlmap.listener.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.makbn.jlmap.JLMap;
import io.github.makbn.jlmap.listener.JLAction;
import io.github.makbn.jlmap.listener.OnJLActionListener;
import io.github.makbn.jlmap.model.JLBounds;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * Handles status change events for the map, such as zoom and resize actions.
 * <p>
 * This event handler listens for map status changes and dispatches corresponding events:
 *
 * @author Matt Akbarian  (@makbn)
 * @see ZoomEvent
 * @see ResizeEvent
 */
@Slf4j
public class JLStatusChangeEventHandler implements JLEventHandler<Object> {
    public static final String FUNCTION_ZOOM = "zoom";
    public static final String FUNCTION_ZOOM_START = "zoomstart";
    public static final String FUNCTION_ZOOM_END = "zoomend";
    public static final String FUNCTION_RESIZE = "resize";
    public static final Set<String> FUNCTIONS = Set.of(FUNCTION_ZOOM, FUNCTION_ZOOM_START, FUNCTION_ZOOM_END,
            FUNCTION_RESIZE);

    Gson gson = new Gson();

    @Override
    public void handle(@NonNull JLMap<?> map, @NonNull Object source, @NonNull String functionName, OnJLActionListener<Object> listener, Object param1, Object param2, Object param3, Object param4, Object param5) {
        switch (functionName) {
            case FUNCTION_ZOOM -> listener
                    .onAction(source, new ZoomEvent(JLAction.ZOOM, gson.fromJson(String.valueOf(param3), Double.class), gson.fromJson(String.valueOf(param5), JLBounds.class)));
            case FUNCTION_ZOOM_START -> listener
                    .onAction(source, new ZoomEvent(JLAction.ZOOM_START, gson.fromJson(String.valueOf(param3), Double.class), gson.fromJson(String.valueOf(param5), JLBounds.class)));
            case FUNCTION_ZOOM_END -> listener
                    .onAction(source, new ZoomEvent(JLAction.ZOOM_END, gson.fromJson(String.valueOf(param3), Double.class), gson.fromJson(String.valueOf(param5), JLBounds.class)));
            case FUNCTION_RESIZE -> listener
                    .onAction(source, new ResizeEvent(JLAction.RESIZE,
                            getDimension(param4, false, "Width"),
                            getDimension(param4, false, "Height"),
                            getDimension(param4, true, "Width"),
                            getDimension(param4, true, "Height"),
                            gson.fromJson(String.valueOf(param3), Double.class)));
            default -> log.error("{} not implemented!", functionName);
        }
    }

    /**
     * Extracts the dimension value from the JSON object for new or old width/height.
     *
     * @param json      the JSON object as string
     * @param forOld    true for old dimension, false for new
     * @param dimension "Width" or "Height"
     * @return the dimension value as int
     */
    private int getDimension(Object json, boolean forOld, String dimension) {
        String field = (forOld ? "old" : "new") + dimension;
        JsonElement dimensionElement = JsonParser.parseString(String.valueOf(json));
        return dimensionElement.getAsJsonObject().get(field).getAsInt();
    }

    @Override
    public boolean canHandle(@NonNull String functionName) {
        return FUNCTIONS.contains(functionName);
    }
}
