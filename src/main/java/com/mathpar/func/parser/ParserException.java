/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func.parser;

/**
 *
 * @author ivan
 */
public class ParserException extends RuntimeException {
    public ParserException() {
    }

    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserException(Throwable cause) {
        super(cause);
    }
}
