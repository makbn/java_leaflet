package io.github.makbn.jlmap.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class JLLatLngTest {

    @Test
    void distanceTo_same_point_should_return_zero() {
        JLLatLng point = JLLatLng.builder().lat(40.7128).lng(-74.0060).build();
        assertThat(point.distanceTo(point)).isCloseTo(0.0, within(0.001));
    }

    @Test
    void distanceTo_nyc_to_la_should_be_approximately_3940km() {
        JLLatLng nyc = JLLatLng.builder().lat(40.7128).lng(-74.0060).build();
        JLLatLng la = JLLatLng.builder().lat(34.0522).lng(-118.2437).build();

        double distance = nyc.distanceTo(la);
        assertThat(distance).isCloseTo(3_940_000, within(50_000.0));
    }

    @Test
    void distanceTo_equator_to_north_pole_should_be_approximately_10000km() {
        JLLatLng equator = JLLatLng.builder().lat(0).lng(0).build();
        JLLatLng pole = JLLatLng.builder().lat(90).lng(0).build();

        double distance = equator.distanceTo(pole);
        assertThat(distance).isCloseTo(10_000_000, within(100_000.0));
    }

    @Test
    void distanceTo_should_be_symmetric() {
        JLLatLng paris = JLLatLng.builder().lat(48.8566).lng(2.3522).build();
        JLLatLng london = JLLatLng.builder().lat(51.5074).lng(-0.1278).build();

        assertThat(paris.distanceTo(london))
                .isCloseTo(london.distanceTo(paris), within(0.001));
    }

    @Test
    void distanceTo_antipodal_points_should_be_approximately_half_circumference() {
        JLLatLng a = JLLatLng.builder().lat(0).lng(0).build();
        JLLatLng b = JLLatLng.builder().lat(0).lng(180).build();

        double distance = a.distanceTo(b);
        assertThat(distance).isCloseTo(20_000_000, within(200_000.0));
    }

    @Test
    void distanceTo_short_distance_should_be_positive() {
        JLLatLng a = JLLatLng.builder().lat(10.0).lng(20.0).build();
        JLLatLng b = JLLatLng.builder().lat(10.001).lng(20.001).build();

        assertThat(a.distanceTo(b)).isGreaterThan(0);
    }

    @Test
    void equals_should_return_true_for_same_coordinates() {
        JLLatLng a = JLLatLng.builder().lat(10.0).lng(20.0).build();
        JLLatLng b = JLLatLng.builder().lat(10.0).lng(20.0).build();

        assertThat(a).isEqualTo(b);
    }

    @Test
    void equals_should_return_false_for_different_lat() {
        JLLatLng a = JLLatLng.builder().lat(10.0).lng(20.0).build();
        JLLatLng b = JLLatLng.builder().lat(10.1).lng(20.0).build();

        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void equals_should_return_false_for_different_lng() {
        JLLatLng a = JLLatLng.builder().lat(10.0).lng(20.0).build();
        JLLatLng b = JLLatLng.builder().lat(10.0).lng(20.1).build();

        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void equals_should_return_true_for_same_reference() {
        JLLatLng a = JLLatLng.builder().lat(10.0).lng(20.0).build();
        assertThat(a).isEqualTo(a);
    }

    @Test
    void equals_should_return_false_for_null() {
        JLLatLng a = JLLatLng.builder().lat(10.0).lng(20.0).build();
        assertThat(a).isNotEqualTo(null);
    }

    @Test
    void equals_should_return_false_for_different_type() {
        JLLatLng a = JLLatLng.builder().lat(10.0).lng(20.0).build();
        assertThat(a).isNotEqualTo("not a latlng");
    }

    @Test
    void equals_with_margin_should_return_true_for_nearby_points() {
        JLLatLng a = JLLatLng.builder().lat(10.0).lng(20.0).build();
        JLLatLng b = JLLatLng.builder().lat(10.0001).lng(20.0001).build();

        assertThat(a.equals(b, 100f)).isTrue();
    }

    @Test
    void equals_with_margin_should_return_false_for_distant_points() {
        JLLatLng a = JLLatLng.builder().lat(10.0).lng(20.0).build();
        JLLatLng b = JLLatLng.builder().lat(11.0).lng(21.0).build();

        assertThat(a.equals(b, 1f)).isFalse();
    }

    @Test
    void equals_with_margin_should_return_true_for_same_reference() {
        JLLatLng a = JLLatLng.builder().lat(10.0).lng(20.0).build();
        assertThat(a.equals(a, 0f)).isTrue();
    }

    @Test
    void equals_with_margin_should_return_false_for_null() {
        JLLatLng a = JLLatLng.builder().lat(10.0).lng(20.0).build();
        assertThat(a.equals(null, 100f)).isFalse();
    }

    @Test
    void equals_with_margin_should_return_false_for_different_type() {
        JLLatLng a = JLLatLng.builder().lat(10.0).lng(20.0).build();
        assertThat(a.equals("not a latlng", 100f)).isFalse();
    }

    @Test
    void hashCode_should_be_consistent_for_equal_points() {
        JLLatLng a = JLLatLng.builder().lat(10.0).lng(20.0).build();
        JLLatLng b = JLLatLng.builder().lat(10.0).lng(20.0).build();

        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void hashCode_should_differ_for_different_points() {
        JLLatLng a = JLLatLng.builder().lat(10.0).lng(20.0).build();
        JLLatLng b = JLLatLng.builder().lat(30.0).lng(40.0).build();

        assertThat(a.hashCode()).isNotEqualTo(b.hashCode());
    }

    @Test
    void toString_should_format_as_lat_lng_in_brackets() {
        JLLatLng point = JLLatLng.builder().lat(10.5).lng(20.5).build();

        String expected = String.format("[%f, %f]", 10.5, 20.5);
        assertThat(point.toString()).isEqualTo(expected);
    }

    @Test
    void toString_should_handle_negative_coordinates() {
        JLLatLng point = JLLatLng.builder().lat(-33.8688).lng(151.2093).build();

        String expected = String.format("[%f, %f]", -33.8688, 151.2093);
        assertThat(point.toString()).isEqualTo(expected);
    }

    @Test
    void toString_should_handle_zero_coordinates() {
        JLLatLng point = JLLatLng.builder().lat(0).lng(0).build();

        String expected = String.format("[%f, %f]", 0.0, 0.0);
        assertThat(point.toString()).isEqualTo(expected);
    }
}
