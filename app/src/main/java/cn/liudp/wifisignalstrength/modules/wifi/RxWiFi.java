package cn.liudp.wifisignalstrength.modules.wifi;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.util.Log;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CHANGE_WIFI_STATE;

/**
 * @author dongpoliu on 2018-04-01.
 */

public class RxWiFi {

    private final static String LOG_TAG = "RxWifi";

    public RxWiFi() {
    }

    /**
     * Observes WiFi Access Points.
     * Returns fresh list of Access Points
     * whenever WiFi signal strength changes.
     *
     * @param context Context of the activity or an application
     * @return RxJava Observable with list of WiFi scan results
     */
    @SuppressLint("MissingPermission")
    @RequiresPermission(allOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, CHANGE_WIFI_STATE, ACCESS_WIFI_STATE})
    public static Observable<List<ScanResult>> observeWiFiAccessPoints(final Context context) {
        @SuppressLint("WifiManagerPotentialLeak") final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            wifiManager.startScan(); // without starting scan, we may never receive any scan results
        } else {
            Log.w(LOG_TAG, "WifiManager was null, so WiFi scan was not started");
        }
        final IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        return Observable.create(new ObservableOnSubscribe<List<ScanResult>>() {
            @Override public void subscribe(final ObservableEmitter<List<ScanResult>> emitter) throws Exception {
                final BroadcastReceiver receiver = createWifiScanResultsReceiver(emitter, wifiManager);
                if (wifiManager != null) {
                    context.registerReceiver(receiver, filter);
                } else {
                    emitter.onError(new RuntimeException("WifiManager was null, so BroadcastReceiver for Wifi scan results " + "cannot be registered"));
                }

                Disposable disposable = disposeInUiThread(new Action() {
                    @Override public void run() {
                        tryToUnregisterReceiver(context, receiver);
                    }
                });
                emitter.setDisposable(disposable);
            }
        });
    }

    @NonNull
    protected static BroadcastReceiver createWifiScanResultsReceiver(final ObservableEmitter<List<ScanResult>> emitter, final WifiManager wifiManager) {
        return new BroadcastReceiver() {
            @Override public void onReceive(Context context1, Intent intent) {
                wifiManager.startScan(); // we need to start scan again to get fresh results ASAP
                emitter.onNext(wifiManager.getScanResults());
            }
        };
    }

    private static Disposable disposeInUiThread(final Action action) {
        return Disposables.fromAction(new Action() {
            @Override public void run() throws Exception {
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    action.run();
                } else {
                    final Scheduler.Worker inner = AndroidSchedulers.mainThread().createWorker();
                    inner.schedule(new Runnable() {
                        @Override public void run() {
                            try {
                                action.run();
                            } catch (Exception e) {
                                onError("Could not unregister receiver in UI Thread", e);
                            }
                            inner.dispose();
                        }
                    });
                }
            }
        });
    }

    protected static void tryToUnregisterReceiver(final Context context, final BroadcastReceiver receiver) {
        try {
            context.unregisterReceiver(receiver);
        } catch (Exception exception) {
            onError("receiver was already unregistered", exception);
        }
    }

    protected static void onError(final String message, final Exception exception) {
        Log.e(LOG_TAG, message, exception);
    }
}
