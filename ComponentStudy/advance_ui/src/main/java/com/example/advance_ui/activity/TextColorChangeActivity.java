package com.example.advance_ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.advance_ui.R;
import com.example.advance_ui.custom.TextColorChangeView;

import java.util.ArrayList;
import java.util.List;

public class TextColorChangeActivity extends AppCompatActivity {

    private ViewPager vpPage;
    private LinearLayout llTabContainer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_main4);
        vpPage = findViewById(R.id.vpPage);
        llTabContainer = findViewById(R.id.llTabContainer);
        setData();
    }


    void setData() {
        final List<TextView> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            TextView textView = new TextView(this);
            textView.setText("page: " + i);
            textView.setTextSize(38);
            textView.setTextColor(Color.BLUE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            params.leftMargin = 100;
            params.topMargin = 200;
            textView.setLayoutParams(params);
            list.add(textView);
        }


        vpPage.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                container.addView(list.get(position));

                return list.get(position);
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        });
        vpPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                System.out.println("position: " + position);
                System.out.println("positionOffset: " + positionOffset);
                System.out.println("positionOffsetPixels: " + positionOffsetPixels);
                /**
                 * 通过下标获取当前的指示器
                 * 设置after为false表示当前不是下一个要完全显示的指示器
                 * 设置进度为 1-positionoffset代表着将当前显示的指示器渐渐取消掉高亮
                 */
                TextColorChangeView cur = (TextColorChangeView) llTabContainer.getChildAt(position);
                cur.setAfter(false);
                cur.setProgress(1 - positionOffset);

                /**
                 * 下一个不等于空的情况下设置为要显示的指示器，并把当前viewpager页面显示的进度传递给指示器用作渐变效果
                 */
                TextColorChangeView next = (TextColorChangeView) llTabContainer.getChildAt(position + 1);
                if (next != null) {
                    next.setAfter(true);
                    next.setProgress(positionOffset);
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        TextColorChangeView childAt = (TextColorChangeView) llTabContainer.getChildAt(0);
        childAt.setProgress(1.0f);
    }
}

