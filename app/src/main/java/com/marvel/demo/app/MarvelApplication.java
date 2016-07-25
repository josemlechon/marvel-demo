package com.marvel.demo.app;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.marvel.demo.BuildConfig;
import com.marvel.demo.R;
import com.marvel.demo.di.component.AppComponent;
import com.marvel.demo.di.component.DaggerAppComponent;
import com.marvel.demo.di.module.AppModule;
import com.marvel.demo.di.module.NetModule;
import com.marvel.demo.helper.DateBaseHelper;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by jose m lechon on 18/07/16.
 *
 * @version 0.1.0
 * @since 1
 */
public class MarvelApplication extends Application {


    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initDagger();

        initCalligraphy();

        initDDBB();
    }

    private void initDDBB(){
        FlowManager.init(new FlowConfig.Builder(this).build());

        DateBaseHelper.turnOnDebugUtils(this);
    }

    private void initDagger() {

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(BuildConfig.SERVER_URL))
                .build();

    }


    private void initCalligraphy(){
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/roboto/Roboto-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }


    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    @VisibleForTesting
    public void setAppComponent(AppComponent appComponent) {
        mAppComponent = appComponent;
    }
}
