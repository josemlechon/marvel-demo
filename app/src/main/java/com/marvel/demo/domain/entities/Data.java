package com.marvel.demo.domain.entities;

import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marvel.demo.domain.errors.ValidationFailedException;
import com.marvel.demo.domain.model.AppDatabase;
import com.marvel.demo.domain.model.Validation;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by jose m lechon on 20/07/16.
 *
 * @version 0.1.0
 * @since 1
 */

@Table(database = AppDatabase.class)
public class Data<T>  extends BaseModel implements Validation{

    @Column
    @PrimaryKey
    public Long id;

    @Column
    public String name;

    @Column
    @JsonProperty("offset")
    public int offset;

    @Column
    @JsonProperty("limit")
    public int limit;

    @Column
    @JsonProperty("total")
    public long total;

    @Column
    @JsonProperty("count")
    public int count;

    @JsonProperty("results")
    public T result;

    @Override
    public void validate() {

        if(TextUtils.isEmpty(name)) throw new ValidationFailedException("No valid name for Data");
    }
}
