package io.github.makbn.jlmap.listener.event;

import com.google.gson.Gson;
import io.github.makbn.jlmap.JLMap;
import io.github.makbn.jlmap.listener.JLAction;
import io.github.makbn.jlmap.listener.OnJLActionListener;
import io.github.makbn.jlmap.model.JLBounds;
import io.github.makbn.jlmap.model.JLLatLng;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class JLLayerEventHandlerTest {

    private static final Gson GSON = new Gson();

    private static final String BOUNDS_JSON = """
            {"northEast":{"lat":52.0,"lng":14.0},"southWest":{"lat":51.0,"lng":13.0}}""";

    @Mock
    JLMap<?> map;
    @Mock
    OnJLActionListener<Object> listener;

    Object source;
    JLLayerEventHandler handler;

    private static String boundsJson(double swLat, double swLng, double neLat, double neLng) {
        return GSON.toJson(JLBounds.builder()
                .southWest(new JLLatLng(swLat, swLng))
                .northEast(new JLLatLng(neLat, neLng))
                .build());
    }

    @BeforeEach
    void setUp() {
        handler = new JLLayerEventHandler();
        source = new Object();
    }

    @Test
    void canHandle_shouldAcceptAddAndRemove() {
        assertThat(handler.canHandle("add")).isTrue();
        assertThat(handler.canHandle("remove")).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"click", "zoom", "move", "drag", "resize", "unknown", ""})
    void canHandle_shouldRejectUnrelatedFunctions(String functionName) {
        assertThat(handler.canHandle(functionName)).isFalse();
    }

    @Test
    void handle_add_withSinglePointJson_shouldWrapInNestedList() {
        String singlePoint = "{\"lat\":51.5,\"lng\":-0.1}";
        String bounds = boundsJson(51.0, -1.0, 52.0, 0.0);

        handler.handle(map, source, "add", listener, null, null, null, singlePoint, bounds);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener).onAction(eq(source), captor.capture());

        assertThat(captor.getValue()).isInstanceOf(LayerEvent.class);
        LayerEvent event = (LayerEvent) captor.getValue();
        assertThat(event.action()).isEqualTo(JLAction.ADD);
        assertThat(event.latLng()).hasSize(1);
        assertThat(event.latLng().get(0)).hasSize(1);
        assertThat(event.latLng().get(0).get(0).getLat()).isCloseTo(51.5, within(0.0001));
        assertThat(event.latLng().get(0).get(0).getLng()).isCloseTo(-0.1, within(0.0001));
        assertThat(event.bounds()).isNotNull();
    }

    @Test
    void handle_add_withArrayJson_shouldParseNestedLatLngList() {
        String arrayLatLng = "[[{\"lat\":51.5,\"lng\":-0.1},{\"lat\":52.0,\"lng\":0.0}]]";
        String bounds = boundsJson(51.0, -1.0, 53.0, 1.0);

        handler.handle(map, source, "add", listener, null, null, null, arrayLatLng, bounds);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener).onAction(eq(source), captor.capture());

        LayerEvent event = (LayerEvent) captor.getValue();
        assertThat(event.action()).isEqualTo(JLAction.ADD);
        assertThat(event.latLng()).hasSize(1);
        List<JLLatLng> ring = event.latLng().get(0);
        assertThat(ring).hasSize(2);
        assertThat(ring.get(0).getLat()).isCloseTo(51.5, within(0.0001));
        assertThat(ring.get(0).getLng()).isCloseTo(-0.1, within(0.0001));
        assertThat(ring.get(1).getLat()).isCloseTo(52.0, within(0.0001));
        assertThat(ring.get(1).getLng()).isCloseTo(0.0, within(0.0001));
    }

    @Test
    void handle_remove_shouldFireLayerEventWithRemoveAction() {
        String singlePoint = "{\"lat\":48.8566,\"lng\":2.3522}";
        String bounds = boundsJson(48.0, 2.0, 49.0, 3.0);

        handler.handle(map, source, "remove", listener, null, null, null, singlePoint, bounds);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener).onAction(eq(source), captor.capture());

        LayerEvent event = (LayerEvent) captor.getValue();
        assertThat(event.action()).isEqualTo(JLAction.REMOVE);
        assertThat(event.latLng()).hasSize(1);
        assertThat(event.latLng().get(0).get(0).getLat()).isCloseTo(48.8566, within(0.0001));
        assertThat(event.bounds()).isNotNull();
        assertThat(event.bounds().getNorthEast().getLat()).isCloseTo(49.0, within(0.0001));
    }

    @Test
    void handle_add_withInvalidJson_shouldProduceEmptyLatLngList() {
        String invalidJson = "not-valid-json";
        String bounds = boundsJson(0.0, 0.0, 1.0, 1.0);

        handler.handle(map, source, "add", listener, null, null, null, invalidJson, bounds);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener).onAction(any(), captor.capture());

        LayerEvent event = (LayerEvent) captor.getValue();
        assertThat(event.latLng()).isEmpty();
    }

    @Test
    void handle_unknownFunction_shouldNotInvokeListener() {
        handler.handle(map, source, "unknown_event", listener, null, null, null, "{}", "{}");

        verifyNoInteractions(listener);
    }

    @Test
    void handle_add_shouldDeserializeBoundsCorrectly() {
        String singlePoint = "{\"lat\":0.0,\"lng\":0.0}";
        String bounds = """
                {"northEast":{"lat":48.9,"lng":2.5},"southWest":{"lat":48.8,"lng":2.2}}""";

        handler.handle(map, source, "add", listener, null, null, null, singlePoint, bounds);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(listener).onAction(any(), captor.capture());

        LayerEvent event = (LayerEvent) captor.getValue();
        assertThat(event.bounds().getNorthEast().getLat()).isCloseTo(48.9, within(0.0001));
        assertThat(event.bounds().getNorthEast().getLng()).isCloseTo(2.5, within(0.0001));
        assertThat(event.bounds().getSouthWest().getLat()).isCloseTo(48.8, within(0.0001));
        assertThat(event.bounds().getSouthWest().getLng()).isCloseTo(2.2, within(0.0001));
    }
}
