package com.vtech.mobile.kidiconnect2021.customcamera.effect.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vtech.mobile.kidiconnect2021.R;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.fragment.view.MaskRecyclerView;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.load.base.EffectModel;
import com.vtech.mobile.kidiconnect2021.customcamera.utils.UIHelper;

import java.util.List;
import java.util.Map;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> implements MaskRecyclerView.OnItemTriggerListener {

    private static final String TAG = "ImageAdapter";
    private Context mContext;
    private MaskRecyclerView maskRecyclerView;
    private int itemCount = 6;
    private List<ItemModel> modelList;
    private volatile String currentMainMask = "";

    private volatile int itemWH = -1;
    private volatile int itemWidth = -1;
    private volatile int iconWidth = -1;

    public ImageAdapter(Context context, MaskRecyclerView recyclerView, List<ItemModel> itemModels) {
        this.mContext = context;
        this.maskRecyclerView = recyclerView;
        this.modelList = itemModels;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_mask_list, parent, false);
        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        // 因为是循环拉动，此处使用取模得到在List中的位置
        int pos = holder.getLayoutPosition() % modelList.size();
        ItemModel itemModel = modelList.get(pos);
        // 设置icon图片
        holder.iconView.setImageResource(R.drawable.icon_cat);

        // ======================= 位置关系绑定 ==================================
        // 设置对应ItemModel
//        holder.itemView.setTag(itemModel.model.getName());
//        itemModel.setViewHolder(holder);
        try {
            if (itemWH == -1) {
                int widthPixels = holder.iconView.getContext().getResources().getDisplayMetrics().widthPixels;
                int size = widthPixels / itemCount;
                int pi = UIHelper.dip2px(holder.iconView.getContext(), 100);

                itemWidth = size;

                if (size > pi) {
                    size = pi - 2;
                }

                itemWH = size;

                // 设置ItemView的大小
                ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                params.width = itemWidth;
                params.height = itemWH;
                holder.itemView.setLayoutParams(params);

                // 适当减小图片大小
                iconWidth = itemWH - UIHelper.dip2px(holder.iconView.getContext(), 12);

                // 设置Image的大小
                ViewGroup.LayoutParams imageViewParams = holder.iconView.getLayoutParams();
                imageViewParams.width = iconWidth;
                imageViewParams.height = iconWidth;
                holder.iconView.setLayoutParams(imageViewParams);

            } else {
                // 设置ItemView的大小
                if (holder.itemView.getLayoutParams().width != itemWidth
                        || holder.itemView.getLayoutParams().height != itemWH) {
                    ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
                    layoutParams.width = itemWidth;
                    layoutParams.height = itemWH;
                    holder.itemView.setLayoutParams(layoutParams);
                }

                // 设置Image的大小
                ViewGroup.LayoutParams imageViewParams = holder.iconView.getLayoutParams();
                imageViewParams.width = iconWidth;
                imageViewParams.height = iconWidth;
                holder.iconView.setLayoutParams(imageViewParams);

            }
        } catch (Exception e) {
            Log.e(TAG, "调整Item大小时错误: " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size()*100;
    }

    @Override
    public void onTriggerAfterSlide(View mainItemView) {

    }

    @Override
    public void updateListShow() {

    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView iconView;
        private String bindingEffectName = "";

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            iconView = itemView.findViewById(R.id.image_view);
        }
    }

    private Map<String, Drawable> iconDrawableMap;

    public static class ItemModel {

        private EffectModel model;
        private ImageViewHolder viewHolder;
        private int position;

        public ItemModel(EffectModel model) {
            this.model = model;
        }

        public EffectModel getModel() {
            return model;
        }

        public void setModel(EffectModel model) {
            this.model = model;
        }

        public ImageViewHolder getViewHolder() {
            return viewHolder;
        }

        public void setViewHolder(ImageViewHolder viewHolder) {
            this.viewHolder = viewHolder;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}
