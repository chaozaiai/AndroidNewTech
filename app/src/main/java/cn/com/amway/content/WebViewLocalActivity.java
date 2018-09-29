package cn.com.amway.content;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.MiddlewareWebChromeBase;
import com.just.agentweb.MiddlewareWebClientBase;
import com.just.agentweb.PermissionInterceptor;
import com.just.agentweb.WebListenerManager;
import com.just.agentweb.download.DefaultDownloadImpl;
import com.techidea.library.utils.LogUtil;
import com.techidea.library.utils.StringUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.amway.content.constant.Constant;
import cn.com.amway.content.utils.BitmapEncodeUtils;
import cn.com.amway.content.utils.GifSizeFilter;
import cn.com.amway.content.utils.MiddlewareChromeClient;
import cn.com.amway.content.utils.MiddlewareWebViewClient;
import cn.com.amway.content.utils.UIController;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by sam on 2018/6/21.
 */

public class WebViewLocalActivity extends AppCompatActivity {

    private static final String TAG = WebViewLocalActivity.class.getCanonicalName();

    @BindView(R.id.webview_container)
    LinearLayout linearLayout;
//    @BindView(R.id.progressWB)
//    ProgressBarWebView progressBarWebView;


    private AgentWeb agentWeb;
    private BridgeWebView mBridgeWebView;
    private MiddlewareWebClientBase mMiddleWareWebClient;
    private MiddlewareWebChromeBase mMiddleWareWebChrome;

    private String detailUrl = "file:///android_asset/test.html";
    private AlertDialog alertDialog;
    private String[] paramsUrl = {};
    private String method = "";
    private String params = "";
    private IntentIntegrator intentIntegrator;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private AlertDialog alertDialogProgress;
    private String userInfo = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_webview);
        ButterKnife.bind(this);
        String intentUrl = getIntent().getStringExtra(Constant.PTEXTRA_URL);
        String intentUserinfo = getIntent().getStringExtra(Constant.PTEXTRA_USERINFO);
        if (!StringUtil.isEmpty(intentUrl)) {
            detailUrl = intentUrl;
        }
        if (!StringUtil.isEmpty(intentUserinfo)) {
            userInfo = intentUserinfo;
        }
        detailUrl += "?random=" + Math.round(Math.random() * 10);
        initView();
        LogUtil.info(TAG, "onCreate");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtil.info(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtil.info(TAG, "onRestoreInstanceState");
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void initView() {
        if (StringUtil.isEmpty(detailUrl)) {
            Toast.makeText(getApplicationContext(), "详情链接为空", Toast.LENGTH_LONG).show();
            return;
        }

        agentWeb = AgentWeb.with(this)//
                .setAgentWebParent(linearLayout, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件。
                .useDefaultIndicator(-1, 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                .setAgentWebWebSettings(getSettings())//设置 IAgentWebSettings。
                .setWebViewClient(mWebViewClient)//WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
                .setWebChromeClient(mWebChromeClient) //WebChromeClient
                .setPermissionInterceptor(mPermissionInterceptor) //权限拦截 2.0.0 加入。
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK) //严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
                .setAgentWebUIController(new UIController(this)) //自定义UI  AgentWeb3.0.0 加入。
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1) //参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新， AgentWeb 3.0.0 加入。
                .useMiddlewareWebChrome(getMiddlewareWebChrome()) //设置WebChromeClient中间件，支持多个WebChromeClient，AgentWeb 3.0.0 加入。
                .useMiddlewareWebClient(getMiddlewareWebClient()) //设置WebViewClient中间件，支持多个WebViewClient， AgentWeb 3.0.0 加入。
//                .setDownloadListener(mDownloadListener) 4.0.0 删除该API//下载回调
//                .openParallelDownload()// 4.0.0删除该API 打开并行下载 , 默认串行下载。 请通过AgentWebDownloader#Extra实现并行下载
//                .setNotifyIcon(R.drawable.ic_file_download_black_24dp) 4.0.0删除该api //下载通知图标。4.0.0后的版本请通过AgentWebDownloader#Extra修改icon
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
                .createAgentWeb()//创建AgentWeb。
                .ready()//设置 WebSettings。
                .go(detailUrl);
        agentWeb.getWebCreator().getWebView().setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        if (agentWeb != null) {
            //注入对象
            agentWeb.getJsInterfaceHolder().addJavaObject("nativeJsObj", new ContactJs());
        }

//        WebSettings webSettings = progressBarWebView.getSettings();
//        StringBuffer userAgent = new StringBuffer(webSettings.getUserAgentString());
//        Log.i(TAG, userAgent.toString());
//        userAgent.append(";AmwayContentApp");
//        webSettings.setUserAgentString(userAgent.toString());
//        userAgent = new StringBuffer(webSettings.getUserAgentString());
//        Log.i(TAG, userAgent.toString());
//        webSettings.setDefaultTextEncodingName("utf-8");
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setSupportMultipleWindows(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
//        progressBarWebView.addJavascriptInterface(new ContactJs(), "nativeJsObj");
//        progressBarWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                LogUtil.info(TAG, url);
//                String urlTemp = URLDecoder.decode(url);
//                if (url.contains("api://wx")
//                        || url.contains("api://ali")) {
//                    showDialog(urlTemp);
//                    return true;
//                } else {
//                    view.loadUrl(url);  //加载新的url
//                    return true;
//                }
//            }
//        });
//        progressBarWebView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK && progressBarWebView.canGoBack()) {
//                        progressBarWebView.goBack();   //后退
//                        return true;    //已处理
//                    }
//                }
//                return false;
//            }
//        });
//        progressBarWebView.loadUrl(detailUrl);
    }

    protected PermissionInterceptor mPermissionInterceptor = new PermissionInterceptor() {

        /**
         * PermissionInterceptor 能达到 url1 允许授权， url2 拒绝授权的效果。
         * @param url
         * @param permissions
         * @param permissions
         * @param action
         * @return true 该Url对应页面请求权限进行拦截 ，false 表示不拦截。
         */
        @Override
        public boolean intercept(String url, String[] permissions, String action) {
//            Log.i(TAG, "mUrl:" + url + "  permission:" + mGson.toJson(permissions) + " action:" + action);
            return false;
        }
    };
    /**
     * MiddlewareWebClientBase 是 AgentWeb 3.0.0 提供一个强大的功能，
     * 如果用户需要使用 AgentWeb 提供的功能， 不想重写 WebClientView方
     * 法覆盖AgentWeb提供的功能，那么 MiddlewareWebClientBase 是一个
     * 不错的选择 。
     *
     * @return
     */
    protected MiddlewareWebClientBase getMiddlewareWebClient() {
        return this.mMiddleWareWebClient = new MiddlewareWebViewClient() {
            /**
             *
             * @param view
             * @param url
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith("agentweb")) { // 拦截 url，不执行 DefaultWebClient#shouldOverrideUrlLoading
                    Log.i(TAG, "agentweb scheme ~");
                    return true;
                }

                if (super.shouldOverrideUrlLoading(view, url)) { // 执行 DefaultWebClient#shouldOverrideUrlLoading
                    return true;
                }
                // do you work
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        };
    }

    protected MiddlewareWebChromeBase getMiddlewareWebChrome() {
        return this.mMiddleWareWebChrome = new MiddlewareChromeClient() {
        };
    }

    /**
     * @return IAgentWebSettings
     */
    public IAgentWebSettings getSettings() {
        return new AbsAgentWebSettings() {
            private AgentWeb mAgentWeb;

            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {
                this.mAgentWeb = agentWeb;
            }

            /**
             * AgentWeb 4.0.0 内部删除了 DownloadListener 监听 ，以及相关API ，将 Download 部分完全抽离出来独立一个库，
             * 如果你需要使用 AgentWeb Download 部分 ， 请依赖上 compile 'com.just.agentweb:download:4.0.0 ，
             * 如果你需要监听下载结果，请自定义 AgentWebSetting ， New 出 DefaultDownloadImpl，传入DownloadListenerAdapter
             * 实现进度或者结果监听，例如下面这个例子，如果你不需要监听进度，或者下载结果，下面 setDownloader 的例子可以忽略。
             * @param webView
             * @param downloadListener
             * @return WebListenerManager
             */
//            @Override
//            public WebListenerManager setDownloader(WebView webView, android.webkit.DownloadListener downloadListener) {
//                return super.setDownloader(webView,
//                        DefaultDownloadImpl
//                                .create((Activity) webView.getContext(),
//                                        webView,
//                                        mDownloadListenerAdapter,
//                                        mDownloadListenerAdapter,
//                                        this.mAgentWeb.getPermissionInterceptor()));
//            }
        };
    }

    protected WebViewClient mWebViewClient = new WebViewClient() {

        private HashMap<String, Long> timer = new HashMap<>();

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        //
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {

            Log.i(TAG, "view:" + new Gson().toJson(view.getHitTestResult()));
            Log.i(TAG, "mWebViewClient shouldOverrideUrlLoading:" + url);
            //intent:// scheme的处理 如果返回false ， 则交给 DefaultWebClient 处理 ， 默认会打开该Activity  ， 如果Activity不存在则跳到应用市场上去.  true 表示拦截
            //例如优酷视频播放 ，intent://play?...package=com.youku.phone;end;
            //优酷想唤起自己应用播放该视频 ， 下面拦截地址返回 true  则会在应用内 H5 播放 ，禁止优酷唤起播放该视频， 如果返回 false ， DefaultWebClient  会根据intent 协议处理 该地址 ， 首先匹配该应用存不存在 ，如果存在 ， 唤起该应用播放 ， 如果不存在 ， 则跳到应用市场下载该应用 .
            if (url.startsWith("intent://") && url.contains("com.youku.phone")) {
                return true;
            }
			/*else if (isAlipay(view, mUrl))   //1.2.5开始不用调用该方法了 ，只要引入支付宝sdk即可 ， DefaultWebClient 默认会处理相应url调起支付宝
			    return true;*/


            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

//            Log.i(TAG, "mUrl:" + url + " onPageStarted  target:" + getUrl());
//            timer.put(url, System.currentTimeMillis());
//            if (url.equals(getUrl())) {
//                pageNavigator(View.GONE);
//            } else {
//                pageNavigator(View.VISIBLE);
//            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (timer.get(url) != null) {
                long overTime = System.currentTimeMillis();
                Long startTime = timer.get(url);
                Log.i(TAG, "  page mUrl:" + url + "  used time:" + (overTime - startTime));
            }

        }
		/*错误页回调该方法 ， 如果重写了该方法， 上面传入了布局将不会显示 ， 交由开发者实现，注意参数对齐。*/
	   /* public void onMainFrameError(AbsAgentWebUIController agentWebUIController, WebView view, int errorCode, String description, String failingUrl) {

            Log.i(TAG, "AgentWebFragment onMainFrameError");
            agentWebUIController.onMainFrameError(view,errorCode,description,failingUrl);

        }*/

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);

//			Log.i(TAG, "onReceivedHttpError:" + 3 + "  request:" + mGson.toJson(request) + "  errorResponse:" + mGson.toJson(errorResponse));
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

//			Log.i(TAG, "onReceivedError:" + errorCode + "  description:" + description + "  errorResponse:" + failingUrl);
        }
    };

    protected WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //  super.onProgressChanged(view, newProgress);
            Log.i(TAG, "onProgressChanged:" + newProgress + "  view:" + view);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    };

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        agentWeb.getWebLifeCycle().onResume();
        super.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        agentWeb.getWebLifeCycle().onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (agentWeb != null) {
            agentWeb.getWebLifeCycle().onDestroy();
            agentWeb = null;
        }
        // ...
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        // ...
        super.onBackPressed();
    }

    private void showDialog(String url) {
        paramsUrl = url.split("/");
        if (paramsUrl.length < 3) {
            Toast.makeText(getApplicationContext(), "参数有误", Toast.LENGTH_LONG).show();
            return;
        } else {
            method = paramsUrl[paramsUrl.length - 1];
            params = paramsUrl[paramsUrl.length - 2];
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("支付参数")
                    .setMessage(params)
                    .setCancelable(false)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            webviewJsCallBack(method, "支付成功");
                            if (null != alertDialog
                                    && alertDialog.isShowing()) {
                                alertDialog.dismiss();
                            }
                        }
                    });
            alertDialog = builder.create();
            alertDialog.show();
        }

    }

    private void createWebPrintJob(WebView webView) {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

        // Create a print job with name and adapter instance
        String jobName = getString(R.string.app_name) + " Document";
        PrintJob printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());

        // Save the job object for later status checking
//        mPrintJobs.add(printJob);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_CODE_SELECT_PHOTO && resultCode == RESULT_OK) {
            List<Uri> uriList = Matisse.obtainResult(data);
            List<String> pathList = Matisse.obtainPathResult(data);
            if (null != pathList && pathList.size() > 0) {
                String filePath = pathList.get(0);
                File orginFile = new File(filePath);
                Log.i(TAG, "origin size: " + orginFile.length() / 1024);
                Luban.with(this)
                        .load(filePath)
                        .ignoreBy(100)
                        .setTargetDir(getPath())
                        .filter(new CompressionPredicate() {
                            @Override
                            public boolean apply(String path) {
                                return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                            }
                        })
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                                // TODO 压缩开始前调用，可以在方法内启动 loading UI
                                showProgress();
                            }

                            @Override
                            public void onSuccess(File file) {
                                Log.i(TAG, "target size: " + file.length() / 1024);
                                // TODO 压缩成功后调用，返回压缩后的图片文件
                                String tempPath = file.getAbsolutePath();

                                String base64 = BitmapEncodeUtils.encodeBase64(tempPath);
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("image", base64);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                webviewJsCallBack(method, tempPath);
//                                saveBitmap(base64);
                                hideProgress();
                            }

                            @Override
                            public void onError(Throwable e) {
                                // TODO 当压缩过程出现问题时调用
                                hideProgress();
                                Toast.makeText(getApplicationContext(), "压缩图片出错，请稍候重试", Toast.LENGTH_LONG).show();
                            }
                        }).launch();

            } else {
                Toast.makeText(getApplicationContext(), "选择图片有误，请稍候重试", Toast.LENGTH_LONG).show();
            }
        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (null != result) {
                if (result.getContents() == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
//                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    webviewJsCallBack(method, result.getContents());
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

    }

    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/Luban/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }


    /*private void saveBitmap(String base64) {
        Log.i(TAG, DateUtil.getDateFormat(System.currentTimeMillis(), DateUtil.MESSAGE_FORMAT));
        byte[] decode = Base64.decode(base64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        //save to image on sdcard
        try {
            String path = Environment.getExternalStorageDirectory().getPath()
                    + "/decodeImage.jpg";
            Log.d("linc", "path is " + path);
            OutputStream stream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            stream.close();
            Log.e("linc", "jpg okay!");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("linc", "failed: " + e.getMessage());
        }
        Log.i(TAG, DateUtil.getDateFormat(System.currentTimeMillis(), DateUtil.MESSAGE_FORMAT));

    }*/


    private void webviewJsCallBack(String actionMethod, String params) {
//        progressBarWebView.loadUrl("javascript:" + actionMethod + "('" + params + "')");
        agentWeb.getJsAccessEntrace().quickCallJs(actionMethod, params);
        Log.i(TAG, "base 64 webviewJsCallBack: " + params.length());
    }

/*    private void webviewJsCallBack(String actionMethod, String params) {
//        progressBarWebView.loadUrl("javascript:" + actionMethod + "('" + params + "')");
        agentWeb.getJsAccessEntrace().quickCallJs(actionMethod,params);
        Log.i(TAG, "base 64 webviewJsCallBack JSONObject: " + params.toString());
    }*/

    private String ADA = "123";

    class ContactJs extends Object {

        /**
         * H5 调用初始化用户信息接口
         *
         * @return 以json格式返回用户信息
         */
        @JavascriptInterface
        public String callInitUser() {
            Log.i("H5 params", "no params");
            return userInfo;
        }

        /**
         * H5 扫码按钮调用接口
         *
         * @param callBack native 扫码完成后回调H5的js方法名称。
         */
        @JavascriptInterface
        public void callScan(String callBack) {
            method = callBack;
            Log.i("H5 callScan", callBack);
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (null == intentIntegrator) {
                        intentIntegrator = new IntentIntegrator(WebViewLocalActivity.this);
                        intentIntegrator.setOrientationLocked(false);
                        intentIntegrator.initiateScan();
                    } else {
                        intentIntegrator.initiateScan();
                    }
                }
            });
        }

        /**
         * H5 选择图片调用接口
         *
         * @param callBack native 选择图片后完成后回调H5的js方法名称。
         *                 回调js方法参数为图片Base64字符串
         */
        @JavascriptInterface
        public void callSelectPhoto(String callBack) {
            method = callBack;
            Log.i("H5 callSelectPhoto", callBack);
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    selectPhoto();
                }
            });
        }
    }

    private void selectPhoto() {
        Matisse.from(WebViewLocalActivity.this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(1)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.5f)
                .imageEngine(new GlideEngine())
                .forResult(Constant.REQUEST_CODE_SELECT_PHOTO);
    }

    private void showProgress() {
        if (alertDialogProgress == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialog = View.inflate(this, R.layout.progress_dialog, null);
            builder.setView(dialog);
            alertDialogProgress = builder.create();
            alertDialogProgress.show();
        }
    }

    private void hideProgress() {
        if (null != alertDialogProgress
                && alertDialogProgress.isShowing()) {
            alertDialogProgress.dismiss();
            alertDialogProgress = null;
        }
    }
}
