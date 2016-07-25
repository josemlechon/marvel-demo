package com.marvel.demo.helper;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by jose m lechon on 20/07/16.
 *
 * @version 0.1.0
 * @since 1
 *
 *   --- DEV ----
 */
public class NetModuleHelper {

    /**
     * Define all settings used only in Dev
     * @param logging
     */
    public static void setOkHttp(HttpLoggingInterceptor logging){

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

    }
}
