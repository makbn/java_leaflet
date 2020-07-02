package io.github.makbn.jlmap.model;

import io.github.makbn.jlmap.JLProperties;
import lombok.*;

import java.util.Objects;

/**
 * Represents a geographical point with a certain latitude and longitude.
 * by: Mehdi Akbarian Rastaghi (@makbn)
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class JLLatLng {
    /** geographical given latitude in degrees */
    private final double lat;
    /** geographical given longitude in degrees */
    private final double lng;

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference.Uses Haversine method as its base.
     * @param dest Destination coordinate {{@link JLLatLng}}
     * @return Distance in Meters
     * @author David George
     */
    public double distanceTo(JLLatLng dest){
        double latDistance = Math.toRadians(dest.getLat() - lat);
        double lonDistance = Math.toRadians(dest.getLng() - lng);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(dest.getLat()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = JLProperties.EARTH_RADIUS * c * 1000;

        distance = Math.pow(distance, 2);
        return Math.sqrt(distance);
    }

    /**
     *
     * @param o The given point
     * @return Returns true if the given {{@link JLLatLng}} point is exactly at the same position.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JLLatLng latLng = (JLLatLng) o;
        return Double.compare(latLng.lat, lat) == 0 &&
                Double.compare(latLng.lng, lng) == 0;
    }

    /**
     *
     * @param o The given point
     * @param maxMargin The margin of error
     * @return Returns true if the given {{@link JLLatLng}} point is at the same position (within a small margin of error).
     * The margin of error can be overridden by setting maxMargin.
     */
    public boolean equals(Object o, int maxMargin) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JLLatLng latLng = (JLLatLng) o;
        return distanceTo(latLng) <= maxMargin;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lng);
    }
}
