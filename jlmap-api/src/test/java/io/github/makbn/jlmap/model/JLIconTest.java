package io.github.makbn.jlmap.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JLIconTest {

    @Test
    void toString_containsIconUrlAndLeafletCall() {
        var icon = JLIcon.builder()
                .iconUrl("http://example.com/icon.png")
                .iconSize(JLPoint.builder().x(25).y(41).build())
                .iconAnchor(JLPoint.builder().x(12).y(41).build())
                .popupAnchor(JLPoint.builder().x(1).y(-34).build())
                .shadowSize(JLPoint.builder().x(41).y(41).build())
                .shadowAnchor(JLPoint.builder().x(12).y(41).build())
                .build();

        var result = icon.toString();

        assertThat(result)
                .startsWith("L.icon({")
                .contains("iconUrl: 'http://example.com/icon.png'")
                .endsWith("})");
    }

    @Test
    void toString_withShadowUrl_containsShadowUrl() {
        var icon = JLIcon.builder()
                .iconUrl("http://example.com/icon.png")
                .shadowUrl("http://example.com/shadow.png")
                .iconSize(JLPoint.builder().x(25).y(41).build())
                .iconAnchor(JLPoint.builder().x(12).y(41).build())
                .popupAnchor(JLPoint.builder().x(1).y(-34).build())
                .shadowSize(JLPoint.builder().x(41).y(41).build())
                .shadowAnchor(JLPoint.builder().x(12).y(41).build())
                .build();

        var result = icon.toString();

        assertThat(result).contains("shadowUrl: 'http://example.com/shadow.png'");
    }

    @Test
    void toString_withoutShadowUrl_doesNotContainShadowUrl() {
        var icon = JLIcon.builder()
                .iconUrl("http://example.com/icon.png")
                .iconSize(JLPoint.builder().x(25).y(41).build())
                .iconAnchor(JLPoint.builder().x(12).y(41).build())
                .popupAnchor(JLPoint.builder().x(1).y(-34).build())
                .shadowSize(JLPoint.builder().x(41).y(41).build())
                .shadowAnchor(JLPoint.builder().x(12).y(41).build())
                .build();

        var result = icon.toString();

        assertThat(result).doesNotContain("shadowUrl:");
    }

    @Test
    void toString_containsSizeAndAnchorValues() {
        var icon = JLIcon.builder()
                .iconUrl("http://example.com/icon.png")
                .iconSize(JLPoint.builder().x(25).y(41).build())
                .iconAnchor(JLPoint.builder().x(12).y(41).build())
                .popupAnchor(JLPoint.builder().x(1).y(-34).build())
                .shadowSize(JLPoint.builder().x(41).y(41).build())
                .shadowAnchor(JLPoint.builder().x(12).y(41).build())
                .build();

        var result = icon.toString();

        assertThat(result)
                .contains("iconSize: [25.0, 41.0]")
                .contains("iconAnchor: [12.0, 41.0]")
                .contains("popupAnchor: [1.0, -34.0]");
    }
}
