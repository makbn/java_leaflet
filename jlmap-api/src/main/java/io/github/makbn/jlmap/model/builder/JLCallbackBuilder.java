package io.github.makbn.jlmap.model.builder;

import io.github.makbn.jlmap.listener.JLAction;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matt Akbarian  (@makbn)
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JLCallbackBuilder {
    List<String> callbacks = new ArrayList<>();
    String varName;
    String elementType;

    public JLCallbackBuilder(String elementType, String varName) {
        this.varName = varName;
        this.elementType = elementType;
    }

    private static @NotNull String getCallbackFunction(JLAction event) {
        //language=JavaScript
        return switch (event) {
            case ADD, REMOVE -> """
                    this.%3$s.on('%1$s', e => {
                        if (typeof e.sourceTarget.getElement === 'function' 
                        && e.sourceTarget.getElement()
                        && typeof  e.sourceTarget.getElement().setAttribute === 'function') {
                            e.sourceTarget.getElement().setAttribute('id', e.target.uuid);
                        }
                        this.jlMapElement.$server.eventHandler('%1$s', '%2$s', e.target.uuid, this.map.getZoom(),
                         JSON.stringify((typeof e.target.getLatLng === "function") ? 
                         e.target.getLatLng() : 
                         (typeof e.target.getLatLngs === "function") ? 
                         e.target.getLatLngs() :
                         {"lat": 0, "lng": 0}),
                         JSON.stringify(this.map.getBounds()));
                    
                    });
                    """;
            case RESIZE -> """
                    this.map.on('%1$s', e => this.jlMapElement.$server.eventHandler('%1$s', '%2$s', this.%3$s.uuid, this.map.getZoom(),
                        JSON.stringify({"oldWidth": e.oldSize.x, "oldHeight": e.oldSize.y, "newWidth": e.newSize.x, "newHeight": e.newSize.y}),
                        JSON.stringify(this.map.getBounds())
                    ));
                    """;
            case CONTEXT_MENU -> """
                    this.%3$s.on('%1$s', e => {
                        this.jlMapElement.$server.eventHandler('%1$s', '%2$s', e.target.uuid, this.map.getZoom(),
                        JSON.stringify({"x": e.containerPoint.x, "y": e.containerPoint.y, "lat": e.latlng.lat, "lng": e.latlng.lng}),
                        JSON.stringify(this.map.getBounds()));
                        L.DomEvent.stopPropagation(e);
                    });
                    """;
            default -> """
                    this.%3$s.on('%1$s', e => this.jlMapElement.$server.eventHandler('%1$s', '%2$s', e.target.uuid, this.map.getZoom(),
                        JSON.stringify((typeof e.target.getLatLng === "function") ? 
                        { "lat": e.target.getLatLng().lat, "lng": e.target.getLatLng().lng } : 
                        {"lat": e.latlng.lat, "lng": e.latlng.lng}),
                        JSON.stringify(this.map.getBounds())
                    ));
                    """;
        };
    }

    public JLCallbackBuilder on(JLAction event) {
        callbacks.add(String.format(getCallbackFunction(event), event.getJsEventName(), elementType, varName));
        return this;
    }

    public List<String> build() {
        return callbacks;
    }
}
