package com.vtech.mobile.kidiconnect2021.customcamera.effect.load.facialmask;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.load.base.EffectModel;

import java.io.Serializable;

public class TiFancyItem implements Serializable {
    private String datafolder;
    @SerializedName("builtinflag")
    private Boolean builtin;
    @SerializedName("dataname")
    private String name;
    private String thumb;
    private String type;
    private String url;
    private Double version;
    private Integer ordernumber;

    public String getDatafolder() {
        return datafolder;
    }

    public void setDatafolder(String datafolder) {
        this.datafolder = datafolder;
    }

    public Boolean getBuiltin() {
        return builtin;
    }

    public void setBuiltin(Boolean builtin) {
        this.builtin = builtin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public Integer getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(Integer ordernumber) {
        this.ordernumber = ordernumber;
    }

    @Override
    public String toString() {
        return "TiFancyItem{" +
                "datafolder='" + datafolder + '\'' +
                ", builtin=" + builtin +
                ", name='" + name + '\'' +
                ", thumb='" + thumb + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", version=" + version +
                ", ordernumber=" + ordernumber +
                '}';
    }

    public EffectModel toKcEffectModel() {
        // 此处需要判断是否为内建Effect
        if (builtin) {
            Log.d("TiFancyItem","url : "+url);
            return new EffectModel(name, datafolder, thumb, false, false, type, version, url, ordernumber);
        } else {
            return new EffectModel(name, datafolder, thumb, true, false, type, version, url, ordernumber);
        }

    }

}
