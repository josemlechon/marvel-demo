package com.marvel.demo.di.component;

import android.app.Application;

import com.marvel.demo.app.MarvelApplication;


/**
 * Created by jose m lechon on 20/07/16.
 *
 * @version 0.1.0
 * @since 1
 */
public class ComponentFactory {


    public static AppComponent getAppComponent(Application context) {

        return ((MarvelApplication) context).getAppComponent();
    }


    public static ActivityComponent getActivityComponent(Application context) {

        AppComponent appComponent = ((MarvelApplication) context).getAppComponent();

        return DaggerActivityComponent.builder().appComponent(appComponent).build();
    }


}
