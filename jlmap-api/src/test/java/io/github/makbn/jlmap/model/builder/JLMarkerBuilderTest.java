package io.github.makbn.jlmap.model.builder;

import io.github.makbn.jlmap.listener.JLAction;
import io.github.makbn.jlmap.model.JLOptions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JLMarkerBuilderTest {

    @Test
    void buildJsElement_containsMarkerCreationAndUuid() {
        var builder = new JLMarkerBuilder()
                .setUuid("test-marker-1")
                .setLat(48.8566)
                .setLng(2.3522)
                .setText("Paris");

        var js = builder.buildJsElement().trim().replaceAll("( +|\r|\n)", " ");

        assertThat(js)
                .contains("L.marker([48.856600, 2.352200]")
                .contains("test-marker-1.uuid = 'test-marker-1'")
                .contains("test-marker-1.addTo(this.map)");
    }

    @Test
    void buildJsElement_withOptions_containsOptionValues() {
        var builder = new JLMarkerBuilder()
                .setUuid("marker-opts")
                .setLat(51.5074)
                .setLng(-0.1278)
                .setText("London")
                .withOptions(JLOptions.builder()
                        .draggable(true)
                        .opacity(0.8)
                        .build());

        var js = builder.buildJsElement().trim().replaceAll("( +|\r|\n)", " ");

        assertThat(js)
                .contains("draggable: true")
                .contains("opacity: 0.8");
    }

    @Test
    void buildJsElement_withCallbacks_containsCallbackJs() {
        var builder = new JLMarkerBuilder()
                .setUuid("marker-cb")
                .setLat(40.7128)
                .setLng(-74.0060)
                .setText("NYC")
                .withCallbacks(cb -> {
                    cb.on(JLAction.CLICK);
                    cb.on(JLAction.ADD);
                });

        var js = builder.buildJsElement().trim().replaceAll("( +|\r|\n)", " ");

        assertThat(js)
                .contains("this.marker-cb.on('click'")
                .contains("this.marker-cb.on('add'");
    }

    @Test
    void buildJLObject_returnsMarkerWithCorrectFields() {
        var builder = new JLMarkerBuilder()
                .setUuid("marker-obj")
                .setLat(35.6762)
                .setLng(139.6503)
                .setText("Tokyo");

        var marker = builder.buildJLObject();

        assertThat(marker.getJLId()).isEqualTo("marker-obj");
        assertThat(marker.getLatLng().getLat()).isEqualTo(35.6762);
        assertThat(marker.getLatLng().getLng()).isEqualTo(139.6503);
        assertThat(marker.getText()).isEqualTo("Tokyo");
    }
}
