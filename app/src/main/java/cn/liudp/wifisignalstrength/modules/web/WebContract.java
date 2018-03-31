package cn.liudp.wifisignalstrength.modules.web;

import android.app.Activity;
import android.app.Fragment;

import cn.liudp.wifisignalstrength.base.BasePresenter;
import cn.liudp.wifisignalstrength.base.BaseView;
import cn.liudp.wifisignalstrength.fragments.AboutFragment;

/**
 * @author dongpoliu on 2018-03-31.
 */

public interface WebContract {

    interface IWebView extends BaseView {
        AboutFragment getWebViewContext();
//        void setTitle(String title);
        void loadUrl(String url);
        void initWebView();
    }

    interface IWebPresenter extends BasePresenter {
        String getUrl();
    }
}
