package cn.com.amway.content;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.techidea.library.widget.ProgressBarWebView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sam on 2018/6/21.
 */

public class ProductActivity extends AppCompatActivity {

    @BindView(R.id.pbweb)
    ProgressBarWebView progressBarWebView;
    private String detailUrl="https://catalog.amwaynet.com.cn/h5/index.html?from=singlemessage&isappinstalled=0";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){

        WebSettings webSettings = progressBarWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        progressBarWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);  //加载新的url
                return true;
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
}
