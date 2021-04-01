package com.month.markethelper.utils;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;

public class MapUtils {

    private static AMapLocationClient mLocationClient;

    public static AMapLocationClient getLocationClient(Context context) {
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(context);
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
            mLocationOption.setOnceLocation(true);
            mLocationClient.setLocationOption(mLocationOption);
        }
        return mLocationClient;
    }
}
