package cn.liudp.wifisignalstrength.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import cn.liudp.wifisignalstrength.R;
import cn.liudp.wifisignalstrength.base.BaseFragment;

/**
 * @author dongpoliu on 2018-03-17.
 */

public class MainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public final static String INTENT_STRING_TAB_NAME = MainFragment.class.getSimpleName();
    public final static String INTENT_INT_INDEX = "IndexIntent";
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;
//    @BindView(R.id.message)
//    TextView mMessage;
//    private Context mContext;
//    private WifiProcessInterface wifiProcessInterface = WifiProcess.getInstance();
//    private AccessPointAdapter accessPointAdapter;

    public static MainFragment newInstance(String text){
        MainFragment mainFragment = new MainFragment();
        Bundle bundle=new Bundle();
        bundle.putString("text",text);
        mainFragment.setArguments(bundle);
        return mainFragment;
    }

    @Override
    protected void initEventAndData(View mView) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onPause() {
        super.onPause();
//        wifiProcessInterface.stopScan();
    }

    @Override
    public void onResume() {
        super.onResume();
        startWifiAccessPointsSubscription();
    }

   private void startWifiAccessPointsSubscription() {

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

    public void showMessage(String message) {
//        mMessage.setText(message);
//        mMessage.setVisibility(View.VISIBLE);
    }

    public void hideMessage() {
//        mMessage.setVisibility(View.GONE);
    }
}