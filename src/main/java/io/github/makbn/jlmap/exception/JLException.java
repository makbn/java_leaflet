package io.github.makbn.jlmap.exception;

/**
 * Internal JLMap application's Exception base class.
 * by: Mehdi Akbarian Rastaghi (@makbn)
 */

public class JLException extends RuntimeException{
    public JLException(String message) {
        super(message);
    }
}
