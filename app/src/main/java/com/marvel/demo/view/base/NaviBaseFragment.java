package com.marvel.demo.view.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.navi.Event;
import com.trello.navi.Listener;
import com.trello.navi.NaviComponent;
import com.trello.navi.internal.NaviEmitter;

/**
 * Created by jose m lechon on 20/07/16
 *
 * @version 0.1.0
 * @since 1
 */
public  abstract class NaviBaseFragment extends Fragment implements NaviComponent {

    private final NaviEmitter base = NaviEmitter.createFragmentEmitter();

    @Override public boolean handlesEvents(Event... events) {
        return base.handlesEvents(events);
    }

    @Override public <T> void addListener(Event<T> event, Listener<T> listener) {
        base.addListener(event, listener);
    }

    @Override public <T> void removeListener(Listener<T> listener) {
        base.removeListener(listener);
    }

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        base.onAttach(activity);
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        base.onAttach(context);
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        base.onCreate(savedInstanceState);
    }

    @Nullable
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        base.onCreateView(savedInstanceState);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        base.onActivityCreated(savedInstanceState);
    }

    @Override public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        base.onViewStateRestored(savedInstanceState);
    }

    @Override public void onStart() {
        super.onStart();
        base.onStart();
    }

    @Override public void onResume() {
        super.onResume();
        base.onResume();
    }

    @Override public void onPause() {
        base.onPause();
        super.onPause();
    }

    @Override public void onStop() {
        base.onStop();
        super.onStop();
    }

    @Override public void onDestroyView() {
        base.onDestroyView();
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        base.onDestroy();
        super.onDestroy();
    }

    @Override public void onDetach() {
        base.onDetach();
        super.onDetach();
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        base.onSaveInstanceState(outState);
    }

    @Override public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        base.onConfigurationChanged(newConfig);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        base.onActivityResult(requestCode, resultCode, data);
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        base.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
