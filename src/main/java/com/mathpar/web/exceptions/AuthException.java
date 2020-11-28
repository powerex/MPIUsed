/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.web.exceptions;

/**
 * Authentication exception.
 *
 * @author ivan
 */
public class AuthException extends MathparException {
    public AuthException() {
    }

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthException(Throwable cause) {
        super(cause);
    }
}
