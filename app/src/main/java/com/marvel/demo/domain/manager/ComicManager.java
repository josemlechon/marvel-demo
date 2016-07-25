package com.marvel.demo.domain.manager;

import android.content.Context;
import android.support.annotation.NonNull;

import com.marvel.demo.domain.entities.Comic;
import com.marvel.demo.domain.entities.Data;
import com.marvel.demo.domain.entities.ResponseWS;
import com.marvel.demo.domain.errors.ErrorException;
import com.marvel.demo.domain.model.ComicModel;
import com.marvel.demo.domain.model.DataModel;
import com.marvel.demo.domain.ws.ServiceApi;
import com.marvel.demo.helper.NetworkHelper;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jose m lechon on 20/07/16.
 * <p>
 * Todo test http://gateway.marvel.com/v1/public/comics?apikey=6a7ed890b4b941a925202a5630d5b162&ts=1&hash=5def722ec801d40824af072eeec86266
 *
 * @version 0.1.0
 * @since 1
 */
public class ComicManager {

    private static final String TAG = ComicManager.class.getSimpleName();

    private static final int MAX_RETRY_NET_CALLS = 2;
    final Context mContext;

    final ServiceApi mApiService;

    final ComicModel mComicModel;
    final DataModel mDataModel;

    private static final int LIMIT_COMICS_REQUEST = 20;


    public ComicManager(final @NonNull ServiceApi apiService, ComicModel comicModel, DataModel dataModel, Context context) {

        this.mApiService = apiService;
        this.mComicModel = comicModel;
        this.mContext = context;
        this.mDataModel = dataModel;
    }


    public Observable<List<Comic>> getComics() {


        Data data = mDataModel.getData(DataModel.NAME_COMIC);
        if (data == null || data.offset == 0) {
            return getComicsByNetwork(0);
        } else {
            return getComicsStored();
        }

    }

    public Observable<List<Comic>> getMoreComics() {


        Data data = mDataModel.getData(DataModel.NAME_COMIC);
        if (data == null) {
            return getComicsByNetwork(0);
        } else {
            return getComicsByNetwork(data.offset + data.count);
        }
    }

    public Observable<Comic> getComic(Long idComic) {

        // In case the Comic data is not in the DDBB,
        // we create a sequence for querying either net and DDBB
        return Observable
                .concat(
                        getComicStored(idComic),
                        getComicByNetwork(idComic)
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .first(data -> data != null );


    }

    public void clearComics() {

        mComicModel.cleanTable();
        mDataModel.delete(DataModel.NAME_COMIC);
    }

    private Observable<List<Comic>> getComicsByNetwork(int offset) {


        return mApiService.getComics(LIMIT_COMICS_REQUEST, offset)
                .subscribeOn(Schedulers.io())
                .compose(NetworkHelper.<ResponseWS<List<Comic>>>parseHttpErrors())
                .retryWhen(NetworkHelper.getRetryWhenNoNetwork(MAX_RETRY_NET_CALLS))
                .flatMap(NetworkHelper.<List<Comic>>storeDataPageIfValid(mDataModel, DataModel.NAME_COMIC))
                .flatMap(data -> {

                    mComicModel.saveAll(data);

                    List<Comic> dataSaved = mComicModel.getAllComics();

                    return Observable.just(dataSaved);
                });

    }


    public Observable<List<Comic>> getComicsStored() {

        return Observable.create(subscriber -> {

            subscriber.onNext(mComicModel.getAllComics());
            subscriber.onCompleted();
        });
    }


    public Observable<Comic> getComicStored(Long idComic) {

        return Observable.create(subscriber -> {

            subscriber.onNext(mComicModel.getComic(idComic));
            subscriber.onCompleted();
        });
    }


    public Observable<Comic> getComicByNetwork(Long idComic) {

        return mApiService.getComic(idComic)
                .subscribeOn(Schedulers.io())
                .compose(NetworkHelper.<ResponseWS<List<Comic>>>parseHttpErrors())
                .retryWhen(NetworkHelper.getRetryWhenNoNetwork(MAX_RETRY_NET_CALLS))
                .flatMap(NetworkHelper.<List<Comic>>checkIfValidData())
                .flatMap(data->{

                    if(data.isEmpty() ) return null;
                    return Observable.just(data.get(0));
                })
                .observeOn(AndroidSchedulers.mainThread());
    }


}
