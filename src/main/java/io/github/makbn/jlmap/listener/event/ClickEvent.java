package io.github.makbn.jlmap.listener.event;

import io.github.makbn.jlmap.model.JLLatLng;

public record ClickEvent(JLLatLng center) implements Event {
}
