package io.github.makbn.jlmap.fx.test.internal;

import io.github.makbn.jlmap.fx.internal.JLFxMapRenderer;
import io.github.makbn.jlmap.map.JLMapProvider;
import io.github.makbn.jlmap.model.JLLatLng;
import io.github.makbn.jlmap.model.JLMapOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class JLFxMapRendererTest {

    private JLFxMapRenderer renderer;

    private static Stream<Arguments> mapOptionVariations() {
        return Stream.of(
                Arguments.of(
                        createMapOptionWithCoords(45.0, -120.0, 10, true, JLMapProvider.getDefault()),
                        "45.0", "-120.0", "10", true, JLMapProvider.getDefault().getUrl()
                ),
                Arguments.of(
                        createMapOptionWithCoords(0.0, 0.0, 1, false, JLMapProvider.getDefault()),
                        "0.0", "0.0", "1", false, JLMapProvider.getDefault().getUrl()
                ),
                Arguments.of(
                        createMapOptionWithCoords(-33.8688, 151.2093, 15, true, JLMapProvider.getDefault()),
                        "-33.8688", "151.2093", "15", true, JLMapProvider.getDefault().getUrl()
                )
        );
    }

    private static JLMapOption createDefaultMapOption() {
        return createMapOptionWithCoords(52.5200, 13.4050, 13, true, JLMapProvider.getDefault());
    }

    private static JLMapOption createMapOptionWithCoords(double lat, double lng, int zoom, boolean zoomControl, JLMapProvider provider) {
        return JLMapOption.builder()
                .startCoordinate(JLLatLng.builder().lat(lat).lng(lng).build())
                .jlMapProvider(provider)
                .additionalParameter(Set.of(
                        new JLMapOption.Parameter("zoomControl", String.valueOf(zoomControl)),
                        new JLMapOption.Parameter("initialZoom", String.valueOf(zoom))
                ))
                .build();
    }

    @BeforeEach
    void setUp() {
        renderer = new JLFxMapRenderer();
    }

    @Test
    void render_shouldGenerateValidHtmlDocument() {
        // Given
        JLMapOption option = createDefaultMapOption();

        // When
        String html = renderer.render(option);

        // Then
        assertThat(html).isNotNull().isNotEmpty();
        assertThat(html).contains("<!DOCTYPE html>");
        assertThat(html).contains("<html lang=\"EN\">");
        assertThat(html).contains("<title>JLMap Java - Leaflet</title>");
        assertThat(html).contains("</html>");
    }

    @Test
    void render_shouldIncludeLeafletCssAndJavascript() {
        // Given
        JLMapOption option = createDefaultMapOption();

        // When
        String html = renderer.render(option);

        // Then
        assertThat(html).contains("https://unpkg.com/leaflet@1.9.4/dist/leaflet.css");
        assertThat(html).contains("https://unpkg.com/leaflet@1.9.4/dist/leaflet.js");
        assertThat(html).contains("https://cdn.jsdelivr.net/npm/leaflet-providers@2.0.0/leaflet-providers.min.js");
    }

    @Test
    void render_shouldIncludeLeafletIntegrityAttributes() {
        // Given
        JLMapOption option = createDefaultMapOption();

        // When
        String html = renderer.render(option);

        // Then
        assertThat(html).contains("sha256-p4NxAoJBhIIN+hmNHrzRCf9tD/miZyoHS5obTRR9BMY=");
        assertThat(html).contains("sha256-20nQCchB9co0qIjJZRGuk2/Z9VM+kNiyxNV1lvTlZBo=");
        assertThat(html).contains("crossorigin=\"\"");
    }

    @Test
    void render_shouldIncludeMapContainerDiv() {
        // Given
        JLMapOption option = createDefaultMapOption();

        // When
        String html = renderer.render(option);

        // Then
        assertThat(html).contains("<div id=\"jl-map-view\"");
        assertThat(html).contains("class=\"leaflet-container leaflet-retina\"");
        assertThat(html).contains("width: 100%; min-height: 100vh; height: 100vh; position: relative; background-color: #191a1a;");
    }

    @Test
    void render_shouldIncludeMapHelperFunctions() {
        // Given
        JLMapOption option = createDefaultMapOption();

        // When
        String html = renderer.render(option);

        // Then
        assertThat(html).contains("function getCenterOfElement(event, mapElement)");
        assertThat(html).contains("function getMapBounds(mapElement)");
        assertThat(html).contains("mapElement.getBounds().getNorthEast().lat");
        assertThat(html).contains("mapElement.getBounds().getSouthWest().lng");
    }

    @Test
    void render_shouldIncludeJsRelayFunction() {
        // Given
        JLMapOption option = createDefaultMapOption();

        // When
        String html = renderer.render(option);

        // Then
        assertThat(html).contains("function jlMapServerCallbackDelegate(functionName, param1, param2, param3, param4, param5)");
        assertThat(html).contains("if ('serverCallback' in window)");
        assertThat(html).contains("serverCallback.functionCalled");
    }

    @Test
    void render_shouldIncludeClientToServerEventHandler() {
        // Given
        JLMapOption option = createDefaultMapOption();

        // When
        String html = renderer.render(option);

        // Then
        assertThat(html).contains("function eventHandler(functionType, jlType, uuid, param1, param2, param3)");
        assertThat(html).contains("jlMapServerCallbackDelegate(functionType, jlType, uuid, param1, param2, param3);");
    }

    @Test
    void render_shouldIncludeMapInitializationWithCorrectParameters() {
        // Given
        JLMapOption option = createMapOptionWithCoords(45.0, -120.0, 10, true, JLMapProvider.getDefault());

        // When
        String html = renderer.render(option);

        // Then
        assertThat(html).contains("this.map = L.map(this.jlMapElement, {zoomControl: true})");
        assertThat(html).contains(".setView([45.0, -120.0], 10);");
        assertThat(html).contains("L.tileLayer('" + JLMapProvider.getDefault().getUrl() + "').addTo(this.map);");
    }

    @Test
    void render_shouldIncludeAllMapEventHandlers() {
        // Given
        JLMapOption option = createDefaultMapOption();

        // When
        String html = renderer.render(option);

        // Then
        assertThat(html).contains("this.map.on('click', e => eventHandler('click', 'map', 'main_map'");
        assertThat(html).contains("this.map.on('move', e => eventHandler('move', 'map', 'main_map'");
        assertThat(html).contains("this.map.on('movestart', e => eventHandler('movestart', 'map', 'main_map'");
        assertThat(html).contains("this.map.on('moveend', e => eventHandler('moveend', 'map', 'main_map'");
        assertThat(html).contains("this.map.on('zoom', e => eventHandler('zoom', 'map', 'main_map'");
    }

    @Test
    void render_shouldIncludeMapSetup() {
        // Given
        JLMapOption option = createDefaultMapOption();

        // When
        String html = renderer.render(option);

        // Then
        assertThat(html).contains("this.jlMapElement = document.querySelector('#jl-map-view');");
        assertThat(html).contains("this.outr_eventHandler = eventHandler;");
        assertThat(html).contains("this.map.jlid = 'main_map';");
        assertThat(html).contains("this.jlMapElement.$server = {");
        assertThat(html).contains("eventHandler: (functionType, jlType, uuid, param1, param2, param3)");
    }

    @Test
    void render_shouldSetBodyStyle() {
        // Given
        JLMapOption option = createDefaultMapOption();

        // When
        String html = renderer.render(option);

        // Then
        assertThat(html).contains("margin: 0; background-color: #191a1a;");
    }

    @Test
    void render_shouldIncludeMetaTags() {
        // Given
        JLMapOption option = createDefaultMapOption();

        // When
        String html = renderer.render(option);

        // Then
        assertThat(html).contains("<meta charset=\"utf-8\"");
        assertThat(html).contains("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"");
    }

    @Test
    void render_shouldIncludeL_DISABLE_3D_flag() {
        // Given
        JLMapOption option = createDefaultMapOption();

        // When
        String html = renderer.render(option);

        // Then - L_DISABLE_3D must be present to force 2D CSS transforms in JavaFX WebView
        assertThat(html).contains("window.L_DISABLE_3D = true");
    }

    @Test
    void render_shouldIncludePointerEventRemoval() {
        // Given
        JLMapOption option = createDefaultMapOption();

        // When
        String html = renderer.render(option);

        // Then - PointerEvent must be disabled for JavaFX WebView compatibility
        assertThat(html).contains("window.PointerEvent");
        assertThat(html).contains("PointerEvent = undefined");
    }

    @Test
    void render_shouldPlaceCompatibilityPatchBeforeLeafletScript() {
        // Given
        JLMapOption option = createDefaultMapOption();

        // When
        String html = renderer.render(option);

        // Then - The patch must appear before Leaflet loads so L_DISABLE_3D is checked during init
        int patchPosition = html.indexOf("L_DISABLE_3D");
        int leafletScriptPosition = html.indexOf("leaflet@1.9.4/dist/leaflet.js");
        assertThat(patchPosition)
                .as("L_DISABLE_3D must appear before Leaflet script")
                .isGreaterThan(-1)
                .isLessThan(leafletScriptPosition);
    }

    @Test
    void render_shouldWrapCompatibilityPatchInIIFE() {
        // Given
        JLMapOption option = createDefaultMapOption();

        // When
        String html = renderer.render(option);

        // Then - The patch should be wrapped in an IIFE with try-catch for safety
        int iifeStart = html.indexOf("(function()");
        int disableFlag = html.indexOf("L_DISABLE_3D");
        int iifeEnd = html.indexOf("})();");
        assertThat(iifeStart).isGreaterThan(-1);
        assertThat(disableFlag).isGreaterThan(iifeStart);
        assertThat(iifeEnd).isGreaterThan(disableFlag);
    }
}