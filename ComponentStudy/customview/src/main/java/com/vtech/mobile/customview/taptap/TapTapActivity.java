package com.vtech.mobile.customview.taptap;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.FractionRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.vtech.mobile.customview.R;
import com.vtech.mobile.customview.taptap.fragments.RecommendFragment;
import com.vtech.mobile.customview.taptap.fragments.VideoFragment;
import com.vtech.mobile.customview.taptap.widget.TextColorChangeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TapTapActivity extends AppCompatActivity {

    private static final String TAG = "TapTapActivity";

    @BindView(R.id.vpBody)
    ViewPager vpBody;
    @BindView(R.id.rlNavigationBar)
    RelativeLayout rlNavigationBar;
    @BindView(R.id.llTitleContainer)
    LinearLayout llTitleContainer;

    private List<Fragment> fragmentList;
    private int oldY = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_taptap);
        ButterKnife.bind(this);

        fragmentList = new ArrayList<>();
        fragmentList.add(RecommendFragment.getInstance(1));
        fragmentList.add(new VideoFragment());
        vpBody.setAdapter(new BodyPageAdapter(getSupportFragmentManager()));

        vpBody.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                TextColorChangeView one = (TextColorChangeView) llTitleContainer.getChildAt(position);
                // 往左滑的时候，position就是当前，往右滑的时候，position就是前一个
                one.setAfter(false); // 第一个就后面高亮
                one.setProgress(1-positionOffset);

                TextColorChangeView two = (TextColorChangeView) llTitleContainer.getChildAt(position+1);
                if (two!=null){
                    two.setAfter(true);// 第二个就前面高亮
                    two.setProgress(positionOffset); // 前面一个的偏移量就是后一个前面高亮的部分
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: " + position + " Y: " + rlNavigationBar.getTranslationY());
                if (position==1){
                    oldY = (int) rlNavigationBar.getTranslationY();
                    rlNavigationBar.setTranslationY(0);
                }else {
                    rlNavigationBar.setTranslationY(oldY);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    class BodyPageAdapter extends FragmentPagerAdapter{

        public BodyPageAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
