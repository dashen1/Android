package com.vtech.mobile.kidiconnect2021.customcamera.effect.load.facialmask;

import com.vtech.mobile.kidiconnect2021.customcamera.utils.KCConfigs;

import java.io.Serializable;

public class IconData implements Serializable {
    private String url;

    public String getUrl() {
        return KCConfigs.getKcFacialMaskDomain() + url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "IconData{" +
                "url='" + url + '\'' +
                '}';
    }
}
