package com.vtech.mobile.kidiconnect2021.customcamera.effect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vtech.mobile.kidiconnect2021.R;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.fragment.view.MaskRecyclerView;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.load.base.EffectModel;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> implements MaskRecyclerView.OnItemTriggerListener {

    private static final String TAG = "ImageAdapter";
    private Context mContext;
    private MaskRecyclerView maskRecyclerView;
    private int itemCount = 6;
    private List<ItemModel> modelList;
    private volatile String currentMainMask = "";

    public ImageAdapter(Context context, MaskRecyclerView recyclerView, List<ItemModel> itemModels) {
        this.mContext = mContext;
        this.maskRecyclerView = recyclerView;
        this.modelList = modelList;
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
        holder.itemView.setTag(itemModel.model.getName());
        itemModel.setViewHolder(holder);

    }

    @Override
    public int getItemCount() {
        return 0;
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
        }
    }

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
