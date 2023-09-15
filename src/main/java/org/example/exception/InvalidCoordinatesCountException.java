package org.example.exception;

/***
 * Возникает в случае, если запрошено одно количество точек,
 * а передано другое.
 */
public class InvalidCoordinatesCountException extends Exception {
    public InvalidCoordinatesCountException() {
        super();
    }
}
