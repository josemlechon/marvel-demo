package com.marvel.demo.view.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.ybq.android.spinkit.SpinKitView;
import com.marvel.demo.R;
import com.marvel.demo.di.component.ActivityComponent;
import com.marvel.demo.domain.entities.Comic;
import com.marvel.demo.domain.entities.Image;
import com.marvel.demo.view.base.BaseActivity;
import com.marvel.demo.view.presentation.home.DetailComicPresenter;
import com.marvel.demo.view.presentation.home.DetailComicView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailComicActivity extends BaseActivity implements DetailComicView {


    private static final String EXTRA_ID_COMIC = DetailComicActivity.class.getCanonicalName() + "#EXTRA_ID_COMIC";


    public static Intent createIntent(Context context, Long idComic) {

        Intent intent = new Intent(context, DetailComicActivity.class);
        intent.putExtra(EXTRA_ID_COMIC, idComic);

        return intent;
    }

    private Long idComic;

    @Inject
    DetailComicPresenter mPresenter;

    //Views
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.imageview_comic)
    ImageView mImageviewComic;

    @BindView(R.id.collapsingtoolbar_detail_comic)
    CollapsingToolbarLayout mCollapsingtoolbar;

    @BindView(R.id.appbar)
    AppBarLayout mAppbar;

    @BindView(R.id.textview_title)
    TextView mTextviewTitle;
    @BindView(R.id.textview_description)
    TextView mTextviewDescription;

    @BindView(R.id.spinkitview_comic)
    SpinKitView mKitViewLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_comic);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        idComic = getIntent().getLongExtra(EXTRA_ID_COMIC, -1);


        initViews();
    }

    private void initViews() {

        ViewCompat.setTransitionName(mTextviewTitle, getString(R.string.transition_comics));

        hideLoading();

        mCollapsingtoolbar.setTitle("");
        mCollapsingtoolbar.setExpandedTitleColor(ContextCompat.getColor(getApplicationContext(), R.color.transparent));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.getComic(idComic);
    }

    @Override
    protected void setPresenter() {

        mPresenter.setView(this);
        mPresenter.setLifecycleProvider(this);
    }

    @Override
    protected void setInjection(ActivityComponent component) {
        component.inject(this);
    }

    @Override
    protected void onDestroyPresenter() {

        mPresenter.onDestroy();
    }


    @Override
    public void showComicData(Comic comic) {

        initImagePartner(comic.getRandomImage());

        mTextviewTitle.setText(comic.title);
        mTextviewDescription.setText(comic.description);
    }

    private void initImagePartner(Image image) {

        Glide.with(this)
                .load(image.getImageURL())
                .asBitmap()
                .into(new BitmapImageViewTarget(mImageviewComic) {

                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        super.onResourceReady(bitmap, glideAnimation);

                        if (bitmap != null && !bitmap.isRecycled()) {
                            Palette.from(bitmap).generate(palette -> {

                                Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();

                                int defaultColor = ContextCompat.getColor(getApplicationContext(), R.color.primary);

                                if (vibrantSwatch != null) {

                                    mCollapsingtoolbar.setContentScrimColor(vibrantSwatch.getRgb());
                                    mCollapsingtoolbar.setBackgroundColor(vibrantSwatch.getRgb());
                                    mCollapsingtoolbar.setStatusBarScrimColor(vibrantSwatch.getTitleTextColor());

                                    changeStatusBarColor(vibrantSwatch.getRgb());
                                } else {

                                    defaultColor = palette.getLightVibrantColor(defaultColor);
                                    int color = palette.getVibrantColor(defaultColor);
                                    mCollapsingtoolbar.setBackgroundColor(color);
                                    mCollapsingtoolbar.setContentScrimColor(color);

                                    changeStatusBarColor(palette.getVibrantColor(defaultColor));
                                }
                            });
                        }
                    }
                });
    }

    @Override
    public void showLoading() {
        mKitViewLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mKitViewLoading.setVisibility(View.GONE);
    }


    private void changeStatusBarColor(int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(color);

        }
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAfterTransition(this);
        super.onBackPressed();
    }
}

