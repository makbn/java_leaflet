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
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class JLOptions {

    /** Default value for theming options. */
    public static final JLOptions DEFAULT = JLOptions.builder().build();

    /** Stroke color. Default is {{@link Color#BLUE}} */
    @Builder.Default
    private Color color = Color.BLUE;

    /** Fill color. Default is {{@link Color#BLUE}} */
    @Builder.Default
    private Color fillColor = Color.BLUE;

    /** Stroke width in pixels. Default is 3 */
    @Builder.Default
    private int weight = 3;

    /**
     * Whether to draw stroke along the path. Set it to false for disabling
     * borders on polygons or circles.
     */
    @Builder.Default
    private boolean stroke = true;

    /** Whether to fill the path with color. Set it to false fo disabling
     * filling on polygons or circles.
     */
    @Builder.Default
    private boolean fill = true;

    /** Stroke opacity */
    @Builder.Default
    private double opacity = 1.0;

    /** Fill opacity. */
    @Builder.Default
    private double fillOpacity = 0.2;

    /** How much to simplify the polyline on each zoom level.
     * greater value means better performance and smoother
     * look, and smaller value means more accurate representation.
     */
    @Builder.Default
    private double smoothFactor = 1.0;

    /** Controls the presence of a close button in the popup.
     */
    @Builder.Default
    private boolean closeButton = true;

    /** Set it to false if you want to override the default behavior
     * of the popup closing when another popup is opened.
     */
    @Builder.Default
    private boolean autoClose = true;

    /** Whether the marker is draggable with mouse/touch or not.
     */
    @Builder.Default
    private boolean draggable = false;

}
