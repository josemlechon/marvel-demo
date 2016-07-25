package com.marvel.demo.view.presentation;

/**
 * Created by jose m lechon on 20/07/16.
 *
 * @version 0.1.0
 * @since 1
 */
public interface BaseView {

    void showLoading();

    void hideLoading();

    void onException(Exception ex);
}
