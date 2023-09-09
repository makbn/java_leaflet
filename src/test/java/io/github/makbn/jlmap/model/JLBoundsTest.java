package io.github.makbn.jlmap.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JLBoundsTest {

    @Test
    void testToString() {
        JLBounds bounds = JLBounds.builder()
                .northEast(JLLatLng.builder().lat(10).lng(10).build())
                .southWest(JLLatLng.builder().lat(40).lng(60).build())
                .build();

        assertEquals("[[10.000000, 10.000000], [40.000000, 60.000000]]", bounds.toString());
    }
}
