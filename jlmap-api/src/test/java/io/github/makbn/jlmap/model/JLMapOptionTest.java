package io.github.makbn.jlmap.model;

import io.github.makbn.jlmap.JLProperties;
import io.github.makbn.jlmap.map.JLMapProvider;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class JLMapOptionTest {

    @Test
    void zoomControlEnabled_should_return_true_when_parameter_present() {
        JLMapOption option = JLMapOption.builder()
                .additionalParameter(Set.of(
                        new JLMapOption.Parameter("zoomControl", "true")))
                .build();

        assertThat(option.zoomControlEnabled()).isTrue();
    }

    @Test
    void zoomControlEnabled_should_return_false_when_parameter_absent() {
        JLMapOption option = JLMapOption.builder().build();
        assertThat(option.zoomControlEnabled()).isFalse();
    }

    @Test
    void zoomControlEnabled_should_return_false_when_value_is_false() {
        JLMapOption option = JLMapOption.builder()
                .additionalParameter(Set.of(
                        new JLMapOption.Parameter("zoomControl", "false")))
                .build();

        assertThat(option.zoomControlEnabled()).isFalse();
    }

    @Test
    void zoomControlEnabled_should_return_false_for_unrelated_parameters() {
        JLMapOption option = JLMapOption.builder()
                .additionalParameter(Set.of(
                        new JLMapOption.Parameter("otherKey", "true")))
                .build();

        assertThat(option.zoomControlEnabled()).isFalse();
    }

    @Test
    void getInitialZoom_should_return_value_from_parameter() {
        JLMapOption option = JLMapOption.builder()
                .additionalParameter(Set.of(
                        new JLMapOption.Parameter("initialZoom", "15")))
                .build();

        assertThat(option.getInitialZoom()).isEqualTo(15);
    }

    @Test
    void getInitialZoom_should_return_default_when_parameter_absent() {
        JLMapOption option = JLMapOption.builder().build();
        assertThat(option.getInitialZoom()).isEqualTo(JLProperties.DEFAULT_INITIAL_ZOOM);
    }

    @Test
    void getInitialZoom_should_ignore_unrelated_parameters() {
        JLMapOption option = JLMapOption.builder()
                .additionalParameter(Set.of(
                        new JLMapOption.Parameter("otherKey", "15")))
                .build();

        assertThat(option.getInitialZoom()).isEqualTo(JLProperties.DEFAULT_INITIAL_ZOOM);
    }

    @Test
    void getInitialZoom_should_parse_different_zoom_levels() {
        JLMapOption option = JLMapOption.builder()
                .additionalParameter(Set.of(
                        new JLMapOption.Parameter("initialZoom", "1")))
                .build();

        assertThat(option.getInitialZoom()).isEqualTo(1);
    }

    @Test
    void builder_default_startCoordinate_should_use_default_lat_lng() {
        JLMapOption option = JLMapOption.builder().build();

        assertThat(option.getStartCoordinate().getLat())
                .isEqualTo(JLProperties.DEFAULT_INITIAL_LATITUDE);
        assertThat(option.getStartCoordinate().getLng())
                .isEqualTo(JLProperties.DEFAULT_INITIAL_LONGITUDE);
    }

    @Test
    void builder_default_provider_should_be_osm_mapnik() {
        JLMapOption option = JLMapOption.builder().build();

        assertThat(option.getJlMapProvider()).isNotNull();
        assertThat(option.getJlMapProvider().getName())
                .isEqualTo(JLMapProvider.getDefault().getName());
    }

    @Test
    void builder_default_additionalParameter_should_be_empty() {
        JLMapOption option = JLMapOption.builder().build();
        assertThat(option.getAdditionalParameter()).isEmpty();
    }

    @Test
    void builder_should_accept_custom_start_coordinate() {
        JLLatLng custom = JLLatLng.builder().lat(48.8566).lng(2.3522).build();
        JLMapOption option = JLMapOption.builder()
                .startCoordinate(custom)
                .build();

        assertThat(option.getStartCoordinate()).isEqualTo(custom);
    }

    @Test
    void builder_should_accept_custom_provider() {
        JLMapProvider provider = JLMapProvider.OSM_GERMAN.build();
        JLMapOption option = JLMapOption.builder()
                .jlMapProvider(provider)
                .build();

        assertThat(option.getJlMapProvider().getName()).isEqualTo("OpenStreetMap.German");
    }

    @Test
    void parameter_toString_should_format_as_key_equals_value() {
        JLMapOption.Parameter param = new JLMapOption.Parameter("zoomControl", "true");
        assertThat(param.toString()).isEqualTo("zoomControl=true");
    }

    @Test
    void parameter_toString_should_handle_empty_value() {
        JLMapOption.Parameter param = new JLMapOption.Parameter("key", "");
        assertThat(param.toString()).isEqualTo("key=");
    }

    @Test
    void parameter_should_expose_key_and_value() {
        JLMapOption.Parameter param = new JLMapOption.Parameter("myKey", "myValue");

        assertThat(param.key()).isEqualTo("myKey");
        assertThat(param.value()).isEqualTo("myValue");
    }

    @Test
    void multiple_additional_parameters_should_all_be_accessible() {
        JLMapOption option = JLMapOption.builder()
                .additionalParameter(Set.of(
                        new JLMapOption.Parameter("zoomControl", "true"),
                        new JLMapOption.Parameter("initialZoom", "10"),
                        new JLMapOption.Parameter("custom", "value")))
                .build();

        assertThat(option.getAdditionalParameter()).hasSize(3);
        assertThat(option.zoomControlEnabled()).isTrue();
        assertThat(option.getInitialZoom()).isEqualTo(10);
    }
}
