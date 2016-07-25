package com.marvel.demo.helper;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by jose m lechon on 24/07/16.
 *
 * @version 0.1.0
 * @since 1
 */
public class DateBaseHelper {



    public static void turnOnDebugUtils(Application application){

        initStetho(application);
    }


    /**
     * todo read https://code.facebook.com/posts/393927910787513/stetho-a-new-debugging-platform-for-android/
     * todo read https://github.com/facebook/stetho
     */
    private static void initStetho(Application application) {

        Stetho.initialize(
                Stetho.newInitializerBuilder(application)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(application))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(application))
                        .build());
    }
}
