package com.vtech.mobile.kidiconnect2021.customcamera.effect.load.base;

public class EffectModel {

    public static final String EMPTY_EFFECT_NAME = "empty_effect";
    private String name;
    private String dir;
    private String thumb;

    public EffectModel(String name, String dir, String thumb) {
        this.name = name;
        this.dir = dir;
        this.thumb = thumb;

    }

    public String getName() {
        return name;
    }

    public String getDir() {
        return dir;
    }

    public String getThumb() {
        return thumb;
    }

}
