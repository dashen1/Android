package com.vtech.mobile.kidiconnect2021.customcamera.filters;

import android.content.Context;

import com.vtech.mobile.kidiconnect2021.R;

import cn.tillusory.sdk.TiSDKManager;
import cn.tillusory.sdk.bean.TiRotation;

public class StickerFilter extends AbstractFboFilter {


    private TiRotation drawTiRotation = TiRotation.CLOCKWISE_ROTATION_180;

    public StickerFilter(Context context) {
        super(context, R.raw.base_vert, R.raw.base_frag);
    }

    @Override
    public int onDraw(int texture, FilterChain filterChain) {
        FilterContext filterContext = filterChain.filterContext;
        int textureId = TiSDKManager.getInstance().renderTexture2D(
                texture,//2D纹理Id
                filterContext.width,//图像宽度
                filterContext.height,//图像高度
                drawTiRotation,//TiRotation枚举，图像顺时针旋转的角度
                true//图像是否左右镜像
      );
        return filterChain.process(textureId);
    }

}
