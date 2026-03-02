package io.github.makbn.jlmap.listener.event;

import io.github.makbn.jlmap.JLMap;
import io.github.makbn.jlmap.listener.JLAction;
import io.github.makbn.jlmap.listener.OnJLActionListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class JLStatusChangeEventHandlerTest {

    private static final String BOUNDS_JSON = """
            {"northEast":{"lat":52.0,"lng":14.0},"southWest":{"lat":51.0,"lng":13.0}}""";

    private static final String RESIZE_JSON = """
            {"newWidth":800,"newHeight":600,"oldWidth":1024,"oldHeight":768}""";

    @Mock
    JLMap<?> map;
    @Mock
    OnJLActionListener<Object> listener;

    Object source;
    JLStatusChangeEventHandler handler;

    @BeforeEach
    void setUp() {
        handler = new JLStatusChangeEventHandler();
        source = new Object();
    }

    @Test
    void canHandle_shouldAcceptAllStatusChangeFunctions() {
        assertThat(handler.canHandle("zoom")).isTrue();
        assertThat(handler.canHandle("zoomstart")).isTrue();
        assertThat(handler.canHandle("zoomend")).isTrue();
        assertThat(handler.canHandle("resize")).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"click", "move", "drag", "unknown", ""})
    void canHandle_shouldRejectUnrelatedFunctions(String functionName) {
        assertThat(handler.canHandle(functionName)).isFalse();
    }

    @Test
    void handle_zoom_shouldFireZoomEventWithDoubleZoomLevel() {
        handler.handle(map, source, "zoom", listener,
                null, null, "12.5", null, BOUNDS_JSON);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener).onAction(eq(source), captor.capture());

        assertThat(captor.getValue()).isInstanceOf(ZoomEvent.class);
        ZoomEvent event = (ZoomEvent) captor.getValue();
        assertThat(event.action()).isEqualTo(JLAction.ZOOM);
        assertThat(event.zoomLevel()).isCloseTo(12.5, within(0.001));
        assertThat(event.bounds()).isNotNull();
        assertThat(event.bounds().getNorthEast().getLat()).isCloseTo(52.0, within(0.0001));
    }

    @Test
    void handle_zoomstart_shouldFireZoomEventWithZoomStartAction() {
        handler.handle(map, source, "zoomstart", listener,
                null, null, "5.0", null, BOUNDS_JSON);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener).onAction(eq(source), captor.capture());

        ZoomEvent event = (ZoomEvent) captor.getValue();
        assertThat(event.action()).isEqualTo(JLAction.ZOOM_START);
        assertThat(event.zoomLevel()).isCloseTo(5.0, within(0.001));
    }

    @Test
    void handle_zoomend_shouldFireZoomEventWithZoomEndAction() {
        handler.handle(map, source, "zoomend", listener,
                null, null, "18", null, BOUNDS_JSON);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener).onAction(eq(source), captor.capture());

        ZoomEvent event = (ZoomEvent) captor.getValue();
        assertThat(event.action()).isEqualTo(JLAction.ZOOM_END);
        assertThat(event.zoomLevel()).isCloseTo(18.0, within(0.001));
    }

    @Test
    void handle_zoom_shouldParseFractionalZoomLevel() {
        handler.handle(map, source, "zoom", listener,
                null, null, "7.333", null, BOUNDS_JSON);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener).onAction(any(), captor.capture());

        ZoomEvent event = (ZoomEvent) captor.getValue();
        assertThat(event.zoomLevel()).isCloseTo(7.333, within(0.001));
    }

    @Test
    void handle_zoom_shouldParseIntegerZoomAsDouble() {
        handler.handle(map, source, "zoom", listener,
                null, null, "10", null, BOUNDS_JSON);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener).onAction(any(), captor.capture());

        ZoomEvent event = (ZoomEvent) captor.getValue();
        assertThat(event.zoomLevel()).isEqualTo(10.0);
    }

    @Test
    void handle_resize_shouldFireResizeEventWithCorrectDimensions() {
        handler.handle(map, source, "resize", listener,
                null, null, "13.0", RESIZE_JSON, null);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener).onAction(eq(source), captor.capture());

        assertThat(captor.getValue()).isInstanceOf(ResizeEvent.class);
        ResizeEvent event = (ResizeEvent) captor.getValue();
        assertThat(event.action()).isEqualTo(JLAction.RESIZE);
        assertThat(event.newWidth()).isEqualTo(800);
        assertThat(event.newHeight()).isEqualTo(600);
        assertThat(event.oldWidth()).isEqualTo(1024);
        assertThat(event.oldHeight()).isEqualTo(768);
        assertThat(event.zoom()).isCloseTo(13.0, within(0.001));
    }

    @Test
    void handle_resize_shouldParseDoubleZoomLevel() {
        handler.handle(map, source, "resize", listener,
                null, null, "9.75", RESIZE_JSON, null);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener).onAction(any(), captor.capture());

        ResizeEvent event = (ResizeEvent) captor.getValue();
        assertThat(event.zoom()).isCloseTo(9.75, within(0.001));
    }

    @Test
    void handle_unknownFunction_shouldNotInvokeListener() {
        handler.handle(map, source, "unknown_event", listener,
                null, null, "5", null, null);

        verifyNoInteractions(listener);
    }

    @Test
    void handle_zoom_shouldDeserializeBoundsCorrectly() {
        String bounds = """
                {"northEast":{"lat":48.9,"lng":2.5},"southWest":{"lat":48.8,"lng":2.2}}""";

        handler.handle(map, source, "zoom", listener,
                null, null, "15", null, bounds);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener).onAction(any(), captor.capture());

        ZoomEvent event = (ZoomEvent) captor.getValue();
        assertThat(event.bounds().getNorthEast().getLat()).isCloseTo(48.9, within(0.0001));
        assertThat(event.bounds().getNorthEast().getLng()).isCloseTo(2.5, within(0.0001));
        assertThat(event.bounds().getSouthWest().getLat()).isCloseTo(48.8, within(0.0001));
        assertThat(event.bounds().getSouthWest().getLng()).isCloseTo(2.2, within(0.0001));
    }
}
