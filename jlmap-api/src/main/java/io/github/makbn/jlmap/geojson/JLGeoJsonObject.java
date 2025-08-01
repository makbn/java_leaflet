package io.github.makbn.jlmap.geojson;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

/**
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
@Getter
@Setter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class JLGeoJsonObject {
    @NonFinal
    int id;
    String geoJsonContent;

    @Builder
    public JLGeoJsonObject(int id, String geoJsonContent) {
        this.id = id;
        this.geoJsonContent = geoJsonContent;
    }
}
