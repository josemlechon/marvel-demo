package com.marvel.demo.di.component;


import com.marvel.demo.di.scope.ActivityScope;
import com.marvel.demo.view.ui.home.DetailComicActivity;
import com.marvel.demo.view.ui.home.HomeActivity;

import dagger.Component;

/**
 * Created by jose m lechon on 29/01/16.
 *
 * @version 0.2.0
 * @since 1
 */
@ActivityScope
@Component(dependencies = {AppComponent.class})
public interface ActivityComponent extends AppComponent {


    void inject(HomeActivity activity);

    void inject(DetailComicActivity activity);
}
