package cn.liudp.wifisignalstrength;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;
import com.yinglan.alphatabs.AlphaTabsIndicator;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import cn.liudp.wifisignalstrength.base.BaseActivity;
import cn.liudp.wifisignalstrength.fragments.AboutFragment;
import cn.liudp.wifisignalstrength.fragments.MainFragment;
import io.reactivex.Observable;

/**
 * @author dongpoliu on 2018-03-15.
 */

public class MainActivity extends BaseActivity{

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.bottom_bar)
    AlphaTabsIndicator mBottomBar;
    int mTabIndex;
    int count = 1;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mainAdapter);
        mViewPager.addOnPageChangeListener(mainAdapter);
        mBottomBar.setViewPager(mViewPager);
        mBottomBar.getTabView(0).showNumber(6);
        mBottomBar.getTabView(1).showPoint();
    }

    private class MainAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

        private List<Fragment> fragments = new ArrayList<>();
        private String[] titles = {getString(R.string.home), getString(R.string.me)};

        public MainAdapter(FragmentManager fm) {
            super(fm);
            fragments.add(MainFragment.newInstance(titles[0]));
            fragments.add(AboutFragment.newInstance(titles[1]));
        }

        @Override
        public Fragment getItem(int position) {
            mTabIndex = position;
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (0 == position) {
                mBottomBar.getTabView(0).showNumber(mBottomBar.getTabView(0).getBadgeNumber() - 1);
            } else if (2 == position) {
                mBottomBar.getCurrentItemView().removeShow();
            } else if (3 == position) {
                mBottomBar.removeAllBadge();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void onBackPressedSupport() {
        if (mTabIndex != 1) {
            mBottomBar.setTabCurrenItem(0);
        }else {
            if (count != 2) {
                Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.finishAfterTransition(this);
            }
            count = 2;
            Observable.timer(2, TimeUnit.SECONDS)
                .compose(bindToLifecycle())
                .subscribe(aLong -> count = 1);
        }
    }
}
