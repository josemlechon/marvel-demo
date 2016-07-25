package com.marvel.demo.domain.ws;

import com.marvel.demo.domain.entities.Comic;
import com.marvel.demo.domain.entities.ResponseWS;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jose m lechon on 20/07/16.
 * <p>
 * todo http://developer.marvel.com/doc
 *
 * @version 0.1.0
 * @since 1
 */
public interface ServiceApi {


    @GET("/v1/public/comics")
    Observable<ResponseWS<List<Comic>>> getComics(@Query("limit") int limit, @Query("offset") int offset);




    @GET("/v1/public/comics/{idcomic}")
    Observable<ResponseWS<List<Comic>>> getComic(@Path("idcomic") long idComic);
}
