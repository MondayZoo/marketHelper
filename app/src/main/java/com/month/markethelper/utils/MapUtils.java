package com.month.markethelper.utils;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;

public class MapUtils {

    public static AMapLocationClient getLocationClient(Context context) {
        AMapLocationClient locationClient = new AMapLocationClient(context);
        AMapLocationClientOption locationOption = new AMapLocationClientOption();
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        locationOption.setOnceLocation(true);
        locationClient.setLocationOption(locationOption);
        return locationClient;
    }

}
