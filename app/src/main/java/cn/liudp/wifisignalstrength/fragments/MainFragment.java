package cn.liudp.wifisignalstrength.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.liudp.wifisignalstrength.MyApplication;
import cn.liudp.wifisignalstrength.R;
import cn.liudp.wifisignalstrength.adapters.AccessPointAdapter;
import cn.liudp.wifisignalstrength.base.BaseFragment;
import cn.liudp.wifisignalstrength.models.AccessPoint;
import cn.liudp.wifisignalstrength.modules.wifi.RxWiFi;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author dongpoliu on 2018-03-17.
 */

public class MainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public final static String TAG = MainFragment.class.getSimpleName();
    public final static String INTENT_INT_INDEX = "IndexIntent";
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;
//    @BindView(R.id.message)
//    TextView mMessage;
//    private Context mContext;
    private ArrayList<AccessPoint> accessPointsList;
    private AccessPointAdapter accessPointAdapter;
    private Disposable rxWiFiScanResultSubscription;
    private Disposable rxWiFiInfoSubscription;
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_WIFI_STATE
    };

    public static MainFragment newInstance(String text){
        MainFragment mainFragment = new MainFragment();
        Bundle bundle=new Bundle();
        bundle.putString("text",text);
        mainFragment.setArguments(bundle);
        return mainFragment;
    }

    @Override
    protected void initEventAndData(View mView) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission_group.LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        }
        accessPointAdapter = new AccessPointAdapter();
        accessPointsList = new ArrayList<>();
        mRecyclerView.setAdapter(accessPointAdapter);
        rxWiFiScanResultSubscription = RxWiFi.observeWiFiAccessPoints(getContext())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<ScanResult>>() {
                @Override
                public void accept(List<ScanResult> scanResults) throws Exception {
                    AccessPoint accessPoint = new AccessPoint();
                    for (ScanResult scanResult:scanResults) {
                        accessPoint.setSsid(scanResult.SSID);
                        accessPoint.setRssi(scanResult.level);
                        accessPoint.setFrequency(scanResult.frequency);
                        accessPoint.setCapabilities(scanResult.capabilities);
                        System.out.println(TAG + " " + accessPoint.getSsid());
                        accessPointsList.add(accessPoint);
                    }
                    accessPointAdapter.setList(accessPointsList);
                    displayAccessPoints(accessPointAdapter);
                }
         });
    }

    private void displayAccessPoints(AccessPointAdapter accessPointAdapter) {
        final LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), MyApplication.getGridSpanCount(getActivity()));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(accessPointAdapter);
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
//            }
//        });
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