package com.marvel.demo.di.module;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.marvel.demo.app.MarvelApplication;
import com.marvel.demo.domain.manager.ComicManager;
import com.marvel.demo.domain.model.AppDatabase;
import com.marvel.demo.domain.model.ComicModel;
import com.marvel.demo.domain.model.DataModel;
import com.marvel.demo.domain.model.ImageModel;
import com.marvel.demo.domain.ws.ServiceApi;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jose m lechon on 20/07/16.
 *
 * @version 0.1.0
 * @since 1
 */

@Module
public class AppModule {


    private final MarvelApplication mApplication;

    public AppModule(MarvelApplication app) {
        mApplication = app;
    }

    @Provides
    @Singleton
    public Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    public MarvelApplication providesMarvelApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    public  Context providesContext() {
        return mApplication.getApplicationContext();
    }


    @Provides
    @Singleton
    public DatabaseWrapper providesDatabase() {
        return FlowManager.getDatabase(AppDatabase.NAME).getWritableDatabase();
    }


    @Provides
    @Singleton
    public ComicManager providesComicManager(ServiceApi api, ComicModel model , DataModel dataModel,  Context context) {
        return new ComicManager(api, model,   dataModel, context);
    }


    @Provides
    @Singleton
    public ComicModel providesComicModel(ImageModel imageModel, MarvelApplication app, DatabaseWrapper ddbb ){
        return new ComicModel(app, ddbb , imageModel );
    }

    @Provides
    @Singleton
    public ImageModel providesImageModel(MarvelApplication app, DatabaseWrapper ddbb ){
        return new ImageModel(app, ddbb);
    }

    @Provides
    @Singleton
    public DataModel providesDataModel(MarvelApplication app, DatabaseWrapper ddbb ){
        return new DataModel(app, ddbb);
    }


}

