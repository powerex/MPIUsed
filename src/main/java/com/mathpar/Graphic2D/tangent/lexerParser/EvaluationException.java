package com.mathpar.Graphic2D.tangent.lexerParser;


/**
 * A simple subclass of RuntimeException that indicates errors when trying to
 * evaluate an expression.
 */
public class EvaluationException extends RuntimeException {
// private static final long serialVersionUID = 4794094610927358603L;

    /**
     * Construct the evaluation exception with a message.
     *
     * @param message the message containing the cause of the exception
     */
    public EvaluationException(String message) {
        super(message);
    }
}
