package io.github.makbn.jlmap.geojson;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JLGeoJsonObject {
    private String type;
    private List<JLFeature> features;

}
