package io.github.makbn.jlmap.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Color abstraction for map styling.
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JLColor {
    private double red;
    private double green;
    private double blue;
    private double opacity;

    public JLColor(double red, double green, double blue) {
        this(red, green, blue, 1.0);
    }

    /**
     * Converts the color to a hex string representation.
     * @return hex color string (e.g., "#FF0000")
     */
    public String toHexString() {
        int r = (int) (red * 255);
        int g = (int) (green * 255);
        int b = (int) (blue * 255);
        return String.format("#%02X%02X%02X", r, g, b);
    }

    /**
     * Creates a color from hex string.
     * @param hex hex color string (e.g., "#FF0000")
     * @return JLColor instance
     */
    public static JLColor fromHex(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return new JLColor(r / 255.0, g / 255.0, b / 255.0, 1.0);
    }

    // Predefined colors
    public static final JLColor RED = new JLColor(1.0, 0.0, 0.0);
    public static final JLColor GREEN = new JLColor(0.0, 1.0, 0.0);
    public static final JLColor BLUE = new JLColor(0.0, 0.0, 1.0);
    public static final JLColor BLACK = new JLColor(0.0, 0.0, 0.0);
    public static final JLColor WHITE = new JLColor(1.0, 1.0, 1.0);
    public static final JLColor YELLOW = new JLColor(1.0, 1.0, 0.0);
    public static final JLColor ORANGE = new JLColor(1.0, 0.5, 0.0);
    public static final JLColor PURPLE = new JLColor(0.5, 0.0, 0.5);
    public static final JLColor GRAY = new JLColor(0.5, 0.5, 0.5);
} 