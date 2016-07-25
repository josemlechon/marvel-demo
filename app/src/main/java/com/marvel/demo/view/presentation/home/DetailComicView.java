package com.marvel.demo.view.presentation.home;

import com.marvel.demo.domain.entities.Comic;
import com.marvel.demo.view.presentation.BaseView;

/**
 * Created by jose m lechon on 23/07/16.
 *
 * @version 0.1.0
 * @since 1
 */
public interface DetailComicView extends BaseView {

    void showComicData(Comic comic);
}
