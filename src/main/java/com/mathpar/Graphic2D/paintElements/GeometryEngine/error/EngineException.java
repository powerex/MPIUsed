package com.mathpar.Graphic2D.paintElements.GeometryEngine.error;

public class EngineException extends Exception {
    private static final long serialVersionUID = -5365630128856068164L + 235L;

    EngineException(String s) {
        super(s);
    }

    EngineException(String message, Throwable cause) {
        super(message, cause);
    }

    EngineException(Throwable cause) {
        super(cause);
    }
}
