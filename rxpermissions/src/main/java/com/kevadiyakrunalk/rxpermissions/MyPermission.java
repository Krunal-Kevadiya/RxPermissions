package com.kevadiyakrunalk.rxpermissions;

import android.app.Application;

/**
 * Created by Patidar on 16-05-2017.
 */

public class MyPermission extends Application {
    public static RxPermissions sSingleton;

    @Override
    public void onCreate() {
        super.onCreate();
        sSingleton = new RxPermissions();
    }
}
