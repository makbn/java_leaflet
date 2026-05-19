package io.github.makbn.jlmap.model.builder;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JLPopupBuilderTest {

    @Test
    void buildJsElement_containsPopupCreationAndCoordinates() {
        var builder = new JLPopupBuilder()
                .setUuid("popup-1")
                .setLat(52.5200)
                .setLng(13.4050)
                .setContent("Hello Berlin");

        var js = builder.buildJsElement().trim().replaceAll("( +|\r|\n)", " ");

        assertThat(js)
                .contains("L.popup(")
                .contains(".setLatLng([52.520000, 13.405000]")
                .contains(".setContent(\"Hello Berlin\")");
    }

    @Test
    void buildJsElement_contentWithQuotes_escapedInJs() {
        var builder = new JLPopupBuilder()
                .setUuid("popup-quotes")
                .setLat(0)
                .setLng(0)
                .setContent("He said \"hello\"");

        var js = builder.buildJsElement();

        assertThat(js).contains("He said \\\"hello\\\"");
    }

    @Test
    void buildJsElement_contentWithScriptTag_scriptRemoved() {
        var builder = new JLPopupBuilder()
                .setUuid("popup-xss")
                .setLat(0)
                .setLng(0)
                .setContent("<script>alert('xss')</script>test");

        var js = builder.buildJsElement();

        assertThat(js)
                .doesNotContain("<script>")
                .doesNotContain("alert")
                .contains("test");
    }

    @Test
    void buildJsElement_nullContent_emptyString() {
        var builder = new JLPopupBuilder()
                .setUuid("popup-null")
                .setLat(0)
                .setLng(0);

        var js = builder.buildJsElement();

        assertThat(js).contains(".setContent(\"\")");
    }

    @Test
    void buildJLObject_returnsSanitizedPopup() {
        var builder = new JLPopupBuilder()
                .setUuid("popup-obj")
                .setLat(48.8566)
                .setLng(2.3522)
                .setContent("<script>bad</script>safe content");

        var popup = builder.buildJLObject();

        assertThat(popup.getJLId()).isEqualTo("popup-obj");
        assertThat(popup.getLatLng().getLat()).isEqualTo(48.8566);
        assertThat(popup.getLatLng().getLng()).isEqualTo(2.3522);
        assertThat(popup.getText())
                .doesNotContain("<script>")
                .contains("safe content");
    }
}
