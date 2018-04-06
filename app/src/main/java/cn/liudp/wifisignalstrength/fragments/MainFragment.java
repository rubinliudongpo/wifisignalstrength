package cn.liudp.wifisignalstrength.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import cn.liudp.wifisignalstrength.R;
import cn.liudp.wifisignalstrength.adapters.AccessPointAdapter;
import cn.liudp.wifisignalstrength.base.BaseFragment;
import cn.liudp.wifisignalstrength.models.AccessPoint;
import cn.liudp.wifisignalstrength.modules.wifi.RxWiFi;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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
    @BindView(R.id.title_bar)
    CommonTitleBar titleBar;
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
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_BUTTON || action == CommonTitleBar.ACTION_LEFT_TEXT) {
//                    onBackPressed();
                }
            }
        });
        accessPointsList = new ArrayList<>();
        accessPointAdapter = new AccessPointAdapter();
        accessPointAdapter.setList(accessPointsList);
        final LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(accessPointAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
//                    visibleItemCount = mLayoutManager.getChildCount();
//                    totalItemCount = mLayoutManager.getItemCount();
//                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
//                    if (!loadingMore) {
//                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && (viewMore) && !(mItemsContainer.isRefreshing())) {
//                            loadingMore = true;

//                        }
//                    }
                }
            }
        });
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                AccessPoint accessPoint = (AccessPoint) accessPointsList.get(position);
//                Intent intent = new Intent(getActivity(), AccessPointActivity.class);
//                intent.putExtra("accessPointId", accessPoint.getSsid());
//                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));
        mRefreshLayout.setRefreshing(false);
    }

    private void displayAccessPoints(List<ScanResult> scanResults) {
        accessPointsList.clear();
        for (ScanResult scanResult : scanResults) {
            AccessPoint accessPoint = new AccessPoint();
            accessPoint.setSsid(scanResult.SSID);
            accessPoint.setRssi(scanResult.level);
            accessPoint.setFrequency(scanResult.frequency);
            accessPoint.setCapabilities(scanResult.capabilities);
            System.out.println(TAG + " " + accessPoint.getSsid());
            accessPointsList.add(accessPoint);
        }
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
    public void onResume () {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission_group.LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        }
        mRefreshLayout.setOnRefreshListener(this);
        accessPointsList = new ArrayList<>();
        rxWiFiScanResultSubscription = RxWiFi.observeWiFiAccessPoints(getContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayAccessPoints);
        startWifiAccessPointsSubscription();
    }

   private void startWifiAccessPointsSubscription() {

   }

    @Override
    public void onRefresh() {
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
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

    static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        public interface OnItemClickListener {
            void onItemClick(View view, int position);
            void onItemLongClick(View view, int position);
        }
        private OnItemClickListener mListener;
        private GestureDetector mGestureDetector;
        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, RecyclerItemClickListener.OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                   return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null && mListener != null) {
                        mListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}