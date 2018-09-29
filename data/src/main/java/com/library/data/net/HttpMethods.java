package com.library.data.net;

import com.library.data.HttpResult;
import com.library.data.RxUtils;
import com.library.data.entity.AEMResult;
import com.library.data.entity.ArticleEntity;
import com.library.data.entity.BookResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sam on 2018/1/31.
 */

public class HttpMethods {

    private static final int DEFAULT_TIMEOUT_SECONDS = 60;
    private static final int DEFAULT_READ_TIMEOUT_SECONDS = 60;
    private static final int DEFAULT_WRITE_TIMEOUT_SECONDS = 60;
//    上面提到的配置，是配置的哪些内容呢？
    private ApiService apiService;
    private Retrofit retrofit;
    String Url = "https://searchqa.amwaynet.com.cn/solr/amway_cloud_fulltext/edismaxQ?" +
            "q=((title: 钙镁片^1000.0) OR (description: 钙镁片) " +
            "OR (strippedContent: 钙镁片) OR (metatag.keywords: 钙镁片)" +
            " OR (metatag.aemtags: 钙镁片))&fq=-id:*.pdf AND -id:*.doc AND" +
            " -id:*.docx -id:*.ppt AND -id:*.pptx AND -id:*.mp3 " +
            "AND -id:*.mp4 AND -(id:*/product/* AND productImageUrl:*) " +
            "AND -(id:*/content/china/accl/aaworkshop/* AND " +
            "-id:*/productdetail.*) AND (metatag.aemtags : " +
            "钙镁片OR metatag.keywords: 钙镁片) & sort=score " +
            "desc&rows=10&start=0&wt=json&indent=true";
    private static final String BASE_URL = "https://searchqa.amwaynet.com.cn";

    private static volatile HttpMethods Instance;

    private HttpMethods() {
        init();
    }

    public static HttpMethods getInstance() {
        if (null == Instance) {
            synchronized (HttpMethods.class) {
                if (Instance == null) {
                    Instance = new HttpMethods();
                }
            }
        }
        return Instance;
    }

    public Observable<BookResult> searchBook(String keyword, String tag, String start, String count) {
        return apiService.searchBook(keyword, tag, start, count)
                .compose(RxUtils.<BookResult>rxSchedulerHelper())
                .onErrorResumeNext(new HttpExceptionFunc<BookResult>());
    }

    public Observable<AEMResult> searchAEM(String q, String fq, String start,
                                            String rows, String wt, String indent,
                                            String sort) {
        return apiService.searchAEM(q, fq, start, rows,wt,indent,sort)
                .compose(RxUtils.<AEMResult>rxSchedulerHelper())
                .onErrorResumeNext(new HttpExceptionFunc<AEMResult>());
    }


    public Observable<AEMResult> getArticle(String q, String fq, String start,
                                            String rows, String wt, String indent,
                                            String sort) {
        return apiService.getArticle(q, fq, start, rows,wt,indent,sort)
                .compose(RxUtils.<AEMResult>rxSchedulerHelper())
                .onErrorResumeNext(new HttpExceptionFunc<AEMResult>());
    }


    private void init() {
        OkHttpClient okHttpClient = getOkhttpClient();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        apiService = retrofit.create(ApiService.class);
    }


    private class HttpResultFunctionData<T> implements Func1<HttpResult<T>, T> {
        @Override
        public T call(HttpResult<T> httpResult) {
            return httpResult.getData();
        }
    }

    private class HttpExceptionFunc<T> implements Func1<Throwable, Observable<T>> {
        @Override
        public Observable<T> call(Throwable throwable) {
            return Observable.error(throwable);
//            return Observable.error(ExceptionHandle.handleException(throwable));
        }
    }

    private OkHttpClient getOkhttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        //通一参数 增加共同的参数
//        basicParamsInterceptor =
//                new BasicParamsInterceptor.Builder()
//                        .addParam(KEY_TOKEN, TOKEN)
//                        .addParam(KEY_CONSUMER_ID, CONSUMER_ID)
//                        .build();
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .retryOnConnectionFailure(false)
                .readTimeout(DEFAULT_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build();
    }


}
