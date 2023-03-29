package com.vtech.mobile.kidiconnect2021.customcamera.effect.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vtech.mobile.kidiconnect2021.MainActivity;
import com.vtech.mobile.kidiconnect2021.R;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.EffectContext;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.EffectUtils;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.adapter.ImageAdapter;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.adapter.LooperLayoutManager;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.fragment.view.MaskRecyclerView;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.fragment.view.SingleCircleView;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.load.base.EffectModel;
import com.vtech.mobile.kidiconnect2021.databinding.FragmentMaskBinding;

import java.util.ArrayList;
import java.util.List;

public class EffectFragment extends Fragment {

    private static final String TAG = "EffectFragment";
    private MaskRecyclerView maskRecyclerView;
    private SingleCircleView circleView;

    private boolean userClickToClose;

    private MainActivity mainActivity;

    private ImageAdapter imageAdapter;

    private Context context;

    private FragmentMaskBinding fragmentMaskBinding;

    public EffectFragment() {
        initData();
    }

    private void initData() {


    }

    public static EffectFragment getInstance(){
        EffectFragment fragment = new EffectFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity){
            mainActivity = (MainActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentMaskBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mask, container, false);

        return fragmentMaskBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 加载Mask数据
        List<ImageAdapter.ItemModel> itemModels = createTestData();

        for (int i = 0; i < EffectContext.getModelList().size(); i++) {
            Log.d(TAG,"ItemModel : "+EffectContext.getModelList().get(i));
            itemModels.add(new ImageAdapter.ItemModel(EffectContext.getModelList().get(i)));
        }

        LooperLayoutManager manager = new LooperLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        manager.setLooperEnable(true);

        int size = itemModels.size();
        int pageSize = 6;
        int pageMarginSize = 2;

        imageAdapter = new ImageAdapter(maskRecyclerView.getContext(), maskRecyclerView, itemModels);

        // 此处获取Icon宽度
        int itemWidth = EffectUtils.getItemSize(maskRecyclerView.getContext(), pageSize);
        int iconWidth = EffectUtils.getItemSizeComp(maskRecyclerView.getContext(), pageSize, 100);

        manager.scrollToPositionWithOffset(0, -itemWidth / 2);
        EffectUtils.setEffect(EffectContext.getModelList().get(0));

        // ================================== 设置列表显示的宽高 ==========================================

        // 设置中间位置圆环的大小
        ViewGroup.LayoutParams params = circleView.getLayoutParams();
        params.height = iconWidth;
        params.width = iconWidth;
        circleView.setLayoutParams(params);

        // 设置recyclerview的大小
        ViewGroup.LayoutParams params1 = maskRecyclerView.getLayoutParams();
        params1.height = iconWidth;
        params1.width = itemWidth * (pageSize + pageMarginSize * 2);
        maskRecyclerView.setLayoutParams(params1);

        RecyclerView.RecycledViewPool pool = maskRecyclerView.getRecycledViewPool();
        maskRecyclerView.setRecycledViewPool(pool);

        // 设置，让其显示列表
        maskRecyclerView.setAdapter(imageAdapter);
        maskRecyclerView.setLayoutManager(manager);

        maskRecyclerView.setMaxFlingVelocityX(3000);
    }

    public static List<ImageAdapter.ItemModel> createTestData(){
        List<ImageAdapter.ItemModel> list = new ArrayList<>();
        ImageAdapter.ItemModel itemModel;
        EffectModel effectModel;
        for (int i = 0; i < 30; i++) {
            effectModel = new EffectModel("CatR","","");
            itemModel = new ImageAdapter.ItemModel(effectModel);
            list.add(itemModel);
        }
        return list;
    }
}
