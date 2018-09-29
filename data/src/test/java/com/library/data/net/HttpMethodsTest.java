package com.library.data.net;

import com.library.data.entity.AEMResult;
import com.library.data.entity.BookResult;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import rx.Observer;

/**
 * Created by sam on 2018/1/31.
 */

public class HttpMethodsTest {

    private Gson gson = new Gson();

    @Before
    public void setup() {
        RxTestHelper.registJavaImmediate();
        RxTestHelper.registAndroidImmediate();
    }

    @After
    public void tearDown() {
        RxTestHelper.rxReset();
    }

    @Test
    public void searchBook() {
        String keyword = "电商";
        String tag = "";
        String start = "1";
        String count = "10";
        HttpMethods.getInstance()
                .searchBook(keyword, tag, start, count).subscribe(new Observer<BookResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                System.out.print(throwable.getMessage());
            }

            @Override
            public void onNext(BookResult bookResult) {
                System.out.println(gson.toJson(bookResult));

            }
        });
    }

    @Test
    public void testGetArticle(){
        String q = "*:*";
        String fq = "-id:*.pdf AND -id:*.doc AND -id:*.docx -id:*.ppt AND -id:*.pptx " +
                "AND -id:*.mp3 AND -id:*.mp4 AND -(id:*/product/* AND productImageUrl:*) " +
                "AND ((metatag.keywords:%s OR metatag.aemtags:%s)) " +
                "AND -aboReadOnly:1 AND -allowShare:0 AND " +
                "-id:*/content/china/accl/aaworkshop/mobile/clientpages/productdetail*";
        String start = "0";
        String rows = "10";
        String wt = "json";
        String indent = "true";
        String sort = "if(exists(query({!v='id:*/productdetail.*'})),0,1) asc,if(exists(query({!v='(metatag.keywords:钙镁片-置顶文章 OR metatag.aemtags:钙镁片-置顶文章)'})),0,1) asc,if(exists(query({!v='id:http?//www.amwayfoundation.org*'})),0,1) desc,score desc,metatag.publishdate desc";


        String tag = "钙镁片";
        String qQ = String.format(fq,tag,tag);

        HttpMethods.getInstance().getArticle(q,qQ,start,rows,wt,indent,sort)
                .subscribe(new Observer<AEMResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(AEMResult aemResult) {
                        System.out.println(gson.toJson(aemResult));
                    }
                });

    }

    @Test
    public void testSearchAEM(){
        String q = "*:*";
        String fq = "-id:*.pdf AND -id:*.doc AND -id:*.docx -id:*.ppt AND -id:*.pptx " +
                "AND -id:*.mp3 AND -id:*.mp4 AND -(id:*/product/* AND productImageUrl:*) " +
                "AND ((metatag.keywords: %s OR metatag.aemtags: %s)) " +
                "AND -aboReadOnly:1 AND -allowShare:0 AND " +
                "-id:*/content/china/accl/aaworkshop/mobile/clientpages/productdetail*";
        String start = "0";
        String rows = "10";
        String wt = "json";
        String indent = "true";
        String sort = "if(exists(query({!v='id:*/productdetail.*'})),0,1) asc," +
                "if(exists(query({!v='(metatag.keywords: %s " +
                "OR metatag.aemtags: %s)'})),0,1) " +
                "asc,if(exists(query({!v='id:http?//www.amwayfoundation.org*'})),0,1) desc,score desc," +
                "metatag.publishdate desc";

        String tag = "钙镁片";
        String qQ = String.format(fq,tag,tag);
        String sortQ = String.format(sort, tag, tag);
        HttpMethods.getInstance().searchAEM(q,qQ,start,rows,wt,indent,sortQ)
                .subscribe(new Observer<AEMResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(AEMResult aemResult) {
                        System.out.println(gson.toJson(aemResult));
                    }
                });

    }

    @Test
    public void testOkHttpGet() throws IOException {
        OkHttpManager okHttpManager = OkHttpManager.getInstance();
        okHttpManager.setTest(true);
        String url = "https://api.douban.com/v2/book/search?";
      /*  @Query("q") String q,
        @Query("tag") String tag,
        @Query("start") String start,
        @Query("count") String count*/
        String keyword = "电商";
        String tag = "";
        String start = "1";
        String count = "10";
        String params = "q=" + keyword +
                "&tag=" + tag +
                "&start=" + start +
                "&count=" + count;
        okHttpManager.getDataSync(url + params, new OnHttpResultListener() {
            @Override
            public void onError(int code, String message) {
                System.out.print(message);
            }

            @Override
            public void onSuccess(String response) {
                System.out.print(response);
            }
        });
    }
}
