package com.vtech.mobile.kidiconnect2021.customcamera.effect.load.facialmask;

import java.io.Serializable;

public class FacialMaskInfo implements Serializable {

    private String location;
    private Double file_version;
    private Double icon_file_version;
    private Double facial_data_version;
    private IconData icon_data;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getFile_version() {
        return file_version;
    }

    public void setFile_version(Double file_version) {
        this.file_version = file_version;
    }

    public Double getIcon_file_version() {
        return icon_file_version;
    }

    public void setIcon_file_version(Double icon_file_version) {
        this.icon_file_version = icon_file_version;
    }

    public Double getFacial_data_version() {
        return facial_data_version;
    }

    public void setFacial_data_version(Double facial_data_version) {
        this.facial_data_version = facial_data_version;
    }

    public IconData getIcon_data() {
        return icon_data;
    }

    public void setIcon_data(IconData icon_data) {
        this.icon_data = icon_data;
    }

    @Override
    public String toString() {
        return "FacialMaskInfo{" +
                "location='" + location + '\'' +
                ", file_version=" + file_version +
                ", icon_file_version=" + icon_file_version +
                ", facial_data_version=" + facial_data_version +
                ", icon_data=" + icon_data +
                '}';
    }
}
