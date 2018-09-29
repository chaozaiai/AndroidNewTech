package cn.com.amway.content;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.techidea.library.utils.LogUtil;
import com.techidea.library.utils.StringUtil;
import com.techidea.library.widget.ProgressBarWebView;

import java.net.URLDecoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.amway.content.constant.Constant;

/**
 * Created by sam on 2018/6/21.
 */

public class WebViewActivity extends AppCompatActivity {

    private static final String TAG = WebViewActivity.class.getCanonicalName();

    @BindView(R.id.pbweb)
    ProgressBarWebView progressBarWebView;
    private String detailUrl = "http://app3titest.metro.com.cn:8080/forYinShang.html";
    private AlertDialog alertDialog;
    private String[] paramsUrl = {};
    private String method = "";
    private String params = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        ButterKnife.bind(this);
        detailUrl += "?random=" + Math.round(Math.random() * 10);
//        detailUrl = getIntent().getStringExtra(Constant.PTEXTRA_URL);
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

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.info(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.info(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.info(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.info(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.info(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.info(TAG, "onDestroy");
    }

    private void initView() {
        if (StringUtil.isEmpty(detailUrl)) {
            Toast.makeText(getApplicationContext(), "详情链接为空", Toast.LENGTH_LONG).show();
            return;
        }
        WebSettings webSettings = progressBarWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        progressBarWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.info(TAG, url);
                String urlTemp = URLDecoder.decode(url);
                if (url.contains("api://wx")
                        || url.contains("api://ali")) {
                    showDialog(urlTemp);
                    return true;
                } else {
                    view.loadUrl(url);  //加载新的url
                    return true;
                }
            }
        });
        progressBarWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && progressBarWebView.canGoBack()) {
                        progressBarWebView.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
        progressBarWebView.loadUrl(detailUrl);
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

    private void webviewJsCallBack(String actionMethod, String params) {
        progressBarWebView.loadUrl("javascript:" + actionMethod + "('" + params + "')");
    }
}
