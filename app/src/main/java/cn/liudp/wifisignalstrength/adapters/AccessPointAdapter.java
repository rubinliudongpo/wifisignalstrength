package cn.liudp.wifisignalstrength.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import butterknife.BindView;
import cn.liudp.wifisignalstrength.R;
import cn.liudp.wifisignalstrength.models.AccessPoint;

/**
 * @author dongpoliu on 2018-03-19.
 */

public class AccessPointAdapter extends  RecyclerView.Adapter<AccessPointAdapter.MyViewHolder> {

    private Context mContext;
    private List<AccessPoint> mAccessPointList;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_view)
        CardView mCardView;
        @BindView(R.id.item_ssid)
        TextView mItemSsid;
        @BindView(R.id.item_signal_level)
        TextView mItemSignalLevel;
        @BindView(R.id.item_signal_level_image)
        ImageView mItemSignalLevelImage;
        @BindView(R.id.item_channel)
        TextView mItemChannel;
        @BindView(R.id.item_primary_frequency)
        TextView mItemPrimaryFrequency;
        @BindView(R.id.item_channel_frequency_range)
        ImageView mItemChannelFrequencyRange;
        @BindView(R.id.item_channel_frequency_width)
        TextView mItemChannelFrequencyWidth;
        @BindView(R.id.item_vendor_short)
        TextView mItemVendorShort;
        @BindView(R.id.item_security_image)
        ImageView mItemSecurityImage;
        @BindView(R.id.item_capabilities)
        TextView mItemCapabilities;

        public MyViewHolder(View itemView) {
            super(itemView);
        }

        protected void bind(AccessPoint accessPoint) {
            mItemSsid.setText(accessPoint.getSsid());
        }
    }

    public AccessPointAdapter() {
    }

    public AccessPointAdapter(Context mContext, List<AccessPoint> itemList) {
        this.mContext = mContext;
        this.mAccessPointList = itemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MyViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.item_accesspoint, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        AccessPoint accessPoint = mAccessPointList.get(position);
        holder.bind(accessPoint);
        holder.mCardView.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setList(List<AccessPoint> accessPointList) {
        this.mAccessPointList = accessPointList;
        notifyDataSetChanged();
    }

    public List<AccessPoint> getAccessPointList() {
        return this.mAccessPointList;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int pos);
    }
}
