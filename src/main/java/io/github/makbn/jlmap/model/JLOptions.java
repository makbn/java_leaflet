package io.github.makbn.jlmap.model;

import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Optional value for theming objects inside the map!
 * Note that all options are not available for all objects!
 * Read more at Leaflet Official Docs.
 * by: Mehdi Akbarian Rastaghi (@makbn)
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class JLOptions {

    /** Default value for theming options. */
    public final static JLOptions DEFAULT = JLOptions.builder().build();

    @Builder.Default
    /** Stroke color. Default is {{@link Color#BLUE}} */
    private Color color = Color.BLUE;

    @Builder.Default
    /** Fill color. Default is {{@link Color#BLUE}} */
    private Color fillColor = Color.BLUE;

    @Builder.Default
    /** Stroke width in pixels. Default is 3 */
    private int weight = 3;

    @Builder.Default
    /** Whether to draw stroke along the path. Set it to false to disable borders on polygons or circles. */
    private boolean stroke = true;

    @Builder.Default
    /** Whether to fill the path with color. Set it to false to disable filling on polygons or circles. */
    private boolean fill = true;

    @Builder.Default
    /** Stroke opacity */
    private double opacity = 1.0;

    @Builder.Default
    /** Fill opacity. */
    private double fillOpacity = 0.2;

    @Builder.Default
    /** How much to simplify the polyline on each zoom level.
     * More means better performance and smoother look, and less means more accurate representation.
     */
    private double smoothFactor = 1.0;

    @Builder.Default
    /** Controls the presence of a close button in the popup. */
    private boolean closeButton = true;

    @Builder.Default
    /** Set it to false if you want to override the default behavior of the popup closing when another popup is opened. */
    private boolean autoClose = true;

    @Builder.Default
    /** Whether the marker is draggable with mouse/touch or not. */
    private boolean draggable = false;

}
