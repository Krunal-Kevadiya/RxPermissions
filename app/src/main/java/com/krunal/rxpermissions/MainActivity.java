package com.krunal.rxpermissions;

import android.Manifest;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kevadiyakrunalk.rxpermissions.DialogCallback;
import com.kevadiyakrunalk.rxpermissions.PermissionCallback;
import com.kevadiyakrunalk.rxpermissions.PermissionStatus;
import com.kevadiyakrunalk.rxpermissions.RxPermissions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxPermissions.getInstance()
                        .with(MainActivity.this)
                        .showRationalDialog("Allow camera, storage and location access", "Without camera, storage and location permission we are unable to take product image or save and take product image location, .Go ahead and grand permission.")
                        .showAccessRemovedDialog("Permission Error", "Setting dialog message")
                        .checkPermission(true, true, new PermissionCallback() {
                                    @Override
                                    public void onPermission(PermissionStatus status, String... permission) {
                                        ((TextView) findViewById(R.id.textView))
                                                .setText("Permission -> " + status + ", granted - > " + permission + "\n");
                                    }

                                    @Override
                                    public void onRational(final DialogCallback callback, String... permission) {
                                                 /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                                                 alertDialogBuilder.setMessage("custom rational");
                                                 alertDialogBuilder.setTitle("Rational");

                                                 alertDialogBuilder.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                                                     @Override
                                                     public void onClick(DialogInterface dialog, int arg) {
                                                         dialog.dismiss();
                                                         callback.onPositiveButton();
                                                     }
                                                 });
                                                 alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                                     @Override
                                                     public void onClick(DialogInterface dialog, int arg) {
                                                         dialog.dismiss();
                                                         callback.onNegativeButton();
                                                     }
                                                 });

                                                 alertDialogBuilder.setCancelable(false);
                                                 alertDialogBuilder.show();*/
                                    }

                                    @Override
                                    public void onAccessRemoved(final DialogCallback callback, String... permission) {
                                                 /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                                                 alertDialogBuilder.setMessage("custom Access Removed");
                                                 alertDialogBuilder.setTitle("Setting");

                                                 alertDialogBuilder.setPositiveButton("SETTING", new DialogInterface.OnClickListener() {
                                                     @Override
                                                     public void onClick(DialogInterface dialog, int arg) {
                                                         dialog.dismiss();
                                                         callback.onPositiveButton();
                                                     }
                                                 });
                                                 alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                                     @Override
                                                     public void onClick(DialogInterface dialog, int arg) {
                                                         dialog.dismiss();
                                                         callback.onNegativeButton();
                                                     }
                                                 });

                                                 alertDialogBuilder.setCancelable(false);
                                                 alertDialogBuilder.show();*/
                                    }
                                }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);

            }
        });
    }
}
