package com.kevadiyakrunalk.rxpermissions;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import static com.kevadiyakrunalk.rxpermissions.RxPermissions.REQUEST_CODE;

public class RxSettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            handleIntent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent();
    }

    private void handleIntent() {
        try {
            Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + RxPermissions.getInstance().getPermissionBean().getStrPackage()));
            myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(myAppSettings, REQUEST_CODE);
        } catch(Exception e) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == REQUEST_CODE) {
                RxPermissions.getInstance().onActivityResult(REQUEST_CODE, Activity.RESULT_OK, null);
                finish();
            }
        } catch(Exception e) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}