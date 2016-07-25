package com.marvel.demo.view.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.MenuItem;
import android.widget.Toast;

import com.marvel.demo.R;
import com.marvel.demo.di.component.ActivityComponent;
import com.marvel.demo.di.component.ComponentFactory;
import com.marvel.demo.domain.errors.MalformedRequestException;
import com.marvel.demo.domain.errors.NetworkException;
import com.marvel.demo.view.presentation.BaseView;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by jose m lechon on 20/07/16.
 *
 * @version 0.1.0
 * @since 1
 */
public abstract class BaseActivity extends NavyActivity   implements BaseView {


    private ActivityComponent mComponent;

    protected Unbinder unbind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        mComponent = ComponentFactory.getActivityComponent(getApplication());
        setInjection(mComponent);

        setPresenter();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        unbind = ButterKnife.bind(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                 supportFinishAfterTransition();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {

        if (unbind != null) unbind.unbind();

        onDestroyPresenter();

        super.onDestroy();
    }

    /**
     *
     */
    protected abstract void setPresenter();

    /**
     * Set
     */
    protected abstract void setInjection(ActivityComponent component);

    /**
     *
     */
    protected abstract void onDestroyPresenter();



    @Override
    public void onException(Exception ex) {

        if(ex instanceof NetworkException){

            Toast.makeText(getApplicationContext(),  R.string.no_network, Toast.LENGTH_LONG).show();

        }else{
            //We could also show a Dialog Fragment
            Toast.makeText(getApplicationContext(),R.string.error_unexpected, Toast.LENGTH_LONG ).show();
        }

    }
}
