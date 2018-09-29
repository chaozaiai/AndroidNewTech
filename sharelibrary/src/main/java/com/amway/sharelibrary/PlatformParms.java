package com.amway.sharelibrary;

/**
 * Created by sam on 2018/7/27.
 */

public class PlatformParms {
     private String id;
     private String sortId;
     private String appId;
     private String appSecret;
     private String bypassApproval="true";
     private String enable="true";

    public PlatformParms() {
    }

    public PlatformParms(String id, String sortId, String appId, String appSecret, String bypassApproval, String enable) {
        this.id = id;
        this.sortId = sortId;
        this.appId = appId;
        this.appSecret = appSecret;
        this.bypassApproval = bypassApproval;
        this.enable = enable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getBypassApproval() {
        return bypassApproval;
    }

    public void setBypassApproval(String bypassApproval) {
        this.bypassApproval = bypassApproval;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }
}
