package io.github.makbn.jlmap.geojson;

import com.google.gson.JsonParseException;
import io.github.makbn.jlmap.exception.JLGeoJsonParserException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class JLGeoJsonFile extends JLGeoJsonSource<File> {

    @Override
    public String load(File file) throws JLGeoJsonParserException {
        try {
            String fileContent = Files.readString(file.toPath());
            validateJson(fileContent);
            return fileContent;
        } catch (IOException | JsonParseException e) {
            throw new JLGeoJsonParserException(e.getMessage());
        }
    }
}
