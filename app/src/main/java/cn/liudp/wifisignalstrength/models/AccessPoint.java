package cn.liudp.wifisignalstrength.models;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author dongpoliu on 2018-03-19.
 */

public class AccessPoint extends Application implements Parcelable {

    private String ssid;
    private int rssi;
    private String capabilities;
    private int frequency;

    public AccessPoint() {
    }

    public AccessPoint(JSONObject jsonObject) {
        try {
            if(jsonObject.has("result") && jsonObject.getJSONObject("result").has("ssid")) {
                this.setSsid(jsonObject.getJSONObject("result").getString("ssid"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            Log.d("InternetAir JSON", jsonObject.toString());
        }
    }

    public String getSsid() {
        return this.ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getRssi() {
        return this.rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getCapabilities() {
        return this.capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public int getFrequency() {
        return this.frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ssid);
        dest.writeInt(this.rssi);
        dest.writeInt(this.frequency);
        dest.writeString(this.capabilities);
    }

    protected AccessPoint(Parcel in) {
        this.ssid = in.readString();
        this.rssi = in.readInt();
        this.frequency = in.readInt();
        this.capabilities = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AccessPoint> CREATOR = new Creator<AccessPoint>() {
        @Override
        public AccessPoint createFromParcel(Parcel source) {
            return new AccessPoint(source);
        }

        @Override
        public AccessPoint[] newArray(int size) {
            return new AccessPoint[size];
        }
    };

}
