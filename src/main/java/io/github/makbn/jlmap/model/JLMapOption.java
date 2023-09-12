package io.github.makbn.jlmap.model;

import io.github.makbn.jlmap.JLProperties;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The {@code JLMapOption} class represents options for configuring a Leaflet
 * map. It is designed to be used with the Builder pattern and immutability
 * provided by Lombok's {@code @Builder} and {@code @Value} annotations.
 * This class allows you to specify various map configuration parameters,
 * such as the starting coordinates, map type, and additional parameters.
 *
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
@Builder
@Value
public class JLMapOption {

    /**
     * The starting geographical coordinates (latitude and longitude)
     * for the map.
     * Default value is (0.00, 0.00).
     */
    @Builder.Default
    @NonNull
    JLLatLng startCoordinate = JLLatLng.builder()
            .lat(0.00)
            .lng(0.00)
            .build();
    /**
     * The map type for configuring the map's appearance and behavior.
     * Default value is the default map type.
     */
    @Builder.Default
    @NonNull
    JLProperties.MapType mapType = JLProperties.MapType.getDefault();

    /**
     * Converts the map options to a query string format, including both
     * map-specific parameters and additional parameters.
     *
     * @return The map options as a query string.
     */
    @NonNull
    public String toQueryString() {
        return Stream.concat(
                        getParameters().stream(), additionalParameter.stream())
                .map(Parameter::toString)
                .collect(Collectors.joining("&",
                        String.format("?mapid=%s&", getMapType().name()), ""));
    }

    /**
     * Additional parameters to include in the map configuration.
     */
    @Builder.Default
    Set<Parameter> additionalParameter = new HashSet<>();

    /**
     * Gets the map-specific parameters based on the selected map type.
     *
     * @return A set of map-specific parameters.
     */
    public Set<Parameter> getParameters() {
        return mapType.parameters();
    }

    /**
     * Represents a key-value pair used for additional parameters in the map
     * configuration.
     */
    public record Parameter(String key, String value) {
        @Override
        public String toString() {
            return String.format("%s=%s", key, value);
        }
    }
}

