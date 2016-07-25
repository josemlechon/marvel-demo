
package com.marvel.demo.domain.errors;

/**
 * @author jose m lechon on 23/07/2016
 * @version 1.0.0
 * @since 1
 */
public class ValidationFailedException extends RuntimeException {

    public ValidationFailedException() {
    }

    public ValidationFailedException(String detailMessage) {
        super(detailMessage);
    }
}
