package com.vtech.mobile.kidiconnect2021.customcamera.effect.load.facialmask;

import com.vtech.mobile.kidiconnect2021.customcamera.effect.load.base.EffectModel;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.load.base.LocalEffects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacialMask implements Serializable {
    private FacialMaskInfo info;
    private List<TiFancyItem> mask_list;
    private List<TiFancyItem> sticker_list;
    private List<TiFancyItem> comb_list;
    public FacialMaskInfo getInfo() {
        return info;
    }

    public void setInfo(FacialMaskInfo info) {
        this.info = info;
    }

    public List<TiFancyItem> getMask_list() {
        return mask_list;
    }

    public void setMask_list(List<TiFancyItem> mask_list) {
        this.mask_list = mask_list;
    }

    public List<TiFancyItem> getSticker_list() {
        return sticker_list;
    }

    public void setSticker_list(List<TiFancyItem> sticker_list) {
        this.sticker_list = sticker_list;
    }

    public List<TiFancyItem> getComb_list() {
        return comb_list;
    }

    public void setComb_list(List<TiFancyItem> comb_list) {
        this.comb_list = comb_list;
    }


    @Override
    public String toString() {
        return "FacialMask{" +
                "info=" + info +
                ", mask_list=" + mask_list +
                ", sticker_list=" + sticker_list +
                ", comb_list=" + comb_list +
                '}';
    }

    public LocalEffects toKcEffectConfigModel(){
        Map<String, EffectModel> temp = new HashMap<>();

        for (TiFancyItem t : mask_list) {
            temp.put(t.getName(),t.toKcEffectModel());
        }
        for (TiFancyItem t : sticker_list) {
            temp.put(t.getName(),t.toKcEffectModel());
        }
        for (TiFancyItem t : comb_list) {
            temp.put(t.getName(),t.toKcEffectModel());
        }

        return new LocalEffects(temp,"","",null);
    }
}
