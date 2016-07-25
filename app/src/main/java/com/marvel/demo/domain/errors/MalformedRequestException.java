package com.marvel.demo.domain.errors;

/**
 * Created by jose m lechon on 20/07/16.
 *
 * @version 0.1.0
 * @since 1
 */
public class MalformedRequestException extends Exception {

    public MalformedRequestException(String error){
        super(error);
    }
}
