package com.marvel.demo.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.marvel.demo.domain.entities.ResponseWS;
import com.marvel.demo.domain.errors.ErrorException;
import com.marvel.demo.domain.errors.MalformedRequestException;
import com.marvel.demo.domain.errors.NetworkException;
import com.marvel.demo.domain.errors.UnknownException;
import com.marvel.demo.domain.model.DataModel;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.functions.Func1;

import static com.marvel.demo.utils.L.LOGD;
import static com.marvel.demo.utils.L.LOGE;

/**
 * Created by jose m lechon on 20/07/16.
 *
 * @version 0.1.0
 * @since 1
 */
public class NetworkHelper {

    public static final String TAG = NetworkHelper.class.getSimpleName();


    public static <T> Observable.Transformer<T, T> parseHttpErrors() {


        return observable -> observable.onErrorResumeNext(
                (Throwable throwable) -> {


                    if (throwable instanceof IOException) {

                        return Observable.error(new NetworkException());

                    } else if (throwable instanceof HttpException) {

                        int errorCode = ((HttpException) throwable).response().code();

                        switch (errorCode) {
                            case 400: // Bad Request
                                LOGE(TAG, "MALFORMED request: ", throwable);
                                return Observable.error(new MalformedRequestException(throwable.getMessage()));
                            case 403: // Forbidden
//                    return Observable.error(new SessionException());
                            case 404: // ¿?
                            case 408: // Request Timeout
                            case 500: // Internal Server Error
                            default:

                                return Observable.error(new UnknownException(throwable.getMessage()));
                        }
                    }

                    return Observable.error(throwable);

                });
    }


    @NonNull
    public static Func1<Observable<? extends Throwable>, Observable<?>> getRetryWhenNoNetwork(@IntRange(from = 2) int numIterations) {


        final int totalIterations = numIterations +1;
        return errors ->
                errors.flatMap(error -> {

                    if (error instanceof IOException || error instanceof NetworkException) {
                        // For IOExceptions, we  retry
                        LOGD(TAG, "Flatmap retry");
                        return Observable.just(null);
                    } else {
                        // For anything else, don't retry
                        LOGD(TAG, "Flatmap normal error");
                        return Observable.error(error);
                    }
                }).zipWith( Observable.range(1, totalIterations ), (n, i) -> {
                    LOGD(TAG, "Num iterations " + i);
                    return i;
                })
                .flatMap(retryCount -> {
                    if (retryCount == totalIterations ) {
                        return Observable.error(new NetworkException());
                    } else {
                        return Observable.timer((long) Math.pow(5, retryCount), TimeUnit.SECONDS);
                    }
                    });
    }

    /**
     * Simple logging to let us know what each source is returning
     */
    public static <T> Observable.Transformer<T, T> logSource(final String source) {
        return dataObservable -> dataObservable.doOnNext(data -> {
            if (data == null) {
                System.out.println(source + " does not have any data.");
            }
//            else if (!data.isUpToDate()) {
//                System.out.println(source + " has stale data.");
//            }
            else {
                System.out.println(source + " has the data you are looking for!");
            }
        });
    }


    @NonNull
    public static <T> Func1<ResponseWS<T>, Observable<T>> storeDataPageIfValid(DataModel dataModel, @DataModel.DataPages String name) {

        return response -> {

            if (response.data != null && response.data.result != null) {

                dataModel.save(response.data, name);

                return Observable.just(response.data.result);

            } else {
                return Observable.error(new ErrorException());
            }

        };
    }


    @NonNull
    public static <T> Func1<ResponseWS<T>, Observable<T>> checkIfValidData() {

        return response -> {

            if (response.data != null && response.data.result != null) {

                return Observable.just(response.data.result);

            } else {
                return Observable.error(new ErrorException());
            }

        };
    }

    public static boolean isNetworkAvailable(@NonNull Context context) {

        if (context == null) {
            return false;
        } else {
            final ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            return (activeNetworkInfo != null) && activeNetworkInfo.isConnected();
        }
    }


    public static String md5(String s) {
        try {

            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            LOGE(TAG, "error md5", e);
        }
        return "";

    }
}
