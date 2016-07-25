package com.marvel.demo.di.module;

import android.app.Application;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.marvel.demo.domain.ws.NetworkInterceptor;
import com.marvel.demo.domain.ws.ServiceApi;
import com.marvel.demo.helper.NetModuleHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.marvel.demo.utils.L.LOGD;


/**
 * Created by jose m lechon on 20/07/16.
 *
 * @version 0.1.0
 * @since 1
 */
@Module
public class NetModule {


    String mBaseUrl;

    public NetModule(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(Cache cache) {


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(
                message -> LOGD("OkHttp",message));

        NetModuleHelper.setOkHttp(logging);

        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(new NetworkInterceptor())
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    Converter.Factory provideFactory() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        mapper.setAnnotationIntrospector(new IgnoreInheritedIntrospector());

        return JacksonConverterFactory.create(mapper);
    }

    @Provides
    @Singleton
    public Retrofit providerRetrofit(OkHttpClient okHttpClient, Converter.Factory factoryMapper) {


        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(mBaseUrl)
                .addCallAdapterFactory( RxJavaCallAdapterFactory.create() )
                .addConverterFactory( factoryMapper )
                .build();

    }

    @Provides
    @Singleton
    public ServiceApi providerApiService(Retrofit retrofit) {

        return retrofit.create(ServiceApi.class);
    }


    private static class IgnoreInheritedIntrospector extends JacksonAnnotationIntrospector {
        @Override
        public boolean hasIgnoreMarker(final AnnotatedMember m) {
            return super.hasIgnoreMarker(m);
        }
    }


}
