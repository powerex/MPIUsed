package com.mathpar.Graphic2D.paintElements.GeometryEngine.error;

public class OperatorException extends EngineException {
    private static final long serialVersionUID = -5365630128856068164L + 167L;

    public OperatorException(String s) {
        super(s);
    }

    public OperatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public OperatorException(Throwable cause) {
        super(cause);
    }
}
