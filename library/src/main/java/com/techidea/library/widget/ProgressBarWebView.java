package com.techidea.library.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.techidea.library.R;

/**
 * Created by sam on 2018/1/15.
 */

public class ProgressBarWebView extends WebView {

    private ProgressBar progressBar;

    public ProgressBarWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3, 0, 0));
        Drawable drawable = getResources().getDrawable(
                R.drawable.library_horizontal_progressbar_drawable
        );
        progressBar.setProgressDrawable(drawable);
        addView(progressBar);
        setWebChromeClient(new WebChromeClient());
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressBar.setVisibility(GONE);
            } else {
                if (progressBar.getVisibility() == GONE) {
                    progressBar.setVisibility(VISIBLE);
                }
                progressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> valueCallback) {
            openFileChooser(valueCallback, null);
        }

        // For Android  >= 3.0
        public void openFileChooser(ValueCallback valueCallback, String acceptType) {
            openFileChooser(valueCallback, acceptType, null);
        }

        //For Android  >= 4.1
        public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {

        }


        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }


    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressBar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
