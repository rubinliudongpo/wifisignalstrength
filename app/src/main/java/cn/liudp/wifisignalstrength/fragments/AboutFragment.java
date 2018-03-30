package cn.liudp.wifisignalstrength.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import butterknife.BindView;
import cn.liudp.wifisignalstrength.R;
import cn.liudp.wifisignalstrength.base.BaseFragment;

/**
 * @author dongpoliu on 2018-03-21.
 */

public class AboutFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener  {

    public final static String INTENT_STRING_TAB_NAME = AboutFragment.class.getSimpleName();
    public final static String INTENT_INT_ABOUT = "AboutIntent";
//    @BindView(R.id.recycler_view)
//    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;
    private Context mContext;

    public static AboutFragment newInstance(String text){
        AboutFragment aboutFragment = new AboutFragment();
        Bundle bundle=new Bundle();
        bundle.putString("text",text);
        aboutFragment.setArguments(bundle);
        return aboutFragment;
    }

    @Override
    protected void initEventAndData(View mView) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    protected void fillData() {
    }

    protected void init() {
        fillData();
//        final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
//        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        });
    }
}
