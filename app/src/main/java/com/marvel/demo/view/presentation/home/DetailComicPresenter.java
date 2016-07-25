package com.marvel.demo.view.presentation.home;

import com.marvel.demo.domain.entities.Comic;
import com.marvel.demo.domain.manager.ComicManager;
import com.marvel.demo.helper.LifecycleObserver;
import com.marvel.demo.view.presentation.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by jose m lechon on 23/07/16.
 *
 * @version 0.1.0
 * @since 1
 */
public class DetailComicPresenter  extends BasePresenter<DetailComicView> {


    private final ComicManager mComicManager;

    @Inject public DetailComicPresenter(ComicManager comicManager){

        this.mComicManager = comicManager;
    }

    public void getComic(Long idComic){

        getView().showLoading();

        mComicManager.getComic( idComic)
                .compose(getLifecycleProvider())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LifecycleObserver<Comic>() {

                    @Override
                    public void onError(Throwable e) {

                        getView().hideLoading();
                        getView().onException((Exception ) e);
                    }

                    @Override
                    public void onNext(Comic comic) {

                        getView().hideLoading();
                        getView().showComicData(comic);

                    }
                });

    }

}
