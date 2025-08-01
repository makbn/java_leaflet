package io.github.makbn.jlmap.geojson;

import com.google.gson.JsonParseException;
import io.github.makbn.jlmap.exception.JLGeoJsonParserException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class JLGeoJsonContent extends JLGeoJsonSource<String> {

    @Override
    public String load(String content) throws JLGeoJsonParserException {
        if (content == null || content.isEmpty())
            throw JLGeoJsonParserException.builder()
                    .message("json is empty!")
                    .build();
        try {
            validateJson(content);
            return content;
        } catch (JsonParseException e) {
            throw new JLGeoJsonParserException(e.getMessage());
        }
    }
}
