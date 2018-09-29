package com.techidea.library.utils;

import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;

import java.io.File;

/**
 * Created by sam on 2018/1/15.
 */

public class WebViewUtils {

    public static final String WEB_CACHE_PATH = "/webcache";

    public static String getWebCacheDir(Context context) {
        return context.getFilesDir().getAbsolutePath() + WEB_CACHE_PATH;
    }

    //app退出，清除webview緩存
    public static void clearWebViewCahce(Context context) {
        try {
            context.deleteDatabase("webview.db");
            context.deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //WebView 缓存文件
        CookieSyncManager.createInstance(context.getApplicationContext());
        //清空所有Cookie
        //Create a singleton CookieSyncManager within a context
        CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
        cookieManager.removeAllCookie();// Removes all cookies.
        CookieSyncManager.getInstance().sync(); // forces sync manager to sync now
        File appCacheDir = new File(context.getFilesDir().getAbsolutePath() + WEB_CACHE_PATH);
        File webviewCacheDir = new File(context.getCacheDir().getAbsolutePath() + WEB_CACHE_PATH);
        //删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }


    public static void setLocationCache(WebSettings webSettings, Context context) {
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);

        //最重要的方法，一定要设置，这就是出不来的主要原因
        //设置dom缓存
        webSettings.setDomStorageEnabled(true);
        //启用地理定位
        webSettings.setGeolocationEnabled(true);


        //添加缓存会导致部分网页页面图片不显示
        String cacheDirPath = WebViewUtils.getWebCacheDir(context.getApplicationContext());
        //设置数据库缓存路径
        webSettings.setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        webSettings.setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);
        //5.0 以上版本webview允许https链接里面放http的图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    public static void setJsEnable(WebSettings webSettings, Context context) {
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        //5.0 以上版本webview允许https链接里面放http的图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    private static void deleteFile(File file) {
        try {
            if (file.exists()) {
                if (file.isFile()) {
                    file.delete();
                } else if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFile(files[i]);
                    }
                }
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testGeolocationOK(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsProviderOK = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkProviderOK = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean geolocationOK = gpsProviderOK && networkProviderOK;
        Log.i("LOCATION", "gpsProviderOK = " + gpsProviderOK + "; networkProviderOK = " + networkProviderOK + "; geoLocationOK=" + geolocationOK);
    }
}
