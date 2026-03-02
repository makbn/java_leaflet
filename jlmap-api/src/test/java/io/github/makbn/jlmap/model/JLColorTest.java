package io.github.makbn.jlmap.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class JLColorTest {

    @Test
    void toHexString_red_should_return_FF0000() {
        assertThat(JLColor.RED.toHexString()).isEqualTo("#FF0000");
    }

    @Test
    void toHexString_green_should_return_00FF00() {
        assertThat(JLColor.GREEN.toHexString()).isEqualTo("#00FF00");
    }

    @Test
    void toHexString_blue_should_return_0000FF() {
        assertThat(JLColor.BLUE.toHexString()).isEqualTo("#0000FF");
    }

    @Test
    void toHexString_black_should_return_000000() {
        assertThat(JLColor.BLACK.toHexString()).isEqualTo("#000000");
    }

    @Test
    void toHexString_white_should_return_FFFFFF() {
        assertThat(JLColor.WHITE.toHexString()).isEqualTo("#FFFFFF");
    }

    @Test
    void toHexString_custom_color_should_convert_correctly() {
        JLColor color = new JLColor(0.5, 0.25, 0.75);
        assertThat(color.toHexString()).isEqualTo("#7F3FBF");
    }

    @Test
    void toHexString_should_pad_single_digit_hex_values() {
        JLColor color = new JLColor(0.0, 0.0, 1.0 / 255.0);
        assertThat(color.toHexString()).isEqualTo("#000001");
    }

    @Test
    void fromHex_with_hash_prefix_should_parse_red() {
        JLColor color = JLColor.fromHex("#FF0000");

        assertThat(color.getRedParameter()).isCloseTo(1.0, within(0.01));
        assertThat(color.getGreenParameter()).isCloseTo(0.0, within(0.01));
        assertThat(color.getBlueParameter()).isCloseTo(0.0, within(0.01));
    }

    @Test
    void fromHex_without_hash_prefix_should_parse_green() {
        JLColor color = JLColor.fromHex("00FF00");

        assertThat(color.getRedParameter()).isCloseTo(0.0, within(0.01));
        assertThat(color.getGreenParameter()).isCloseTo(1.0, within(0.01));
        assertThat(color.getBlueParameter()).isCloseTo(0.0, within(0.01));
    }

    @Test
    void fromHex_should_parse_blue() {
        JLColor color = JLColor.fromHex("#0000FF");

        assertThat(color.getRedParameter()).isCloseTo(0.0, within(0.01));
        assertThat(color.getGreenParameter()).isCloseTo(0.0, within(0.01));
        assertThat(color.getBlueParameter()).isCloseTo(1.0, within(0.01));
    }

    @Test
    void fromHex_should_set_opacity_to_one() {
        JLColor color = JLColor.fromHex("#ABCDEF");
        assertThat(color.getOpacity()).isEqualTo(1.0);
    }

    @Test
    void fromHex_roundtrip_should_preserve_color() {
        JLColor original = new JLColor(0.8, 0.4, 0.2);
        JLColor roundtripped = JLColor.fromHex(original.toHexString());

        assertThat(roundtripped.getRedParameter()).isCloseTo(original.getRedParameter(), within(0.01));
        assertThat(roundtripped.getGreenParameter()).isCloseTo(original.getGreenParameter(), within(0.01));
        assertThat(roundtripped.getBlueParameter()).isCloseTo(original.getBlueParameter(), within(0.01));
    }

    @Test
    void fromHex_roundtrip_should_preserve_black() {
        JLColor roundtripped = JLColor.fromHex(JLColor.BLACK.toHexString());

        assertThat(roundtripped.getRedParameter()).isCloseTo(0.0, within(0.01));
        assertThat(roundtripped.getGreenParameter()).isCloseTo(0.0, within(0.01));
        assertThat(roundtripped.getBlueParameter()).isCloseTo(0.0, within(0.01));
    }

    @Test
    void three_arg_constructor_should_default_opacity_to_one() {
        JLColor color = new JLColor(0.5, 0.5, 0.5);
        assertThat(color.getOpacity()).isEqualTo(1.0);
    }

    @Test
    void four_arg_constructor_should_set_custom_opacity() {
        JLColor color = new JLColor(0.5, 0.5, 0.5, 0.7);
        assertThat(color.getOpacity()).isCloseTo(0.7, within(0.001));
    }

    @Test
    void three_arg_constructor_should_set_rgb_components() {
        JLColor color = new JLColor(0.1, 0.2, 0.3);

        assertThat(color.getRedParameter()).isCloseTo(0.1, within(0.001));
        assertThat(color.getGreenParameter()).isCloseTo(0.2, within(0.001));
        assertThat(color.getBlueParameter()).isCloseTo(0.3, within(0.001));
    }

    @Test
    void predefined_red_should_have_correct_components() {
        assertThat(JLColor.RED.getRedParameter()).isEqualTo(1.0);
        assertThat(JLColor.RED.getGreenParameter()).isEqualTo(0.0);
        assertThat(JLColor.RED.getBlueParameter()).isEqualTo(0.0);
        assertThat(JLColor.RED.getOpacity()).isEqualTo(1.0);
    }

    @Test
    void predefined_green_should_have_correct_components() {
        assertThat(JLColor.GREEN.getRedParameter()).isEqualTo(0.0);
        assertThat(JLColor.GREEN.getGreenParameter()).isEqualTo(1.0);
        assertThat(JLColor.GREEN.getBlueParameter()).isEqualTo(0.0);
    }

    @Test
    void predefined_blue_should_have_correct_components() {
        assertThat(JLColor.BLUE.getRedParameter()).isEqualTo(0.0);
        assertThat(JLColor.BLUE.getGreenParameter()).isEqualTo(0.0);
        assertThat(JLColor.BLUE.getBlueParameter()).isEqualTo(1.0);
    }

    @Test
    void predefined_yellow_should_have_correct_components() {
        assertThat(JLColor.YELLOW.getRedParameter()).isEqualTo(1.0);
        assertThat(JLColor.YELLOW.getGreenParameter()).isEqualTo(1.0);
        assertThat(JLColor.YELLOW.getBlueParameter()).isEqualTo(0.0);
    }

    @Test
    void predefined_orange_should_have_correct_components() {
        assertThat(JLColor.ORANGE.getRedParameter()).isEqualTo(1.0);
        assertThat(JLColor.ORANGE.getGreenParameter()).isEqualTo(0.5);
        assertThat(JLColor.ORANGE.getBlueParameter()).isEqualTo(0.0);
    }

    @Test
    void predefined_purple_should_have_correct_components() {
        assertThat(JLColor.PURPLE.getRedParameter()).isEqualTo(0.5);
        assertThat(JLColor.PURPLE.getGreenParameter()).isEqualTo(0.0);
        assertThat(JLColor.PURPLE.getBlueParameter()).isEqualTo(0.5);
    }

    @Test
    void predefined_gray_should_have_correct_components() {
        assertThat(JLColor.GRAY.getRedParameter()).isEqualTo(0.5);
        assertThat(JLColor.GRAY.getGreenParameter()).isEqualTo(0.5);
        assertThat(JLColor.GRAY.getBlueParameter()).isEqualTo(0.5);
    }

    @Test
    void predefined_black_should_have_all_zero_components() {
        assertThat(JLColor.BLACK.getRedParameter()).isEqualTo(0.0);
        assertThat(JLColor.BLACK.getGreenParameter()).isEqualTo(0.0);
        assertThat(JLColor.BLACK.getBlueParameter()).isEqualTo(0.0);
    }

    @Test
    void predefined_white_should_have_all_one_components() {
        assertThat(JLColor.WHITE.getRedParameter()).isEqualTo(1.0);
        assertThat(JLColor.WHITE.getGreenParameter()).isEqualTo(1.0);
        assertThat(JLColor.WHITE.getBlueParameter()).isEqualTo(1.0);
    }
}
