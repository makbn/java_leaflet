package io.github.makbn.jlmap.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * Represents a rectangular geographical area on a map.
 * by: Mehdi Akbarian Rastaghi (@makbn)
 */
@Getter
@Setter
@Builder
@ToString
public class JLBounds {
    /**
     * the north-east point of the bounds.
     */
    private JLLatLng northEast;
    /**
     * the south-west point of the bounds.
     */
    private JLLatLng southWest;

    /**
     * @return the west longitude of the bounds
     */
    public double getWest() {
        return southWest.getLng();
    }

    /**
     * @return the south latitude of the bounds
     */
    public double getSouth() {
        return southWest.getLat();
    }

    /**
     * @return the east longitude of the bounds
     */
    public double getEast() {
        return northEast.getLng();
    }

    /**
     * @return the north latitude of the bounds
     */
    public double getNorth() {
        return northEast.getLat();
    }

    /**
     * @return the south-east point of the bounds
     */
    public JLLatLng getSouthEast() {
        return JLLatLng.builder()
                .lat(southWest.getLat())
                .lng(northEast.getLng())
                .build();
    }

    /**
     * @return the north-west point of the bounds.
     */
    public JLLatLng getNorthWest() {
        return JLLatLng.builder()
                .lat(northEast.getLat())
                .lng(southWest.getLng())
                .build();
    }

    public JLLatLng getCenter() {
        return JLLatLng.builder()
                .lat((northEast.getLat() + southWest.getLat()) / 2)
                .lng((northEast.getLng() + southWest.getLng()) / 2)
                .build();
    }

    /**
     * @return a string with bounding box coordinates in a
     * 'southwest_lng,southwest_lat,northeast_lng,northeast_lat' format.
     * Useful for sending requests to web services that return geo data.
     */
    public String toBBoxString() {
        return String.format("%f,%f,%f,%f", southWest.getLng(), southWest.getLat(),
                northEast.getLat(), northEast.getLat());
    }

    /**
     * @param bounds given bounds
     * @return {{@link Boolean#TRUE}} if the rectangle contains the given bounds.
     */
    public boolean contains(JLBounds bounds) {
        return (bounds.getSouthWest().getLat() >= southWest.getLat())
                && (bounds.getNorthEast().getLat() <= northEast.getLat())
                && (bounds.getSouthWest().getLng() >= southWest.getLng())
                && (bounds.getNorthEast().getLng() <= northEast.getLng());
    }

    /**
     * @param point given point
     * @return {{@link Boolean#TRUE}} if the rectangle contains the given point.
     */
    public boolean contains(JLLatLng point) {
        return (point.getLat() >= southWest.getLat())
                && (point.getLat() <= northEast.getLat())
                && (point.getLng() >= southWest.getLng())
                && (point.getLng() <= northEast.getLng());
    }

    /**
     * @return {{@link Boolean#TRUE}} if the bounds are properly initialized.
     */
    public boolean isValid() {
        return northEast != null && southWest != null;
    }

    /**
     * @param bufferRatio extending or retracting value
     * @return bounds created by extending or retracting the current bounds by a given ratio
     * in each direction. For example, a ratio of 0.5 extends the bounds by 50% in each direction.
     * Negative values will retract the bounds.
     */
    public JLBounds pad(double bufferRatio) {
        double heightBuffer = Math.abs(southWest.getLat() - northEast.getLat()) * bufferRatio;
        double widthBuffer = Math.abs(southWest.getLng() - northEast.getLng()) * bufferRatio;

        return new JLBounds(
                new JLLatLng(southWest.getLat() - heightBuffer, southWest.getLng() - widthBuffer),
                new JLLatLng(northEast.getLat() + heightBuffer, northEast.getLng() + widthBuffer));
    }

    /**
     * @param bounds    the given bounds
     * @param maxMargin The margin of error
     * @return {{@link Boolean#TRUE}} if the rectangle is equivalent (within a small margin of error) to the given bounds.
     */
    public boolean equals(JLBounds bounds, int maxMargin) {
        if (bounds == null) {
            return false;
        }

        return this.getSouthWest().equals(bounds.getSouthWest(), maxMargin) &&
                this.getNorthEast().equals(bounds.getNorthEast(), maxMargin);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JLBounds jlBounds = (JLBounds) o;
        return Objects.equals(northEast, jlBounds.northEast) &&
                Objects.equals(southWest, jlBounds.southWest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(northEast, southWest);
    }
}
