package io.github.makbn.jlmap;

import com.google.gson.Gson;
import io.github.makbn.jlmap.listener.JLAction;
import io.github.makbn.jlmap.listener.OnJLActionListener;
import io.github.makbn.jlmap.listener.event.Event;
import io.github.makbn.jlmap.listener.event.LayerEvent;
import io.github.makbn.jlmap.listener.event.ZoomEvent;
import io.github.makbn.jlmap.model.JLBounds;
import io.github.makbn.jlmap.model.JLLatLng;
import io.github.makbn.jlmap.model.JLMarker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class JLMapEventHandlerTest {

    private static final Gson GSON = new Gson();

    private static final String BOUNDS_JSON = GSON.toJson(JLBounds.builder()
            .southWest(new JLLatLng(51.0, 13.0))
            .northEast(new JLLatLng(52.0, 14.0))
            .build());

    @Mock
    JLMap<?> map;

    JLMapEventHandler eventHandler;

    @BeforeEach
    void setUp() {
        eventHandler = new JLMapEventHandler();
    }

    @Test
    void addJLObject_singleObject_shouldBeDispatchable() {
        OnJLActionListener<JLMarker> markerListener = mock(OnJLActionListener.class);
        JLMarker marker = JLMarker.builder().id("marker-1").latLng(new JLLatLng(0, 0)).build();
        marker.setOnActionListener(markerListener);

        eventHandler.addJLObject("marker-1", marker);

        String latLng = "{\"lat\":51.5,\"lng\":-0.1}";
        eventHandler.functionCalled(map, "add", "jlmarker", "marker-1", null, latLng, BOUNDS_JSON);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(markerListener).onAction(any(), captor.capture());

        assertThat(captor.getValue()).isInstanceOf(LayerEvent.class);
        LayerEvent event = (LayerEvent) captor.getValue();
        assertThat(event.action()).isEqualTo(JLAction.ADD);
    }

    @Test
    void addJLObject_twoObjectsOfSameType_bothShouldBeStored() {
        OnJLActionListener<JLMarker> listener1 = mock(OnJLActionListener.class);
        OnJLActionListener<JLMarker> listener2 = mock(OnJLActionListener.class);

        JLMarker marker1 = JLMarker.builder().id("m-1").latLng(new JLLatLng(10, 20)).build();
        marker1.setOnActionListener(listener1);

        JLMarker marker2 = JLMarker.builder().id("m-2").latLng(new JLLatLng(30, 40)).build();
        marker2.setOnActionListener(listener2);

        eventHandler.addJLObject("m-1", marker1);
        eventHandler.addJLObject("m-2", marker2);

        String latLng = "{\"lat\":0.0,\"lng\":0.0}";

        eventHandler.functionCalled(map, "add", "jlmarker", "m-1", null, latLng, BOUNDS_JSON);
        verify(listener1).onAction(any(), any());
        verifyNoInteractions(listener2);

        reset(listener1);
        eventHandler.functionCalled(map, "add", "jlmarker", "m-2", null, latLng, BOUNDS_JSON);
        verify(listener2).onAction(any(), any());
        verifyNoInteractions(listener1);
    }

    @Test
    void remove_afterAdd_objectShouldNoLongerBeDispatched() {
        OnJLActionListener<JLMarker> markerListener = mock(OnJLActionListener.class);
        JLMarker marker = JLMarker.builder().id("marker-r").latLng(new JLLatLng(0, 0)).build();
        marker.setOnActionListener(markerListener);

        eventHandler.addJLObject("marker-r", marker);
        eventHandler.remove(JLMarker.class, "marker-r");

        String latLng = "{\"lat\":0.0,\"lng\":0.0}";
        eventHandler.functionCalled(map, "add", "jlmarker", "marker-r", null, latLng, BOUNDS_JSON);

        verifyNoInteractions(markerListener);
    }

    @Test
    void remove_onNonExistentClass_shouldNotThrow() {
        assertThatCode(() -> eventHandler.remove(JLMarker.class, "nonexistent"))
                .doesNotThrowAnyException();
    }

    @Test
    void functionCalled_mapLevelZoomEvent_shouldDispatchToMapListener() {
        OnJLActionListener mapListener = mock(OnJLActionListener.class);
        when(map.getOnActionListener()).thenReturn(mapListener);

        eventHandler.functionCalled(map, "zoom", "map", "main_map", "12.5", null, BOUNDS_JSON);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(mapListener).onAction(any(), captor.capture());

        assertThat(captor.getValue()).isInstanceOf(ZoomEvent.class);
        ZoomEvent event = (ZoomEvent) captor.getValue();
        assertThat(event.action()).isEqualTo(JLAction.ZOOM);
        assertThat(event.zoomLevel()).isCloseTo(12.5, within(0.001));
    }

    @Test
    void functionCalled_objectLevelEvent_shouldDispatchToObjectListener() {
        OnJLActionListener<JLMarker> markerListener = mock(OnJLActionListener.class);
        JLMarker marker = JLMarker.builder().id("obj-1").latLng(new JLLatLng(48.8566, 2.3522)).build();
        marker.setOnActionListener(markerListener);

        eventHandler.addJLObject("obj-1", marker);

        String latLng = "{\"lat\":48.8566,\"lng\":2.3522}";
        String bounds = GSON.toJson(JLBounds.builder()
                .southWest(new JLLatLng(48.0, 2.0))
                .northEast(new JLLatLng(49.0, 3.0))
                .build());

        eventHandler.functionCalled(map, "remove", "jlmarker", "obj-1", null, latLng, bounds);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(markerListener).onAction(any(), captor.capture());

        assertThat(captor.getValue()).isInstanceOf(LayerEvent.class);
        LayerEvent event = (LayerEvent) captor.getValue();
        assertThat(event.action()).isEqualTo(JLAction.REMOVE);
        assertThat(event.latLng()).isNotEmpty();
    }

    @Test
    void functionCalled_unknownType_shouldNotThrow() {
        assertThatCode(() ->
                eventHandler.functionCalled(map, "zoom", "unknowntype", "some-uuid", "10", null, BOUNDS_JSON))
                .doesNotThrowAnyException();
    }
}
