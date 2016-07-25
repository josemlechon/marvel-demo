package com.marvel.demo.view.presentation.home;

import android.support.annotation.NonNull;

import com.marvel.demo.domain.entities.Comic;
import com.marvel.demo.view.presentation.BaseView;

import java.util.List;

/**
 * Created by jose m lechon on 18/07/16.
 *
 * @version 0.1.0
 * @since 1
 */
public interface HomeView extends BaseView {

    void onNetworkLost();

    void showData(@NonNull  List<Comic> comicList);

    void showMoreData(@NonNull  List<Comic> comicList);
}
