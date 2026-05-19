package io.github.makbn.jlmap.geojson;

import io.github.makbn.jlmap.exception.JLGeoJsonParserException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JLGeoJsonContentTest {

    private final JLGeoJsonContent content = new JLGeoJsonContent();

    @Test
    void load_nullInput_throwsException() {
        assertThatThrownBy(() -> content.load(null))
                .isInstanceOf(JLGeoJsonParserException.class);
    }

    @Test
    void load_emptyString_throwsException() {
        assertThatThrownBy(() -> content.load(""))
                .isInstanceOf(JLGeoJsonParserException.class);
    }

    @Test
    void load_validJson_returnsSameString() throws JLGeoJsonParserException {
        var json = "{\"type\": \"Point\", \"coordinates\": [0, 0]}";

        var result = content.load(json);

        assertThat(result).isEqualTo(json);
    }

    @Test
    void load_invalidJson_throwsException() {
        assertThatThrownBy(() -> content.load("not json {{{"))
                .isInstanceOf(JLGeoJsonParserException.class);
    }

    @Test
    void load_validFeatureCollection_returnsContent() throws JLGeoJsonParserException {
        var geoJson = """
                {
                  "type": "FeatureCollection",
                  "features": [
                    {
                      "type": "Feature",
                      "geometry": {
                        "type": "Point",
                        "coordinates": [102.0, 0.5]
                      },
                      "properties": {
                        "name": "test"
                      }
                    }
                  ]
                }
                """;

        var result = content.load(geoJson);

        assertThat(result).isEqualTo(geoJson);
    }
}
