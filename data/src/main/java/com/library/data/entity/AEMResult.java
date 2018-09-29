package com.library.data.entity;

/**
 * Created by sam on 2018/6/21.
 */

public class AEMResult {

    private ReponseHeader responseHeader;
    private QueryResponse<ArticleEntity> response;

    public ReponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ReponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public QueryResponse<ArticleEntity> getResponse() {
        return response;
    }

    public void setResponse(QueryResponse<ArticleEntity> response) {
        this.response = response;
    }
}
