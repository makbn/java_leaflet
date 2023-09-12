package io.github.makbn.jlmap.model;

import jdk.jshell.spi.ExecutionControl;

/**
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
public class JLSvg {
    private static final String ERROR_MESSAGE = "SVG is not implemented!";
    public JLSvg() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException(ERROR_MESSAGE);
    }
}
