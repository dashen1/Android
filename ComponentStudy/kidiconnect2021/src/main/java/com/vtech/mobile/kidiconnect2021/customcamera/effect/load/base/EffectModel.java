package com.vtech.mobile.kidiconnect2021.customcamera.effect.load.base;

import com.vtech.mobile.kidiconnect2021.customcamera.utils.FileManagerUtil;

import java.io.File;

public class EffectModel {

    public static final String EMPTY_EFFECT_NAME = "empty_effect";
    private String dir;
    private boolean downloaded;
    private boolean isTest;
    private String name;
    private int order = -1;
    private boolean remote;
    private String thumb;
    private String type;
    private Double version;
    private String url;

    public EffectModel(String name, String dir, String thumb) {
        this.name = name;
        this.dir = dir;
        this.thumb = thumb;
    }

    public EffectModel(String dir, boolean downloaded, boolean isTest, String name, int order, boolean remote, String thumb, String type, Double version, String url) {
        this.dir = dir;
        this.downloaded = downloaded;
        this.isTest = isTest;
        this.name = name;
        this.order = order;
        this.remote = remote;
        this.thumb = thumb;
        this.type = type;
        this.version = version;
        this.url = url;
    }

    public EffectModel(String name, String dir, String thumb, boolean remote, boolean downloaded, String type, Double version, String url, int order) {
        this.name = name;
        this.dir = dir;
        this.thumb = thumb;
        this.remote = remote;
        this.downloaded = downloaded;
        this.type = type;
        this.version = version;
        this.url = url;
        this.isTest = isTest;
        this.order = order;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isRemote() {
        return remote;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getThumbPath() {
        if (EffectConstant.TYPE_MASK.equals(type))
            return FileManagerUtil.getEffectIconPath() + "mask_icon/" + thumb;
        else if (EffectConstant.TYPE_STICKER.equals(type))
            return FileManagerUtil.getEffectIconPath() + "sticker_icon/" + thumb;
        else
            // 组合类型的 icon
            return FileManagerUtil.getEffectIconPath() + "comb_icon/" + thumb;
    }

    /**
     * 是否为空特效
     */
    public boolean isEmptyType() {
        return EffectModel.EMPTY_EFFECT_NAME.equals(name);
    }

    public boolean isSticker() {
        return EffectConstant.TYPE_STICKER.equals(type);
    }

    /**
     * 判断文件是否可用
     */
    public boolean isFileAvailable() {
        if (isMask()) {
            File file = new File(FileManagerUtil.getMaskPath() + name);
            return file.exists() && file.isDirectory();
        } else if (isSticker()) {
            File file = new File(FileManagerUtil.getStickerPath() + name);
            return file.exists() && file.isDirectory();
        } else if (isComb()) {
            File maskF = new File(FileManagerUtil.getMaskPath() + name);
            File stickerF = new File(FileManagerUtil.getStickerPath() + name);
            return maskF.exists() && maskF.isDirectory() && stickerF.exists() && stickerF.isDirectory();
        }
        return false;
    }

    public boolean isMask() {
        return EffectConstant.TYPE_MASK.equals(type);
    }

    public boolean isComb() {
        return EffectConstant.TYPE_COMB.equals(type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
