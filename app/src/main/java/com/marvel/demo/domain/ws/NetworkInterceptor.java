package com.marvel.demo.domain.ws;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jose m lechon on 20/07/16.
 *
 * @version 0.1.0
 * @since 1
 */
public class NetworkInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String apiKey= "6a7ed890b4b941a925202a5630d5b162";
        String ts = "1";
        String hash = "5def722ec801d40824af072eeec86266";

        //http://gateway.marvel.com/v1/public/comics?apikey=6a7ed890b4b941a925202a5630d5b162&ts=1&hash=5def722ec801d40824af072eeec86266

        HttpUrl url = request.url().newBuilder()
                .addQueryParameter("apikey", apiKey)
                .addQueryParameter("ts", ts)
                .addQueryParameter("hash", hash)
                .build();

        request = request.newBuilder().url(url).build();
        return chain.proceed(request);
    }
}
