package com.marvel.demo.helper;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.marvel.demo.R;


/**
 * @author jose m lechon on 20/07/16.
 * @version 0.1.0
 * @since 1
 */
public class ViewHelper {

    /**
     * Catch multiple click events
     *
     * @param view view clicked
     * @return true | false
     */
    public static boolean isMultipleClickDone(@NonNull View view) {

        if (view.getTag(R.id.id_view_time_last_click) == null) {

            view.setTag(R.id.id_view_time_last_click, SystemClock.elapsedRealtime());
            return Boolean.FALSE;
        }
        Long mLastClickTime = (Long) view.getTag(R.id.id_view_time_last_click);
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return Boolean.TRUE;
        }

        view.setTag(R.id.id_view_time_last_click, SystemClock.elapsedRealtime());
        return Boolean.FALSE;
    }


    /**
     * Change a view to enabled False during a second
     * then it changes again to enabled TRUE.
     * Useful for onClick events in order to avoid multiple clicks.
     *
     * @param view View you want to block
     */
    public static void blockViewShortTime(@NonNull final View view) {

        view.setEnabled(Boolean.FALSE);
        Handler handler = new Handler();

        Runnable r = new Runnable() {
            public void run() {
                view.setEnabled(Boolean.TRUE);
            }
        };
        handler.postDelayed(r, 1500);
    }

    /**
     * Hide keyboard
     *
     * @param activity activity
     */
    public static void hideKeyboard(@NonNull Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View f = activity.getCurrentFocus();
        if (null != f && null != f.getWindowToken() && EditText.class.isAssignableFrom(f.getClass()))
            imm.hideSoftInputFromWindow(f.getWindowToken(), 0);
        else
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    /**
     * Set the max num of characters an EditText can accept
     *
     * @param textView TextView or class that extends it.
     * @param limit    limit of characters
     */
    public static void setLimitCharacters(@NonNull TextView textView, int limit) {


        //Set limit Description
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(limit);
        textView.setFilters(fArray);
    }


    public static void setVisibilityView(Boolean status, View view) {
        view.setVisibility(status ? View.VISIBLE : View.GONE);

    }


}
