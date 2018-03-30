package cn.liudp.wifisignalstrength;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import cn.liudp.wifisignalstrength.activities.GuideActivity;
import cn.liudp.wifisignalstrength.base.BaseActivity;

/**
 * @author dongpoliu on 2018-03-15.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected int getLayout() {
        return 0;
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        if (MyApplication.enableGuide) {
            String GUIDE_KEY = "has_guide";
            System.out.println("WiFiSignalStrength SplashActivity initEventAndData");
            System.out.println("WiFiSignalStrength SplashActivity GUIDE_KEY is " + SPUtils.getInstance().getBoolean(GUIDE_KEY,false));
            if (SPUtils.getInstance().getBoolean(GUIDE_KEY, true)) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromSplash", true);
                SPUtils.getInstance().put(GUIDE_KEY, true);
                ActivityUtils.startActivity(bundle, GuideActivity.class);
                ActivityCompat.finishAfterTransition(this);
                return;
            }
        }
        ActivityUtils.startActivity(MainActivity.class);
        ActivityCompat.finishAfterTransition(this);
    }
}