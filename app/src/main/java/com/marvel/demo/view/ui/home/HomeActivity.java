package com.marvel.demo.view.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.endless.Endless;
import com.marvel.demo.R;
import com.marvel.demo.di.component.ActivityComponent;
import com.marvel.demo.domain.entities.Comic;
import com.marvel.demo.domain.errors.NetworkException;
import com.marvel.demo.view.base.BaseActivity;
import com.marvel.demo.view.presentation.home.HomePresenter;
import com.marvel.demo.view.presentation.home.HomeView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author jose m lechon 21/07/2016
 * @since 1
 */
public class HomeActivity extends BaseActivity implements HomeView, HomeAdapter.OnCallback, Endless.LoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    static final String TAG = HomeActivity.class.getSimpleName();

    //DI
    @Inject
    HomePresenter mHomePresenter;

    private HomeAdapter mAdapter;
    private Endless endlessRecycler;

    //Views
    @BindView(R.id.recyclerview_home)
    RecyclerView mRecyclerView;

    @BindView(R.id.swiperefresh_home)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.spinkitview_home)
    SpinKitView mKitViewLoading;

    @BindView(R.id.imageview_messages)
    ImageView mImageviewMessages;
    @BindView(R.id.viewflipper_messages)
    ViewFlipper mViewflipperMessages;

    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        initUI();

        isTablet = getResources().getBoolean(R.bool.isTablet);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mHomePresenter.getData();
        mHomePresenter.checkOnlineConnectivity();
    }

    private void initUI() {

        hideLoading();

        setSupportActionBar(mToolbar);

        mAdapter = new HomeAdapter();
        mAdapter.setCallback(this);

        if (isTablet) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, LinearLayoutManager.VERTICAL, Boolean.FALSE));
        } else {

            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        mRecyclerView.setHasFixedSize(Boolean.TRUE);
        mRecyclerView.setAdapter(mAdapter);

        View loadingView = View.inflate(this, R.layout.item_loading, null);

        endlessRecycler = Endless.applyTo(mRecyclerView, loadingView);
        endlessRecycler.setLoadMoreListener(this);

        mSwipeRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    protected void setPresenter() {

        mHomePresenter.setView(this);
        mHomePresenter.setLifecycleProvider(this);
    }

    @Override
    protected void setInjection(ActivityComponent component) {

        component.inject(this);
    }

    @Override
    protected void onDestroyPresenter() {
        mHomePresenter.onDestroy();
    }

    @Override
    public void showLoading() {
        mKitViewLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mKitViewLoading.setVisibility(View.GONE);
    }


    @Override
    public void showData(@NonNull List<Comic> comicList) {

        mViewflipperMessages.setDisplayedChild(0);

        mAdapter.setData(comicList);
        //Unless we call this method the adapter is not notified
        endlessRecycler.loadMoreComplete();
    }

    @Override
    public void showMoreData(@NonNull List<Comic> comicList) {

        mAdapter.addData(comicList);
        endlessRecycler.loadMoreComplete();
    }

    @Override
    public void onComicSelected(Comic comic, View viewAnimation) {

        Intent intent = DetailComicActivity.createIntent(getApplicationContext(), comic.idComic);

        String transitionName = getString(R.string.transition_comics);

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, viewAnimation, transitionName);

        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    @Override
    public void onLoadMore(int numPage) {

        mHomePresenter.getMoreComics();
    }

    @Override
    public void onException(Exception ex) {

        //Stop loading views
        endlessRecycler.loadMoreComplete();
        hideLoading();

        //show image if no items on the list
        if(ex instanceof NetworkException && mAdapter.getItemCount() == 0){

            showNoNetworkImage();

        }else if (mAdapter.getItemCount() == 0){

           showErrorImage();
        }


        super.onException(ex);
    }

    private void showNoNetworkImage(){

        mViewflipperMessages.setDisplayedChild(1);
        Glide.with(this)
                .load(R.drawable.no_network)
                .into(mImageviewMessages);
    }

    private void showErrorImage(){

        mViewflipperMessages.setDisplayedChild(1);
        Glide.with(this)
                .load(R.drawable.error)
                .into(mImageviewMessages);
    }

    @Override
    public void onRefresh() {

        mSwipeRefreshLayout.setRefreshing(Boolean.FALSE);
        mHomePresenter.refreshData();
    }


    @Override
    public void onNetworkLost() {
        //Toast.makeText(getApplicationContext(), R.string.warning_no_network, Toast.LENGTH_LONG).show();
        Snackbar.make(mImageviewMessages, R.string.warning_no_network, Snackbar.LENGTH_LONG).show();

        if(mAdapter.getItemCount() == 0) showNoNetworkImage();
    }

}
