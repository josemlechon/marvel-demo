package com.marvel.demo.view.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marvel.demo.di.component.ActivityComponent;
import com.marvel.demo.di.component.ComponentFactory;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jose m lechon on 20/07/16
 *
 * @version 0.1.0
 * @since 1
 */
public abstract class BaseFragment extends NaviBaseFragment {


    private ActivityComponent mComponent;

    private Unbinder mUnbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mComponent = ComponentFactory.getActivityComponent(getActivity().getApplication());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( getLayoutView() , container, false);
        mUnbinder =  ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

        if(mUnbinder != null) mUnbinder.unbind();

    }

    protected abstract @LayoutRes
    int getLayoutView();


    protected ActivityComponent getComponent() {
        return mComponent;
    }

}
