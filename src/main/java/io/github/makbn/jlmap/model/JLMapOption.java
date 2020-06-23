package io.github.makbn.jlmap.model;

import io.github.makbn.jlmap.JLProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class JLMapOption {

    @Builder.Default
    private JLLatLng startCoordinate = JLLatLng.builder()
            .lat(33)
            .lng(33)
            .build();
    @Builder.Default
    private JLProperties.MapType mapType = JLProperties.MapType.DARK;
    private String accessToken;

}
