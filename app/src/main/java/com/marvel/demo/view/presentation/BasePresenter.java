package com.marvel.demo.view.presentation;

import com.trello.navi.NaviComponent;
import com.trello.rxlifecycle.ActivityLifecycleProvider;
import com.trello.rxlifecycle.navi.NaviLifecycle;

import java.lang.ref.WeakReference;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

/**
 * Created by jose m lechon on 20/07/16.
 *
 * @version 0.1.0
 * @since 1
 */
public abstract class BasePresenter<T> {


    private ActivityLifecycleProvider mProvider;


    protected Subscription subscription = Subscriptions.empty();

    private WeakReference<T> mView;

    public final void setView(T view){
        this.mView = new WeakReference<>(view);
    }

    protected T getView(){
        return mView.get();
    }

    public void onDestroy(){
        subscription.unsubscribe();
    }

    public void setLifecycleProvider(NaviComponent naviComponent){

        mProvider = NaviLifecycle.createActivityLifecycleProvider(naviComponent);
    }

    protected <V> Observable.Transformer<V, V> getLifecycleProvider(){
        return (mProvider!=null ) ? mProvider.bindToLifecycle()  : vObservable -> vObservable;
    }




}
