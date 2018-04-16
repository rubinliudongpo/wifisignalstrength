package cn.liudp.wifisignalstrength.models;

import android.support.annotation.NonNull;
import cn.liudp.wifisignalstrength.R;
import cn.liudp.wifisignalstrength.utils.WiFiUtils;

/**
 * @author dongpoliu on 2018-03-19.
 */

public enum SignalStrength {
    ZERO(R.drawable.ic_signal_wifi_0_bar_black_36dp, R.color.error_color),
    ONE(R.drawable.ic_signal_wifi_1_bar_black_36dp, R.color.warning_color),
    TWO(R.drawable.ic_signal_wifi_2_bar_black_36dp, R.color.warning_color),
    THREE(R.drawable.ic_signal_wifi_3_bar_black_36dp, R.color.success_color),
    FOUR(R.drawable.ic_signal_wifi_4_bar_black_36dp, R.color.success_color);

    private final int imageResource;
    private final int colorResource;

    SignalStrength(int imageResource, int colorResource) {
        this.imageResource = imageResource;
        this.colorResource = colorResource;
    }

    public int colorResource() {
        return colorResource;
    }

    public int colorResourceDefault() {
        return R.color.icons_color;
    }

    public int imageResource() {
        return imageResource;
    }

    public boolean weak() {
        return ZERO.equals(this);
    }
}
