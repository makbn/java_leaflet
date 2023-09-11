package io.github.makbn.jlmap.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("JLBounds")
class JLBoundsTest implements ModelTest {

    public static class TestArgumentProvider implements ArgumentsProvider {

        private JLLatLng latLng(Double lat, Double lng) {
            return JLLatLng.builder()
                    .lat(lat)
                    .lng(lng)
                    .build();
        }

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            if (extensionContext.getTags().contains("test_get_center")) {
                return Stream.of(
                        Arguments.of(latLng(51.03, -124.48), latLng(58.23, -110.76), latLng(54.63, -117.62)),
                        Arguments.of(latLng(50.92, -114.12), latLng(51.16, -113.9), latLng(51.04, -114.01))
                );
            } else if (extensionContext.getTags().contains("test_get_direction")) {
                return Stream.of(
                        Arguments.of(JLBounds.builder().southWest(latLng(51.03, -124.48)).northEast(latLng(58.23, -110.76)).build(), -124.48, 58.23, -110.76, 51.03),
                        Arguments.of(JLBounds.builder().southWest(latLng(50.92, -114.12)).northEast(latLng(51.16, -113.9)).build(), -114.12, 51.16, -113.9, 50.92)
                );
            }
            return Stream.empty();
        }
    }

    @Test
    @DisplayName("Test toString output format")
    void testToString() {
        JLBounds bounds = JLBounds.builder()
                .northEast(JLLatLng.builder().lat(10).lng(10).build())
                .southWest(JLLatLng.builder().lat(40).lng(60).build())
                .build();

        assertEquals("[[10.000000, 10.000000], [40.000000, 60.000000]]", bounds.toString());
    }

    @Test
    @DisplayName("Test toBBoxString output format")
    void testBBox() {
        JLBounds bounds = JLBounds.builder()
                .northEast(JLLatLng.builder().lat(10).lng(20).build())
                .southWest(JLLatLng.builder().lat(40).lng(60).build())
                .build();

        assertEquals("60.000000,40.000000,20.000000,10.000000", bounds.toBBoxString());
    }

    @Tag("test_get_center")
    @ParameterizedTest(name = "SW: {0}, NE: {1} Center: {2}")
    @ArgumentsSource(TestArgumentProvider.class)
    @DisplayName("Test getCenter method to find the center of a bound")
    void testGetCenter(JLLatLng sw, JLLatLng ne, JLLatLng expectedCenter) {
        JLLatLng calculatedCenter = JLBounds.builder()
                .southWest(sw)
                .northEast(ne)
                .build()
                .getCenter();

        Assertions.assertTrue(calculatedCenter.distanceTo(expectedCenter) / 1000 < DISTANCE_ERROR_KM);
    }

    @Tag("test_get_direction")
    @ParameterizedTest(name = "Point: {0}")
    @ArgumentsSource(TestArgumentProvider.class)
    @DisplayName("Test get directions of a bound")
    void testGetDirections(JLBounds bounds, double west, double north, double east, double south) {
        Assertions.assertEquals(west, bounds.getWest());
        Assertions.assertEquals(north, bounds.getNorth());
        Assertions.assertEquals(east, bounds.getEast());
        Assertions.assertEquals(south, bounds.getSouth());
    }
}
