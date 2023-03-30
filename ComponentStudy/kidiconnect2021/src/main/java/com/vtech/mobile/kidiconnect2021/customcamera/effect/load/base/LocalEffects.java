package com.vtech.mobile.kidiconnect2021.customcamera.effect.load.base;

import java.util.Map;

public class LocalEffects {
    private Map<String, EffectModel> itemList;
    private String lastMaskName = "";

    private String lastStickerName = "";
    private String lastClickType = "";
    private boolean dataHasUpdate = false;
    private double fileVersion = 0.001;
    private double factoryVersion = 0.001;

    public LocalEffects(Map<String, EffectModel> itemList, String lastMaskName, String lastStickerName, String lastClickType) {
        this.itemList = itemList;
        this.lastMaskName = lastMaskName;
        this.lastStickerName = lastStickerName;
        this.lastClickType = lastClickType;
    }

    public Map<String, EffectModel> getItemList() {
        return itemList;
    }

    public void setItemList(Map<String, EffectModel> itemList) {
        this.itemList = itemList;
    }

    public String getLastMaskName() {
        return lastMaskName;
    }

    public void setLastMaskName(String lastMaskName) {
        this.lastMaskName = lastMaskName;
    }

    public String getLastStickerName() {
        return lastStickerName;
    }

    public void setLastStickerName(String lastStickerName) {
        this.lastStickerName = lastStickerName;
    }

    public String getLastClickType() {
        return lastClickType;
    }

    public void setLastClickType(String lastClickType) {
        this.lastClickType = lastClickType;
    }

    public boolean isDataHasUpdate() {
        return dataHasUpdate;
    }

    public void setDataHasUpdate(boolean dataHasUpdate) {
        this.dataHasUpdate = dataHasUpdate;
    }

    public double getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(double fileVersion) {
        this.fileVersion = fileVersion;
    }

    public double getFactoryVersion() {
        return factoryVersion;
    }

    public void setFactoryVersion(double factoryVersion) {
        this.factoryVersion = factoryVersion;
    }
}
