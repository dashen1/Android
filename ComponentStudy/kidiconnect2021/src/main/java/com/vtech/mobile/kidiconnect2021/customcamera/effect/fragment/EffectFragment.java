package com.vtech.mobile.kidiconnect2021.customcamera.effect.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.vtech.mobile.kidiconnect2021.BackStage.BackStageService;
import com.vtech.mobile.kidiconnect2021.MainActivity;
import com.vtech.mobile.kidiconnect2021.R;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.EffectContext;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.EffectUtils;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.adapter.ImageAdapter;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.adapter.LooperLayoutManager;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.fragment.view.MaskRecyclerView;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.fragment.view.SingleCircleView;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.load.base.EffectModel;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.load.base.LocalEffects;
import com.vtech.mobile.kidiconnect2021.customview.MaskSnapHelper;
import com.vtech.mobile.kidiconnect2021.databinding.FragmentMaskBinding;

import java.util.ArrayList;
import java.util.List;

import cn.tillusory.sdk.TiSDKManager;

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
        EffectContext.notifyOpenEffectFragment(this);
        // addEffectListAsync();
    }

    /**
     * 异步刷新Effect，会使用异步线程去请求WEB服务器上资源
     * 以及本地目录下的资源（本地目录中的资源由工具上传）
     */
    private void addEffectListAsync() {
        Log.d(TAG, "start to request web effect , requesting = " + EffectContext.isWebRequesting());

        if (!EffectContext.isWebRequesting()){
            EffectContext.setWebRequesting(true);

            BackStageService.getInstance().getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    EffectContext.setWebRequesting(true);
                    Log.d(TAG, "Start the child thread to request the Web Effect list : " + Thread.currentThread().getName());
                    try {
                        LocalEffects effects = EffectContext.requestWebEffectList();
                        if (effects != null) {
                            Log.d(TAG, "The latest Web data insert to local : Is about to begin");
                            EffectContext.updateRemoteHandle(effects);
                        }
                    }catch (Exception e){
                        Log.e(TAG, "Start the child thread to request the Web Effect list ----> error " + e.getMessage());
                    }finally {
                        EffectContext.setWebRequesting(false);
                    }
                }
            });
        }
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
        // fragmentMaskBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mask, container, false);
        View view = inflater.inflate(R.layout.fragment_mask, container, false);
        maskRecyclerView = view.findViewById(R.id.home_mask_list_recyclerview);
        circleView = view.findViewById(R.id.circle_view);
        return view;
    }

    public void setUserClickToClose(boolean close) {
        this.userClickToClose = close;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (userClickToClose) {
            EffectContext.notifyCloseEffectFragment();
        } else {
            try {
                // 关闭列表时，把所有的特效都关闭了
                TiSDKManager.getInstance().setSticker("");
                TiSDKManager.getInstance().setMask("");
            } catch (Exception ignored) {

            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 加载Mask数据
        // List<ImageAdapter.ItemModel> itemModels = createTestData();
        List<ImageAdapter.ItemModel> itemModels = new ArrayList<>(EffectContext.getModelList().size());

        for (int i = 0; i < EffectContext.getModelList().size(); i++) {
            Log.d(TAG,"ItemModel : "+EffectContext.getModelList().get(i));
            itemModels.add(new ImageAdapter.ItemModel(EffectContext.getModelList().get(i)));
        }

        // 数据兜底:如果加载不到数据，则再尝试一下
        if (itemModels.size() == 0) {
            Log.e(TAG, "加载Mask数据错误: size = 0");
            // 尝试重新读取数据 Todo
            // EffectContext.notifyOpenEffectFragment(this);
            // 重新装配数据
            itemModels = new ArrayList<>(EffectContext.getModelList().size());
            for (int i = 0; i < EffectContext.getModelList().size(); i++) {
                itemModels.add(new ImageAdapter.ItemModel(EffectContext.getModelList().get(i)));
            }
            if (itemModels.size() == 0) {
                Log.e(TAG, "重新加载Mask数据错误: size = 0");
            }
        } else {
            Log.d(TAG, "加载Mask数据: mask list size = " + itemModels.size());
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
        EffectUtils.setEffect(itemModels.get(0).getModel());

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

        //maskRecyclerView.setMaxFlingVelocityX(3000);

        MaskSnapHelper snapHelper = new MaskSnapHelper();
        maskRecyclerView.setMaskSnapHelper(snapHelper);

        snapHelper.attachToRecyclerView(maskRecyclerView);
    }

    public static List<ImageAdapter.ItemModel> createTestData(){
        List<ImageAdapter.ItemModel> list = new ArrayList<>();
        ImageAdapter.ItemModel itemModel;
        EffectModel effectModel;
        for (int i = 0; i < 10; i++) {
            effectModel = new EffectModel("CatR","","");
            itemModel = new ImageAdapter.ItemModel(effectModel);
            list.add(itemModel);
        }
        return list;
    }
}
