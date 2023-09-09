package io.github.makbn.jlmap.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JLLatLngTest {

    @Test
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

    @Test
    void testDistanceCalculation_lng() {
        JLLatLng pointA = JLLatLng.builder()
                .lat(10)
                .lng(20)
                .build();

        JLLatLng pointB = JLLatLng.builder()
                .lat(10)
                .lng(50)
                .build();

        Assertions.assertTrue(Math.abs(3282 - Math.round(pointA.distanceTo(pointB)/ 1000)) < 0.01);
    }

    @Test
    void testDistanceCalculation_lat() {
        JLLatLng pointA = JLLatLng.builder()
                .lat(50)
                .lng(10)
                .build();

        JLLatLng pointB = JLLatLng.builder()
                .lat(20)
                .lng(10)
                .build();

        Assertions.assertTrue(Math.abs(3334 - Math.round(pointA.distanceTo(pointB)/ 1000)) < 0.01);
    }

    @Test
    void testDistanceCalculation_latLng() {
        JLLatLng pointA = JLLatLng.builder()
                .lat(50)
                .lng(80)
                .build();

        JLLatLng pointB = JLLatLng.builder()
                .lat(30)
                .lng(10)
                .build();

        Assertions.assertTrue(Math.abs(6113 - Math.round(pointA.distanceTo(pointB)/ 1000)) < 0.01);
    }
}
