package io.github.makbn.jlmap.geojson;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.github.makbn.jlmap.exception.JLGeoJsonParserException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

/**
 * The base abstract class for a GeoJSON data source. Implementations of this class are expected
 * to provide functionality for loading and accessing GeoJSON objects.
 * @author Mehdi Akbarian Rastaghi (@makbn)
 *
 * @param <S> source type
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
public abstract class JLGeoJsonSource<S> {

    /**
     * Gson object for JSON serialization and deserialization.
     */
    Gson gson;

    /**
     * The GeoJSON object loaded from this source.
     */
    @NonFinal
    JLGeoJsonObject geoJsonObject;

    /**
     * Initializes a new instance of {@code JLGeoJsonSource} and sets up the Gson object.
     */
    protected JLGeoJsonSource() {
        this.gson = new Gson();
    }

    /**
     * Loads the GeoJSON data from the source and parses it into a GeoJSON object.
     *
     * @throws JLGeoJsonParserException If an error occurs during data loading or parsing.
     */
    public abstract String load(S source) throws JLGeoJsonParserException;

    protected void validateJson(String jsonInString) throws JsonSyntaxException {
        gson.fromJson(jsonInString, Object.class);
    }

}
