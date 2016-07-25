package com.marvel.demo.di.component;

import android.app.Application;
import android.content.Context;

import com.marvel.demo.app.MarvelApplication;
import com.marvel.demo.di.module.AppModule;
import com.marvel.demo.di.module.NetModule;
import com.marvel.demo.domain.manager.ComicManager;
import com.marvel.demo.domain.model.ComicModel;
import com.marvel.demo.domain.model.DataModel;
import com.marvel.demo.domain.model.ImageModel;
import com.marvel.demo.domain.ws.ServiceApi;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by jose m lechon on 20/07/16.
 *
 * @version 0.1.0
 * @since 1
 */

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface AppComponent {


    Application providesApplication();

    MarvelApplication providesMarvelApplication();

    Context providesContext();

    ServiceApi providerApiService();

    ComicManager providesComicManager();

    ComicModel providesComicModel();

    ImageModel providesImageModel();

    DataModel providesDataModel();


}
