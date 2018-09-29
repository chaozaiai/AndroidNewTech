package com.library.data.net;

import com.library.data.HttpResult;
import com.library.data.entity.AEMResult;
import com.library.data.entity.BookResult;

import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sam on 2018/1/31.
 */

public interface ApiService {

    @GET("v2/book/search")
    Observable<BookResult> searchBook(
            @Query("q") String q,
            @Query("tag") String tag,
            @Query("start") String start,
            @Query("count") String count
    );

    //搜索
    @GET("/solr/amway_cloud_fulltext/edismaxQ")
    Observable<AEMResult> searchAEM(
            @Query("q") String q,
            @Query("fq") String fq,
            @Query("start") String start,
            @Query("rows") String rows,
            @Query("wt") String wt,
            @Query("indent") String indent,
            @Query("sort") String sort
    );

    //标签
    @GET("/solr/amway_cloud_category/select")
    Observable<AEMResult> getArticle(
            @Query("q") String q,
            @Query("fq") String fq,
            @Query("start") String start,
            @Query("rows") String rows,
            @Query("wt") String wt,
            @Query("indent") String indent,
            @Query("sort") String sort
    );

    @Multipart
    @POST("showCircle/photoUpload")
    Observable<HttpResult<String>> photoUpload(
            @Part MultipartBody.Part part
    );
}
