package cn.liudp.wifisignalstrength.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import butterknife.BindView;
import cn.liudp.wifisignalstrength.R;
import cn.liudp.wifisignalstrength.base.BaseFragment;
import cn.liudp.wifisignalstrength.modules.web.WebContract;
import cn.liudp.wifisignalstrength.modules.web.WebPresenter;

/**
 * @author dongpoliu on 2018-03-21.
 */

public class AboutFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, WebContract.IWebView {

    public final static String TAB_NAME = AboutFragment.class.getSimpleName();
    public final static String GIT_URL = "https://github.com/rubinliudongpo";

//    @BindView(R.id.swipe_refresh)
//    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.title_bar)
    CommonTitleBar titleBar;
    @BindView(R.id.web_appbar)
    AppBarLayout mWebAppbar;
    @BindView(R.id.web_progress_bar)
    ProgressBar mWebProgressBar;
    @BindView(R.id.web_view)
    WebView mWebView;
    private WebContract.IWebPresenter mWebPresenter;

    public static AboutFragment newInstance(String text){
        AboutFragment aboutFragment = new AboutFragment();
        Bundle bundle=new Bundle();
        bundle.putString("text",text);
        aboutFragment.setArguments(bundle);
        return aboutFragment;
    }

    @Override
    protected void initEventAndData(View mView) {
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_BUTTON || action == CommonTitleBar.ACTION_LEFT_TEXT) {
//                    onBackPressed();
                }
            }
        });
        mWebPresenter = new WebPresenter(this);
        mWebPresenter.subscribe();
        initWebView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        mWebView.setWebChromeClient(new MyWebChrome());
        mWebView.setWebViewClient(new MyWebClient());
    }

    @Override
    public AboutFragment getWebViewContext() {
        return this;
    }

    @Override
    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    @Override
    public void onRefresh() {
//        mRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                mRefreshLayout.setRefreshing(false);
//            }
//        });
    }

    private class MyWebChrome extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mWebProgressBar.setVisibility(View.VISIBLE);
            mWebProgressBar.setProgress(newProgress);
        }
    }

    private class MyWebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            mWebProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mWebPresenter.unSubscribe();
    }
}
