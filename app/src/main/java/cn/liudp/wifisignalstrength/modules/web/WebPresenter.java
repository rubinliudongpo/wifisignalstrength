package cn.liudp.wifisignalstrength.modules.web;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

import cn.liudp.wifisignalstrength.fragments.AboutFragment;

/**
 * @author dongpoliu on 2018-03-31.
 */

public class WebPresenter implements WebContract.IWebPresenter {

    private WebContract.IWebView mWebView;
    private String mUrl;
    private AboutFragment mFragment;

    public WebPresenter(WebContract.IWebView webView){
        this.mWebView = webView;
    }

    @Override
    public void unSubscribe () {
    }

    @Override
    public void subscribe() {
        mFragment = mWebView.getWebViewContext();
        Intent intent = mFragment.getActivity().getIntent();
//        mWebView.setTitle(intent.getStringExtra(AboutFragment.GIT_TITLE));
        mWebView.initWebView();
        mUrl = AboutFragment.GIT_URL;
        mWebView.loadUrl(mUrl);
    }

    @Override
    public String getUrl() {
        return this.mUrl;
    }
}