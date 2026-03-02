package io.github.makbn.jlmap.listener.event;

import com.google.gson.Gson;
import io.github.makbn.jlmap.JLMap;
import io.github.makbn.jlmap.listener.JLAction;
import io.github.makbn.jlmap.listener.OnJLActionListener;
import io.github.makbn.jlmap.model.JLBounds;
import io.github.makbn.jlmap.model.JLLatLng;
import io.github.makbn.jlmap.model.JLObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.RecordComponent;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * Regression tests for the int→double zoom level migration across all event records
 * and their corresponding handlers. Guards against accidental reversion to int and
 * verifies fractional zoom values flow through the full event pipeline.
 */
@ExtendWith(MockitoExtension.class)
class ZoomLevelDoubleRegressionTest {

    private static final Gson GSON = new Gson();

    private static final JLLatLng ORIGIN = JLLatLng.builder().lat(0).lng(0).build();
    private static final JLBounds WORLD = JLBounds.builder()
            .southWest(JLLatLng.builder().lat(-90).lng(-180).build())
            .northEast(JLLatLng.builder().lat(90).lng(180).build())
            .build();

    private static final String BOUNDS_JSON = GSON.toJson(WORLD);

    @Nested
    class EventRecordFieldTypes {

        @Test
        void dragEvent_zoomLevel_field_should_be_double() {
            RecordComponent zoomComponent = findComponent(DragEvent.class, "zoomLevel");
            assertThat(zoomComponent).isNotNull();
            assertThat(zoomComponent.getType()).isEqualTo(double.class);
        }

        @Test
        void moveEvent_zoomLevel_field_should_be_double() {
            RecordComponent zoomComponent = findComponent(MoveEvent.class, "zoomLevel");
            assertThat(zoomComponent).isNotNull();
            assertThat(zoomComponent.getType()).isEqualTo(double.class);
        }

        @Test
        void zoomEvent_zoomLevel_field_should_be_double() {
            RecordComponent zoomComponent = findComponent(ZoomEvent.class, "zoomLevel");
            assertThat(zoomComponent).isNotNull();
            assertThat(zoomComponent.getType()).isEqualTo(double.class);
        }

        @Test
        void resizeEvent_zoom_field_should_be_double() {
            RecordComponent zoomComponent = findComponent(ResizeEvent.class, "zoom");
            assertThat(zoomComponent).isNotNull();
            assertThat(zoomComponent.getType()).isEqualTo(double.class);
        }

        private RecordComponent findComponent(Class<? extends Record> recordClass, String name) {
            return Arrays.stream(recordClass.getRecordComponents())
                    .filter(c -> c.getName().equals(name))
                    .findFirst()
                    .orElse(null);
        }
    }

    @Nested
    class DragEventZoomRegression {

        @Test
        void should_accept_fractional_zoom_and_preserve_precision() {
            var event = new DragEvent(JLAction.DRAG, ORIGIN, WORLD, 12.3456789);
            assertThat(event.zoomLevel()).isCloseTo(12.3456789, within(1e-7));
        }

        @Test
        void should_accept_integer_valued_zoom_as_backward_compat() {
            var event = new DragEvent(JLAction.DRAG, ORIGIN, WORLD, 10);
            assertThat(event.zoomLevel()).isEqualTo(10.0);
        }

        @Test
        void should_accept_zero_zoom() {
            var event = new DragEvent(JLAction.DRAG, ORIGIN, WORLD, 0);
            assertThat(event.zoomLevel()).isEqualTo(0.0);
        }

        @Test
        void should_accept_half_zoom_step() {
            var event = new DragEvent(JLAction.DRAG, ORIGIN, WORLD, 0.5);
            assertThat(event.zoomLevel()).isEqualTo(0.5);
        }

        @Test
        void should_preserve_equality_with_same_fractional_zoom() {
            var a = new DragEvent(JLAction.DRAG, ORIGIN, WORLD, 7.25);
            var b = new DragEvent(JLAction.DRAG, ORIGIN, WORLD, 7.25);
            assertThat(a).isEqualTo(b);
            assertThat(a.hashCode()).isEqualTo(b.hashCode());
        }

        @Test
        void should_not_equal_with_different_fractional_zoom() {
            var a = new DragEvent(JLAction.DRAG, ORIGIN, WORLD, 7.25);
            var b = new DragEvent(JLAction.DRAG, ORIGIN, WORLD, 7.26);
            assertThat(a).isNotEqualTo(b);
        }
    }

    @Nested
    class MoveEventZoomRegression {

        @Test
        void should_accept_fractional_zoom_and_preserve_precision() {
            var event = new MoveEvent(JLAction.MOVE, ORIGIN, WORLD, 15.7777);
            assertThat(event.zoomLevel()).isCloseTo(15.7777, within(1e-4));
        }

        @Test
        void should_accept_integer_valued_zoom_as_backward_compat() {
            var event = new MoveEvent(JLAction.MOVE, ORIGIN, WORLD, 18);
            assertThat(event.zoomLevel()).isEqualTo(18.0);
        }

        @Test
        void should_preserve_equality_with_same_fractional_zoom() {
            var a = new MoveEvent(JLAction.MOVE, ORIGIN, WORLD, 3.14);
            var b = new MoveEvent(JLAction.MOVE, ORIGIN, WORLD, 3.14);
            assertThat(a).isEqualTo(b);
        }
    }

    @Nested
    class ZoomEventZoomRegression {

        @Test
        void should_accept_fractional_zoom_and_preserve_precision() {
            var event = new ZoomEvent(JLAction.ZOOM, 9.999, WORLD);
            assertThat(event.zoomLevel()).isCloseTo(9.999, within(1e-3));
        }

        @Test
        void should_accept_integer_valued_zoom_as_backward_compat() {
            var event = new ZoomEvent(JLAction.ZOOM, 5, WORLD);
            assertThat(event.zoomLevel()).isEqualTo(5.0);
        }

        @Test
        void should_distinguish_different_fractional_zoom_levels() {
            var a = new ZoomEvent(JLAction.ZOOM, 10.0, WORLD);
            var b = new ZoomEvent(JLAction.ZOOM, 10.1, WORLD);
            assertThat(a).isNotEqualTo(b);
        }
    }

    @Nested
    class ResizeEventZoomRegression {

        @Test
        void should_accept_fractional_zoom_and_preserve_precision() {
            var event = new ResizeEvent(JLAction.RESIZE, 800, 600, 1024, 768, 13.75);
            assertThat(event.zoom()).isCloseTo(13.75, within(1e-2));
        }

        @Test
        void should_accept_integer_valued_zoom_as_backward_compat() {
            var event = new ResizeEvent(JLAction.RESIZE, 800, 600, 1024, 768, 14);
            assertThat(event.zoom()).isEqualTo(14.0);
        }

        @Test
        void should_preserve_equality_with_same_fractional_zoom() {
            var a = new ResizeEvent(JLAction.RESIZE, 800, 600, 1024, 768, 11.5);
            var b = new ResizeEvent(JLAction.RESIZE, 800, 600, 1024, 768, 11.5);
            assertThat(a).isEqualTo(b);
        }
    }

    @Nested
    class DragHandlerPipelineRegression {

        @Mock
        JLMap<?> map;
        @Mock
        JLObject<?> source;
        @Mock
        OnJLActionListener<JLObject<?>> listener;
        JLDragEventHandler handler;

        @BeforeEach
        void setUp() {
            handler = new JLDragEventHandler();
        }

        @ParameterizedTest(name = "zoom \"{0}\" → {1}")
        @CsvSource({
                "0,       0.0",
                "0.5,     0.5",
                "1,       1.0",
                "10,      10.0",
                "18,      18.0",
                "22,      22.0",
                "13.5,    13.5",
                "7.333,   7.333",
                "3.14159, 3.14159",
                "1.5E1,   15.0"
        })
        void move_should_parse_zoom_correctly(String input, double expected) {
            handler.handle(map, source, "move", listener,
                    null, null, input, GSON.toJson(ORIGIN), BOUNDS_JSON);

            ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
            verify(listener).onAction(any(), captor.capture());

            MoveEvent event = (MoveEvent) captor.getValue();
            assertThat(event.zoomLevel()).isCloseTo(expected, within(1e-5));
        }

        @ParameterizedTest(name = "zoom \"{0}\" → {1}")
        @CsvSource({
                "0,       0.0",
                "0.5,     0.5",
                "8.75,    8.75",
                "22,      22.0",
                "14.333,  14.333"
        })
        void drag_should_parse_zoom_correctly(String input, double expected) {
            handler.handle(map, source, "drag", listener,
                    null, null, input, GSON.toJson(ORIGIN), BOUNDS_JSON);

            ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
            verify(listener).onAction(any(), captor.capture());

            DragEvent event = (DragEvent) captor.getValue();
            assertThat(event.zoomLevel()).isCloseTo(expected, within(1e-5));
        }
    }

    @Nested
    class StatusChangeHandlerPipelineRegression {

        private static final String RESIZE_JSON =
                "{\"newWidth\":800,\"newHeight\":600,\"oldWidth\":1024,\"oldHeight\":768}";
        @Mock
        JLMap<?> map;
        @Mock
        OnJLActionListener<Object> listener;
        Object source = new Object();
        JLStatusChangeEventHandler handler;

        @BeforeEach
        void setUp() {
            handler = new JLStatusChangeEventHandler();
        }

        @ParameterizedTest(name = "zoom \"{0}\" → {1}")
        @CsvSource({
                "0,       0.0",
                "0.5,     0.5",
                "1,       1.0",
                "10,      10.0",
                "18,      18.0",
                "22,      22.0",
                "12.5,    12.5",
                "7.333,   7.333",
                "9.999,   9.999",
                "1.5E1,   15.0"
        })
        void zoom_should_parse_zoom_correctly(String input, double expected) {
            handler.handle(map, source, "zoom", listener,
                    null, null, input, null, BOUNDS_JSON);

            ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
            verify(listener).onAction(any(), captor.capture());

            ZoomEvent event = (ZoomEvent) captor.getValue();
            assertThat(event.zoomLevel()).isCloseTo(expected, within(1e-5));
        }

        @ParameterizedTest(name = "zoom \"{0}\" → {1}")
        @CsvSource({
                "0,       0.0",
                "0.5,     0.5",
                "13.75,   13.75",
                "22,      22.0"
        })
        void resize_should_parse_zoom_correctly(String input, double expected) {
            handler.handle(map, source, "resize", listener,
                    null, null, input, RESIZE_JSON, null);

            ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
            verify(listener).onAction(any(), captor.capture());

            ResizeEvent event = (ResizeEvent) captor.getValue();
            assertThat(event.zoom()).isCloseTo(expected, within(1e-5));
        }

        @ParameterizedTest
        @ValueSource(strings = {"zoom", "zoomstart", "zoomend"})
        void all_zoom_actions_should_handle_fractional_zoom(String functionName) {
            handler.handle(map, source, functionName, listener,
                    null, null, "11.25", null, BOUNDS_JSON);

            ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
            verify(listener).onAction(any(), captor.capture());

            ZoomEvent event = (ZoomEvent) captor.getValue();
            assertThat(event.zoomLevel()).isCloseTo(11.25, within(1e-5));
        }

        @ParameterizedTest
        @ValueSource(strings = {"zoom", "zoomstart", "zoomend"})
        void all_zoom_actions_should_accept_integer_zoom_without_truncation(String functionName) {
            handler.handle(map, source, functionName, listener,
                    null, null, "15", null, BOUNDS_JSON);

            ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
            verify(listener).onAction(any(), captor.capture());

            ZoomEvent event = (ZoomEvent) captor.getValue();
            assertThat(event.zoomLevel()).isEqualTo(15.0);
        }
    }

    @Nested
    class DragHandlerAllActionsRegression {

        @Mock
        JLMap<?> map;
        @Mock
        JLObject<?> source;
        @Mock
        OnJLActionListener<JLObject<?>> listener;
        JLDragEventHandler handler;

        @BeforeEach
        void setUp() {
            handler = new JLDragEventHandler();
        }

        @ParameterizedTest
        @ValueSource(strings = {"move", "movestart", "moveend"})
        void all_move_actions_should_handle_fractional_zoom(String functionName) {
            handler.handle(map, source, functionName, listener,
                    null, null, "6.75", GSON.toJson(ORIGIN), BOUNDS_JSON);

            ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
            verify(listener).onAction(any(), captor.capture());

            MoveEvent event = (MoveEvent) captor.getValue();
            assertThat(event.zoomLevel()).isCloseTo(6.75, within(1e-5));
        }

        @ParameterizedTest
        @ValueSource(strings = {"drag", "dragstart", "dragend"})
        void all_drag_actions_should_handle_fractional_zoom(String functionName) {
            handler.handle(map, source, functionName, listener,
                    null, null, "4.25", GSON.toJson(ORIGIN), BOUNDS_JSON);

            ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
            verify(listener).onAction(any(), captor.capture());

            DragEvent event = (DragEvent) captor.getValue();
            assertThat(event.zoomLevel()).isCloseTo(4.25, within(1e-5));
        }

        @ParameterizedTest
        @ValueSource(strings = {"move", "movestart", "moveend"})
        void all_move_actions_should_accept_integer_zoom_without_truncation(String functionName) {
            handler.handle(map, source, functionName, listener,
                    null, null, "8", GSON.toJson(ORIGIN), BOUNDS_JSON);

            ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
            verify(listener).onAction(any(), captor.capture());

            MoveEvent event = (MoveEvent) captor.getValue();
            assertThat(event.zoomLevel()).isEqualTo(8.0);
        }

        @ParameterizedTest
        @ValueSource(strings = {"drag", "dragstart", "dragend"})
        void all_drag_actions_should_accept_integer_zoom_without_truncation(String functionName) {
            handler.handle(map, source, functionName, listener,
                    null, null, "12", GSON.toJson(ORIGIN), BOUNDS_JSON);

            ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
            verify(listener).onAction(any(), captor.capture());

            DragEvent event = (DragEvent) captor.getValue();
            assertThat(event.zoomLevel()).isEqualTo(12.0);
        }
    }
}
