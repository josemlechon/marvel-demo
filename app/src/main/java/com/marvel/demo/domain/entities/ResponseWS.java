package com.marvel.demo.domain.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by jose m lechon on 20/07/16.
 *
 * @version 0.1.0
 * @since 1
 */
public class ResponseWS<T> {

    @JsonProperty("code")
    public String code ;

    @JsonProperty("status")
    public String status;

    @JsonProperty("data")
    public Data<T> data;
}
