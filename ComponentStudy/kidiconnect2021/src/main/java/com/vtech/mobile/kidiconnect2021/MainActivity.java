package com.vtech.mobile.kidiconnect2021;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.vtech.mobile.kidiconnect2021.customcamera.effect.fragment.EffectFragment;
import com.vtech.mobile.kidiconnect2021.customcamera.utils.ActivityUtils;
import com.vtech.mobile.kidiconnect2021.customview.RecordCircleView;
import com.vtech.mobile.kidiconnect2021.databinding.ActivityMainBinding;
import com.vtech.mobile.kidiconnect2021.viewmodel.HomeViewModel;
import com.vtech.mobile.kidiconnect2021.viewmodel.HomeViewModelFactory;

public class MainActivity extends PermissionActivity implements View.OnClickListener,RecordCircleView.RecordCircleOnClickListener{

    private ActivityMainBinding binding;

    private final long recordTime = 10000L;

    private long startRecordTime;

    private long maskTime = 0L;

    private HomeViewModel homeViewModel;

    private EffectFragment effectFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(binding.getRoot());
        binding.customSurfaceView.setActivity(MainActivity.this);
        initHomeViewListener();
        homeViewModel = new ViewModelProvider(this,new HomeViewModelFactory()).get(HomeViewModel.class);
        binding.setHomeViewModel(homeViewModel);
    }

    private void initHomeViewListener() {
        binding.btnMask.setOnClickListener(this);
        binding.btnRecord.setAnimateRingColor(Color.RED);
        binding.btnRecord.enableManualStartProgress();
        binding.btnRecord.setRecordCircleOnClickListener(this);
        binding.btnRecord.setTime(recordTime);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_mask:
                // closeOrOpenMask();
        }
    }

    private void closeOrOpenMask() {
        if (System.currentTimeMillis() - maskTime <= 200L) {
            return;
        }
        maskTime = System.currentTimeMillis();
        if(Boolean.TRUE.equals(homeViewModel.getIsMaskVisible().getValue())){
            // close mask
            homeViewModel.getIsMaskVisible().setValue(false);
            if (effectFragment != null) {
                effectFragment.setUserClickToClose(true);
                ActivityUtils.removeFragmentToActivity(getSupportFragmentManager(), effectFragment);
                effectFragment = null;
            }
        }else {
            // show mask
            homeViewModel.getIsMaskVisible().setValue(true);
            // 碎片
           effectFragment = new EffectFragment();
            // 显示碎片
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), effectFragment, R.id.fl_mask_list);
        }
    }



    @Override
    public boolean onCircleBarClick() {
        return false;
    }

    @Override
    public void onCircleBarComplete() {

    }
}