package com.library.data.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sam on 2018/6/21.
 */

public class ArticleEntity implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("metatag.keywords")
    private List<String> keywords;

    @SerializedName("host")
    private String host;

    @SerializedName("metatag.thumbnail")
    private String thumbnail;

    @SerializedName("segment")
    private String segment;

    @SerializedName("metatag.publishdate")
    private String publishdate;

    @SerializedName("strippedContent")
    private String strippedContent;

    @SerializedName("digest")
    private String digest;

    @SerializedName("tstamp")
    private String tstamp;

    @SerializedName("url")
    private String url;

    @SerializedName("title")
    private String title;

    @SerializedName("boost")
    private String boost;

    @SerializedName("modifiedDate")
    private String modifiedDate;

    @SerializedName("score")
    private String score;

    @SerializedName("promotion")
    private Integer promotion;

    @SerializedName("flow")
    private Integer flow;

    @SerializedName("aboReadOnly")
    private int aboReadOnly;

    @SerializedName("videoPath")
    private List<String> videoPath;

    @SerializedName("videoLen")
    private int videoLen;

    @SerializedName("description")
    private String description;


    @SerializedName("metatag.searchtitle")
    private String searchtitle;

    @SerializedName("contentTag")
    private String contentTag;

    public String getContentTag() {
        return contentTag;
    }

    public void setContentTag(String contentTag) {
        this.contentTag = contentTag;
    }

    public int getAboReadOnly() {
        return aboReadOnly;
    }

    public void setAboReadOnly(int aboReadOnly) {
        this.aboReadOnly = aboReadOnly;
    }

    public List<String> getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(List<String> videoPath) {
        this.videoPath = videoPath;
    }

    public int getVideoLen() {
        return videoLen;
    }

    public void setVideoLen(int videoLen) {
        this.videoLen = videoLen;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSearchtitle() {
        return searchtitle;
    }

    public void setSearchtitle(String searchtitle) {
        this.searchtitle = searchtitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getPublishdate() {
        return publishdate;
    }

    public void setPublishdate(String publishdate) {
        this.publishdate = publishdate;
    }

    public String getStrippedContent() {
        return strippedContent;
    }

    public void setStrippedContent(String strippedContent) {
        this.strippedContent = strippedContent;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getTstamp() {
        return tstamp;
    }

    public void setTstamp(String tstamp) {
        this.tstamp = tstamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBoost() {
        return boost;
    }

    public void setBoost(String boost) {
        this.boost = boost;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Integer getPromotion() {
        return promotion;
    }

    public void setPromotion(Integer promotion) {
        this.promotion = promotion;
    }

    public Integer getFlow() {
        return flow;
    }

    public void setFlow(Integer flow) {
        this.flow = flow;
    }
}
