package com.marvel.demo.view.presentation.home;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.cantrowitz.rxbroadcast.RxBroadcast;
import com.marvel.demo.domain.entities.Comic;
import com.marvel.demo.domain.manager.ComicManager;
import com.marvel.demo.helper.LifecycleObserver;
import com.marvel.demo.helper.NetworkHelper;
import com.marvel.demo.view.presentation.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by jose m lechon on 20/07/16.
 *
 * @version 0.1.0
 * @since 1
 */
public class HomePresenter extends BasePresenter<HomeView> {

    private Subscription subscriptionNetwork;
    private Context mContext;

    private ComicManager mComicManager;

    @Inject public HomePresenter(Context context, ComicManager comicManager){

        this.mContext = context;
        this.mComicManager = comicManager;
    }



    public void checkOnlineConnectivity() {

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        subscriptionNetwork = RxBroadcast.fromBroadcast(mContext, filter)
                .compose(getLifecycleProvider())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(intent -> {

                    if(!NetworkHelper.isNetworkAvailable(mContext)){

                        getView().onNetworkLost();
                    }

                });
    }
    public void getData(){

        getView().showLoading();

        getComics();
    }

    public void refreshData(){

        getView().showLoading();

        mComicManager.clearComics( );

        getComics();
    }

    private void getComics(){

        mComicManager.getComics()
                .compose(getLifecycleProvider())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LifecycleObserver<List<Comic>>() {

                    @Override
                    public void onError(Throwable e) {

                        getView().hideLoading();
                        getView().onException((Exception ) e);
                    }

                    @Override
                    public void onNext(List<Comic> comicList) {

                        getView().hideLoading();
                        getView().showData(comicList);

                    }
                });
    }

    public void getMoreComics(){

        mComicManager.getMoreComics()
                    .compose(getLifecycleProvider())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new LifecycleObserver<List<Comic>>() {

                        @Override
                        public void onError(Throwable e) {

                            getView().hideLoading();
                            getView().onException((Exception ) e);
                        }

                        @Override
                        public void onNext(List<Comic> comicList) {

                            getView().hideLoading();
                            getView().showData(comicList);

                        }
                    });
    }

    @Override
    public void onDestroy() {
        if (subscriptionNetwork != null) {
            subscriptionNetwork.unsubscribe();
        }

        super.onDestroy();
    }
}
