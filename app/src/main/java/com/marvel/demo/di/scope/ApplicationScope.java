package com.marvel.demo.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by jose m lechon on 17/04/16.
 *
 * @version 0.1.0
 * @since 1
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationScope {}
