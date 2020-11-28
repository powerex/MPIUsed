package com.mathpar.Graphic2D.paintElements.GeometryEngine.error;

public class ParseException extends EngineException {
    private static final long serialVersionUID = -5365630128856068164L + 25L;

    public ParseException(String s) {
        super(s);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseException(Throwable cause) {
        super(cause);
    }

    public static ParseException at(int location, String expected, String got) {
        return new ParseException(String.format(
                "Parse error at [%s]: expected - '%s', got - '%s'",
                location, expected, got
        ));
    }

    public static ParseException at(int location, String message) {
        return new ParseException(String.format(
                "Parse error at [%s]: %s",
                location, message
        ));
    }
}
