package com.vtech.mobile.kidiconnect2021.customcamera.utils;

public class KCConfigs {

    private static String countryLanguage = "USeng";
    private static String KC_FACIAL_MASK_DOMAIN = "https://contentcdn.vtechda.com/";

    public static String getCountryLanguage() {
        return countryLanguage;
    }

    public static void setCountryLanguage(String countryLanguage) {
        KCConfigs.countryLanguage = countryLanguage;
    }

    public static String getFacialMaskJsonUrl() {
        return KC_FACIAL_MASK_DOMAIN + "Data/KCData/MasksData/" + getCountryLanguage() + "/FacialMask.json";
    }

    public static String getKcFacialMaskDomain() {
        return KC_FACIAL_MASK_DOMAIN;
    }
}
