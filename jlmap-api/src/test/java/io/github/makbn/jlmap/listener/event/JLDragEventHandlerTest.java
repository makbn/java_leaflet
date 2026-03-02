package io.github.makbn.jlmap.listener.event;

import com.google.gson.Gson;
import io.github.makbn.jlmap.JLMap;
import io.github.makbn.jlmap.listener.JLAction;
import io.github.makbn.jlmap.listener.OnJLActionListener;
import io.github.makbn.jlmap.model.JLBounds;
import io.github.makbn.jlmap.model.JLLatLng;
import io.github.makbn.jlmap.model.JLObject;
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

@ExtendWith(MockitoExtension.class)
class JLDragEventHandlerTest {

    private static final Gson GSON = new Gson();

    @Mock
    JLMap<?> map;
    @Mock
    JLObject<?> source;
    @Mock
    OnJLActionListener<JLObject<?>> listener;

    JLDragEventHandler handler;

    private static String latLngJson(double lat, double lng) {
        return GSON.toJson(new JLLatLng(lat, lng));
    }

    private static String boundsJson(double swLat, double swLng, double neLat, double neLng) {
        return GSON.toJson(JLBounds.builder()
                .southWest(new JLLatLng(swLat, swLng))
                .northEast(new JLLatLng(neLat, neLng))
                .build());
    }

    @BeforeEach
    void setUp() {
        handler = new JLDragEventHandler();
    }

    @Test
    void canHandle_shouldAcceptAllDragAndMoveFunctions() {
        assertThat(handler.canHandle("move")).isTrue();
        assertThat(handler.canHandle("movestart")).isTrue();
        assertThat(handler.canHandle("moveend")).isTrue();
        assertThat(handler.canHandle("drag")).isTrue();
        assertThat(handler.canHandle("dragstart")).isTrue();
        assertThat(handler.canHandle("dragend")).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"click", "zoom", "resize", "unknown", ""})
    void canHandle_shouldRejectUnrelatedFunctions(String functionName) {
        assertThat(handler.canHandle(functionName)).isFalse();
    }

    @Test
    void handle_move_shouldFireMoveEventWithDoubleZoom() {
        String center = latLngJson(48.8566, 2.3522);
        String bounds = boundsJson(48.0, 2.0, 49.0, 3.0);

        handler.handle(map, source, "move", listener, null, null, "12.5", center, bounds);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener).onAction(eq(source), captor.capture());

        assertThat(captor.getValue()).isInstanceOf(MoveEvent.class);
        MoveEvent event = (MoveEvent) captor.getValue();
        assertThat(event.action()).isEqualTo(JLAction.MOVE);
        assertThat(event.zoomLevel()).isCloseTo(12.5, within(0.001));
        assertThat(event.center().getLat()).isCloseTo(48.8566, within(0.0001));
        assertThat(event.center().getLng()).isCloseTo(2.3522, within(0.0001));
        assertThat(event.bounds()).isNotNull();
    }

    @Test
    void handle_movestart_shouldFireMoveEventWithMoveStartAction() {
        String center = latLngJson(51.5074, -0.1278);
        String bounds = boundsJson(51.0, -1.0, 52.0, 0.0);

        handler.handle(map, source, "movestart", listener, null, null, "7", center, bounds);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener).onAction(eq(source), captor.capture());

        MoveEvent event = (MoveEvent) captor.getValue();
        assertThat(event.action()).isEqualTo(JLAction.MOVE_START);
        assertThat(event.zoomLevel()).isCloseTo(7.0, within(0.001));
    }

    @Test
    void handle_moveend_shouldFireMoveEventWithMoveEndAction() {
        String center = latLngJson(40.7128, -74.006);
        String bounds = boundsJson(40.0, -75.0, 41.0, -73.0);

        handler.handle(map, source, "moveend", listener, null, null, "10", center, bounds);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener).onAction(eq(source), captor.capture());

        MoveEvent event = (MoveEvent) captor.getValue();
        assertThat(event.action()).isEqualTo(JLAction.MOVE_END);
        assertThat(event.zoomLevel()).isCloseTo(10.0, within(0.001));
    }

    @Test
    void handle_drag_shouldFireDragEventWithDoubleZoom() {
        String center = latLngJson(35.6762, 139.6503);
        String bounds = boundsJson(35.0, 139.0, 36.0, 140.0);

        handler.handle(map, source, "drag", listener, null, null, "8.75", center, bounds);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener).onAction(eq(source), captor.capture());

        assertThat(captor.getValue()).isInstanceOf(DragEvent.class);
        DragEvent event = (DragEvent) captor.getValue();
        assertThat(event.action()).isEqualTo(JLAction.DRAG);
        assertThat(event.zoomLevel()).isCloseTo(8.75, within(0.001));
        assertThat(event.center().getLat()).isCloseTo(35.6762, within(0.0001));
    }

    @Test
    void handle_dragstart_shouldFireDragEventWithDragStartAction() {
        String center = latLngJson(-33.8688, 151.2093);
        String bounds = boundsJson(-34.0, 151.0, -33.0, 152.0);

        handler.handle(map, source, "dragstart", listener, null, null, "5", center, bounds);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener).onAction(eq(source), captor.capture());

        DragEvent event = (DragEvent) captor.getValue();
        assertThat(event.action()).isEqualTo(JLAction.DRAG_START);
        assertThat(event.zoomLevel()).isCloseTo(5.0, within(0.001));
    }

    @Test
    void handle_dragend_shouldFireDragEventWithDragEndAction() {
        String center = latLngJson(55.7558, 37.6173);
        String bounds = boundsJson(55.0, 37.0, 56.0, 38.0);

        handler.handle(map, source, "dragend", listener, null, null, "14.333", center, bounds);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener).onAction(eq(source), captor.capture());

        DragEvent event = (DragEvent) captor.getValue();
        assertThat(event.action()).isEqualTo(JLAction.DRAG_END);
        assertThat(event.zoomLevel()).isCloseTo(14.333, within(0.001));
    }

    @Test
    void handle_shouldParseIntegerZoomAsDouble() {
        String center = latLngJson(0.0, 0.0);
        String bounds = boundsJson(-1.0, -1.0, 1.0, 1.0);

        handler.handle(map, source, "move", listener, null, null, "15", center, bounds);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener).onAction(any(), captor.capture());

        MoveEvent event = (MoveEvent) captor.getValue();
        assertThat(event.zoomLevel()).isEqualTo(15.0);
    }

    @Test
    void handle_shouldParseFractionalZoomCorrectly() {
        String center = latLngJson(0.0, 0.0);
        String bounds = boundsJson(-1.0, -1.0, 1.0, 1.0);

        handler.handle(map, source, "drag", listener, null, null, "3.141592", center, bounds);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener).onAction(any(), captor.capture());

        DragEvent event = (DragEvent) captor.getValue();
        assertThat(event.zoomLevel()).isCloseTo(3.141592, within(0.000001));
    }

    @Test
    void handle_shouldCorrectlyDeserializeBoundsWithAlternateFieldNames() {
        String boundsWithAlternateNames = """
                {"northEast":{"lat":52.0,"lng":14.0},"southWest":{"lat":51.0,"lng":13.0}}""";
        String center = latLngJson(51.5, 13.5);

        handler.handle(map, source, "move", listener, null, null, "10", center, boundsWithAlternateNames);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener).onAction(any(), captor.capture());

        MoveEvent event = (MoveEvent) captor.getValue();
        assertThat(event.bounds()).isNotNull();
        assertThat(event.bounds().getNorthEast().getLat()).isCloseTo(52.0, within(0.0001));
        assertThat(event.bounds().getSouthWest().getLng()).isCloseTo(13.0, within(0.0001));
    }

    @Test
    void handle_unknownFunction_shouldNotInvokeListener() {
        handler.handle(map, source, "unknown_event", listener, null, null, "5", "{}", "{}");

        org.mockito.Mockito.verifyNoInteractions(listener);
    }
}
