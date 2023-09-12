package io.github.makbn.jlmap.listener.event;

import io.github.makbn.jlmap.listener.OnJLMapViewListener;

public record ZoomEvent(OnJLMapViewListener.Action action, int zoomLevel)
        implements Event {
}
