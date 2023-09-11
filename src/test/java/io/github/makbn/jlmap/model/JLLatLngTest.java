package io.github.makbn.jlmap.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


@Tag("JLLatLng")
class JLLatLngTest implements ModelTest {

    @Test
    @DisplayName("Test the equals method for exact similar points")
    void testEquals() {
        JLLatLng pointA = JLLatLng.builder()
                .lat(10)
                .lng(20)
                .build();

        JLLatLng pointB = JLLatLng.builder()
                .lat(10.000)
                .lng(20.00)
                .build();
        Assertions.assertEquals(pointA, pointB);
    }

    @Test
    @DisplayName("Test the equals method for non-similar points")
    void testNotEquals() {
        JLLatLng pointA = JLLatLng.builder()
                .lat(10)
                .lng(20)
                .build();

        JLLatLng pointB = JLLatLng.builder()
                .lat(20)
                .lng(10)
                .build();

        Assertions.assertNotEquals(pointA, pointB);
    }

    @ParameterizedTest(name = "Point A(lat: {0} lng:{1}),  Point B(lat: {2}, lng: {3}), Distance: {4}")
    @DisplayName("Test the equals method with margin for similar points")
    @CsvSource({
            "10, 20, 10.0001, 20",
            "10, 20.0001, 10, 20",
            "10.0001, 20.0001, 10, 20"
    })
    void testNotEqualsWithError(double pointALat, double pointALng, double pointBLat, double pointBLng) {
        JLLatLng pointA = JLLatLng.builder()
                .lat(pointALat)
                .lng(pointALng)
                .build();

        JLLatLng pointB = JLLatLng.builder()
                .lat(pointBLat)
                .lng(pointBLng)
                .build();
        // max margin should be in meter, DISTANCE_ERROR is in Km
        Assertions.assertTrue(pointA.equals(pointB, DISTANCE_ERROR_M));
    }

    @ParameterizedTest(name = "Point A(lat: {0} lng:{1}),  Point B(lat: {2}, lng: {3}), Distance: {4}")
    @DisplayName("Test distance calculation in different directions")
    @CsvSource({
            "10, 20, 10, 50, 3282",
            "50, 10, 20, 10, 3334",
            "50, 80, 30, 10, 6113"
    })
    void testDistanceCalculation_lng(double pointALat, double pointALng, double pointBLat, double pointBLng, int distance) {
        JLLatLng pointA = JLLatLng.builder()
                .lat(pointALat)
                .lng(pointALng)
                .build();

        JLLatLng pointB = JLLatLng.builder()
                .lat(pointBLat)
                .lng(pointBLng)
                .build();

        Assertions.assertTrue(Math.abs(distance - Math.round(pointA.distanceTo(pointB) / 1000)) < DISTANCE_ERROR_KM);
    }
}
