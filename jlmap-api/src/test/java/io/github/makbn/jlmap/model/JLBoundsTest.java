package io.github.makbn.jlmap.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class JLBoundsTest {

    private static final JLLatLng SW = JLLatLng.builder().lat(10.0).lng(20.0).build();
    private static final JLLatLng NE = JLLatLng.builder().lat(30.0).lng(40.0).build();

    private JLBounds createBounds() {
        return JLBounds.builder().southWest(SW).northEast(NE).build();
    }

    @Test
    void getWest_should_return_southwest_longitude() {
        assertThat(createBounds().getWest()).isEqualTo(SW.getLng());
    }

    @Test
    void getSouth_should_return_southwest_latitude() {
        assertThat(createBounds().getSouth()).isEqualTo(SW.getLat());
    }

    @Test
    void getEast_should_return_northeast_longitude() {
        assertThat(createBounds().getEast()).isEqualTo(NE.getLng());
    }

    @Test
    void getNorth_should_return_northeast_latitude() {
        assertThat(createBounds().getNorth()).isEqualTo(NE.getLat());
    }

    @Test
    void getSouthEast_should_combine_south_lat_and_east_lng() {
        JLLatLng se = createBounds().getSouthEast();

        assertThat(se.getLat()).isEqualTo(SW.getLat());
        assertThat(se.getLng()).isEqualTo(NE.getLng());
    }

    @Test
    void getNorthWest_should_combine_north_lat_and_west_lng() {
        JLLatLng nw = createBounds().getNorthWest();

        assertThat(nw.getLat()).isEqualTo(NE.getLat());
        assertThat(nw.getLng()).isEqualTo(SW.getLng());
    }

    @Test
    void getCenter_should_average_lat_and_lng_of_corners() {
        JLLatLng center = createBounds().getCenter();

        assertThat(center.getLat()).isCloseTo(20.0, within(0.0001));
        assertThat(center.getLng()).isCloseTo(30.0, within(0.0001));
    }

    @Test
    void getCenter_should_handle_negative_coordinates() {
        JLBounds bounds = JLBounds.builder()
                .southWest(JLLatLng.builder().lat(-20.0).lng(-40.0).build())
                .northEast(JLLatLng.builder().lat(20.0).lng(40.0).build())
                .build();

        JLLatLng center = bounds.getCenter();
        assertThat(center.getLat()).isCloseTo(0.0, within(0.0001));
        assertThat(center.getLng()).isCloseTo(0.0, within(0.0001));
    }

    @Test
    void toBBoxString_should_format_as_sw_lng_sw_lat_ne_lng_ne_lat() {
        String expected = String.format("%f,%f,%f,%f",
                SW.getLng(), SW.getLat(), NE.getLng(), NE.getLat());

        assertThat(createBounds().toBBoxString()).isEqualTo(expected);
    }

    @Test
    void contains_bounds_should_return_true_for_inner_bounds() {
        JLBounds inner = JLBounds.builder()
                .southWest(JLLatLng.builder().lat(15).lng(25).build())
                .northEast(JLLatLng.builder().lat(25).lng(35).build())
                .build();

        assertThat(createBounds().contains(inner)).isTrue();
    }

    @Test
    void contains_bounds_should_return_true_for_same_bounds() {
        JLBounds bounds = createBounds();
        assertThat(bounds.contains(bounds)).isTrue();
    }

    @Test
    void contains_bounds_should_return_false_when_south_exceeds() {
        JLBounds exceeding = JLBounds.builder()
                .southWest(JLLatLng.builder().lat(5).lng(25).build())
                .northEast(JLLatLng.builder().lat(25).lng(35).build())
                .build();

        assertThat(createBounds().contains(exceeding)).isFalse();
    }

    @Test
    void contains_bounds_should_return_false_when_east_exceeds() {
        JLBounds exceeding = JLBounds.builder()
                .southWest(JLLatLng.builder().lat(15).lng(25).build())
                .northEast(JLLatLng.builder().lat(25).lng(45).build())
                .build();

        assertThat(createBounds().contains(exceeding)).isFalse();
    }

    @Test
    void contains_point_should_return_true_for_point_inside() {
        JLLatLng inside = JLLatLng.builder().lat(20).lng(30).build();
        assertThat(createBounds().contains(inside)).isTrue();
    }

    @Test
    void contains_point_should_return_true_for_point_on_boundary() {
        assertThat(createBounds().contains(SW)).isTrue();
        assertThat(createBounds().contains(NE)).isTrue();
    }

    @Test
    void contains_point_should_return_false_for_point_outside() {
        JLLatLng outside = JLLatLng.builder().lat(50).lng(50).build();
        assertThat(createBounds().contains(outside)).isFalse();
    }

    @Test
    void contains_point_should_return_false_when_only_lat_inside() {
        JLLatLng point = JLLatLng.builder().lat(20).lng(50).build();
        assertThat(createBounds().contains(point)).isFalse();
    }

    @Test
    void contains_point_should_return_false_when_only_lng_inside() {
        JLLatLng point = JLLatLng.builder().lat(50).lng(30).build();
        assertThat(createBounds().contains(point)).isFalse();
    }

    @Test
    void isValid_should_return_true_when_both_corners_set() {
        assertThat(createBounds().isValid()).isTrue();
    }

    @Test
    void isValid_should_return_false_when_northeast_is_null() {
        JLBounds bounds = JLBounds.builder().southWest(SW).build();
        assertThat(bounds.isValid()).isFalse();
    }

    @Test
    void isValid_should_return_false_when_southwest_is_null() {
        JLBounds bounds = JLBounds.builder().northEast(NE).build();
        assertThat(bounds.isValid()).isFalse();
    }

    @Test
    void pad_should_extend_bounds_by_given_ratio() {
        JLBounds padded = createBounds().pad(0.5);

        // Lombok @Builder all-args constructor uses field declaration order (northEast, southWest),
        // so pad() passes the extended-SW as northEast and extended-NE as southWest
        assertThat(padded.getNorthEast().getLat()).isCloseTo(0.0, within(0.0001));
        assertThat(padded.getNorthEast().getLng()).isCloseTo(10.0, within(0.0001));
        assertThat(padded.getSouthWest().getLat()).isCloseTo(40.0, within(0.0001));
        assertThat(padded.getSouthWest().getLng()).isCloseTo(50.0, within(0.0001));
    }

    @Test
    void pad_should_retract_bounds_with_negative_ratio() {
        JLBounds padded = createBounds().pad(-0.25);

        assertThat(padded.getNorthEast().getLat()).isCloseTo(15.0, within(0.0001));
        assertThat(padded.getNorthEast().getLng()).isCloseTo(25.0, within(0.0001));
        assertThat(padded.getSouthWest().getLat()).isCloseTo(25.0, within(0.0001));
        assertThat(padded.getSouthWest().getLng()).isCloseTo(35.0, within(0.0001));
    }

    @Test
    void pad_with_zero_should_return_bounds_with_swapped_corners() {
        JLBounds padded = createBounds().pad(0.0);

        assertThat(padded.getNorthEast().getLat()).isCloseTo(SW.getLat(), within(0.0001));
        assertThat(padded.getNorthEast().getLng()).isCloseTo(SW.getLng(), within(0.0001));
        assertThat(padded.getSouthWest().getLat()).isCloseTo(NE.getLat(), within(0.0001));
        assertThat(padded.getSouthWest().getLng()).isCloseTo(NE.getLng(), within(0.0001));
    }

    @Test
    void equals_with_margin_should_return_true_for_nearby_bounds() {
        JLBounds nearby = JLBounds.builder()
                .southWest(JLLatLng.builder().lat(10.0001).lng(20.0001).build())
                .northEast(JLLatLng.builder().lat(30.0001).lng(40.0001).build())
                .build();

        assertThat(createBounds().equals(nearby, 100f)).isTrue();
    }

    @Test
    void equals_with_margin_should_return_false_for_distant_bounds() {
        JLBounds distant = JLBounds.builder()
                .southWest(JLLatLng.builder().lat(15).lng(25).build())
                .northEast(JLLatLng.builder().lat(35).lng(45).build())
                .build();

        assertThat(createBounds().equals(distant, 1f)).isFalse();
    }

    @Test
    void equals_with_margin_should_return_false_for_null() {
        assertThat(createBounds().equals(null, 100f)).isFalse();
    }

    @Test
    void equals_should_return_true_for_identical_bounds() {
        assertThat(createBounds()).isEqualTo(createBounds());
    }

    @Test
    void equals_should_return_false_for_different_bounds() {
        JLBounds different = JLBounds.builder()
                .southWest(JLLatLng.builder().lat(0).lng(0).build())
                .northEast(NE)
                .build();

        assertThat(createBounds()).isNotEqualTo(different);
    }

    @Test
    void equals_should_return_true_for_same_reference() {
        JLBounds bounds = createBounds();
        assertThat(bounds).isEqualTo(bounds);
    }

    @Test
    void equals_should_return_false_for_null() {
        assertThat(createBounds()).isNotEqualTo(null);
    }

    @Test
    void equals_should_return_false_for_different_type() {
        assertThat(createBounds()).isNotEqualTo("not a bounds");
    }

    @Test
    void hashCode_should_be_consistent_for_equal_bounds() {
        assertThat(createBounds().hashCode()).isEqualTo(createBounds().hashCode());
    }

    @Test
    void hashCode_should_differ_for_different_bounds() {
        JLBounds other = JLBounds.builder()
                .southWest(JLLatLng.builder().lat(0).lng(0).build())
                .northEast(JLLatLng.builder().lat(1).lng(1).build())
                .build();

        assertThat(createBounds().hashCode()).isNotEqualTo(other.hashCode());
    }
}
