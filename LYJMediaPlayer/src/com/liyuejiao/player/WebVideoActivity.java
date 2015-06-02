package com.liyuejiao.player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.liyuejiao.player.util.Constant;
import com.liyuejiao.player.util.FileUtils;
import com.liyuejiao.player.util.SystemUtils;

public class WebVideoActivity extends FragmentActivity {
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUtils.loadScreenInfo(this);
        getActionBar().hide();
        setContentView(R.layout.fragment_online_video);

        String uri = getIntent().getExtras().getString("path");
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.getSettings().setJavaScriptEnabled(true);
        // mWebView.getSettings().setPluginsEnabled(true);
        mWebView.loadUrl(uri);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
            };

            /** 页面跳转 */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (FileUtils.isVideoOrAudio(url)) {
                    Intent intent = new Intent(WebVideoActivity.this, PlayerActivity.class);
                    intent.putExtra(Constant.KEY_PATH, url);
                    startActivity(intent);
                    return true;
                }
                return false;
            };
        });

        mWebView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView != null && mWebView.canGoBack()) {
                    mWebView.goBack();
                    return true;
                }
                return false;
            }
        });
    }

}
