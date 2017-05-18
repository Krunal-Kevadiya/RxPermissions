package com.kevadiyakrunalk.rxpermissions;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by KevadiyaKrunalK on 26-03-2017.
 */
@TargetApi(M)
public class RxPermissionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        try {
            requestPermissions(RxPermissions.getInstance().getPermissionBean().getPermission(), RxPermissions.REQUEST_CODE);
        } catch(Exception e) {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try {
            RxPermissions.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
            finish();
        } catch(Exception e) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            RxPermissions.getInstance().onActivityResult(requestCode, resultCode, data);
            finish();
        } catch(Exception e) {
            finish();
        }
    }
}