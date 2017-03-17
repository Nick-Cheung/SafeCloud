package com.nick.safecloud.activity;

import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nick.safecloud.R;
import com.nick.safecloud.api.ApiScheduler;
import com.nick.safecloud.api.BaiduApi;
import com.nick.safecloud.base.BaseActivity;
import com.nick.safecloud.model.CloudInfoModel;
import com.nick.safecloud.util.CookieUtil;
import com.trello.rxlifecycle.ActivityEvent;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by Sparrow on 2017/3/17.
 */

public class WebLoginActivity extends BaseActivity {

    public static final String URL_BAIDU_LOGIN = "https://wappass.baidu.com/passport?login&authsite=1&tpl=netdisk&overseas=1&regdomestic=1&smsLoginLink=1&display=mobile&u=http%3A%2F%2Fpan.baidu.com%2F";



    @Bind(R.id.webview_login)
    WebView webView;

    WebSettings webSettings;


    public static void startMe(Context context) {
        Intent intent = new Intent(context, WebLoginActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_login);
        ButterKnife.bind(this);
        initWebView();
    }


    private void initWebView() {
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);

        webView.setWebViewClient(new LoginWebViewClient());
        webView.loadUrl(URL_BAIDU_LOGIN);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    class LoginWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            CookieManager cookieManager = CookieManager.getInstance();

            String cookie = cookieManager.getCookie(url);
            CookieUtil.putCookie(cookie);

            BaiduApi.ApiBuilder.build().getQuote()
                    .map(new Func1<String, Boolean>() {
                        @Override
                        public Boolean call(String s) {
                            Gson gson = new Gson();
                            CloudInfoModel model = gson.fromJson(s, CloudInfoModel.class);
                            return model.getErrno() == CloudInfoModel.SUCCESS_CODE;
                        }
                    }).compose(WebLoginActivity.this.<Boolean>bindUntilEvent(ActivityEvent.DESTROY))
                    .compose(new ApiScheduler<Boolean>())
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            if(aBoolean == true) {
                                FileListActivity.startMe(WebLoginActivity.this, "/");
                            }
                        }
                    });
        }
    }
}
