package com.kevadiyakrunalk.rxpermissions;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import java.lang.ref.WeakReference;

public class RxPermissions  {
    public static RxPermissions sSingleton;
    private boolean isShowRation;
    private boolean isShowSetting;
    private boolean isFTRation;
    private boolean isFTSetting;
    static int REQUEST_CODE = 42;
    private PermissionBean permissionBean;
    private DialogCallback dialogCallback;
    private PermissionCallback permissionCallback;
    private WeakReference<Activity> mActivityReference;

    public static RxPermissions getInstance(Activity activity) {
        if (sSingleton == null) {
            synchronized (RxPermissions.class) {
                if (sSingleton == null) {
                    sSingleton = new RxPermissions(activity);
                }
            }
        }
        return sSingleton;
    }

    private RxPermissions(Activity activity) {
        isShowRation = false;
        isShowSetting = false;
        mActivityReference = new WeakReference<>(activity);
        dialogCallback = new DialogCallback() {
            @Override
            public void onPositiveButton() {
                if(isFTRation) {
                    if(isShowRation)
                        startPermissionActivity();
                    else {
                        permissionBean.setStatus(PermissionStatus.DENIED);
                        permissionCallback.onPermission(permissionBean.getStatus(), permissionBean.getPermission());
                    }
                } else {
                    if(isShowSetting)
                        startSettingActivity();
                    else {
                        permissionBean.setStatus(PermissionStatus.ACCESS_REMOVED);
                        permissionCallback.onPermission(permissionBean.getStatus(), permissionBean.getPermission());
                    }
                }
            }

            @Override
            public void onNegativeButton() {
                if(isFTRation) {
                    permissionBean.setStatus(PermissionStatus.DENIED);
                    permissionCallback.onPermission(permissionBean.getStatus(), permissionBean.getPermission());
                } else {
                    permissionBean.setStatus(PermissionStatus.ACCESS_REMOVED);
                    permissionCallback.onPermission(permissionBean.getStatus(), permissionBean.getPermission());
                }
            }
        };
        permissionBean = new PermissionBean();
        permissionBean.setStrPackage(mActivityReference.get().getPackageName());
    }

    public PermissionBean getPermissionBean() {
        return permissionBean;
    }

    public RxPermissions showRationalDialog(String title, String message) {
        permissionBean.setRationalTitle(title);
        permissionBean.setRationalMessage(message);
        return sSingleton;
    }

    public RxPermissions showRationalDialog(int titleResId, int messageResId) {
        if(mActivityReference.get() != null) {
            permissionBean.setRationalTitle(mActivityReference.get().getString(titleResId));
            permissionBean.setRationalMessage(mActivityReference.get().getString(messageResId));
        }
        return sSingleton;
    }

    public RxPermissions showAccessRemovedDialog(String title, String message) {
        permissionBean.setAccessRemovedTitle(title);
        permissionBean.setAccessRemovedMessage(message);
        return sSingleton;
    }

    public RxPermissions showAccessRemovedDialog(int titleResId, int messageResId) {
        if(mActivityReference.get() != null) {
            permissionBean.setAccessRemovedTitle(mActivityReference.get().getString(titleResId));
            permissionBean.setAccessRemovedMessage(mActivityReference.get().getString(messageResId));
        }
        return sSingleton;
    }

    public void checkPermission(boolean showRation, boolean showSetting, PermissionCallback callback, String... permission) {
        isFTRation = isFTSetting = false;
        isShowRation = showRation;
        isShowSetting = showSetting;
        permissionBean.setPermission(permission);
        permissionCallback = callback;
        if(isMarshmallowOrAbove()) {
            if(isGranted(permissionBean.getPermission())) {
                permissionBean.setStatus(PermissionStatus.GRANTED);
                permissionCallback.onPermission(permissionBean.getStatus(), permissionBean.getPermission());
            } else if(isRationale(permissionBean.getPermission())) {
                if(isShowRation)
                    showRationalMessage();
                else {
                    permissionBean.setStatus(PermissionStatus.DENIED);
                    permissionCallback.onPermission(permissionBean.getStatus(), permissionBean.getPermission());
                }
            } else
                startPermissionActivity();
        } else {
            permissionBean.setStatus(PermissionStatus.GRANTED);
            permissionCallback.onPermission(permissionBean.getStatus(), permissionBean.getPermission());
        }
    }

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == REQUEST_CODE) {
            if(isGranted(grantResults)) {
                permissionBean.setStatus(PermissionStatus.GRANTED);
                permissionCallback.onPermission(permissionBean.getStatus(), permissionBean.getPermission());
            } else if(!isRationale(permissions)) {
                if(isShowSetting)
                    doNotAskedEnable();
                else {
                    permissionBean.setStatus(PermissionStatus.ACCESS_REMOVED);
                    permissionCallback.onPermission(permissionBean.getStatus(), permissionBean.getPermission());
                }
            } else {
                if(isShowRation)
                    showRationalMessage();
                else {
                    permissionBean.setStatus(PermissionStatus.DENIED);
                    permissionCallback.onPermission(permissionBean.getStatus(), permissionBean.getPermission());
                }
            }
        } else {
            permissionBean.setStatus(PermissionStatus.ERROR);
            permissionCallback.onPermission(permissionBean.getStatus(), permissionBean.getPermission());
        }
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE) {
            if(isGranted(permissionBean.getPermission())) {
                permissionBean.setStatus(PermissionStatus.GRANTED);
                permissionCallback.onPermission(permissionBean.getStatus(), permissionBean.getPermission());
            } else {
                permissionBean.setStatus(PermissionStatus.ACCESS_REMOVED);
                permissionCallback.onPermission(permissionBean.getStatus(), permissionBean.getPermission());
            }
        } else {
            permissionBean.setStatus(PermissionStatus.ERROR);
            permissionCallback.onPermission(permissionBean.getStatus(), permissionBean.getPermission());
        }
    }

    private boolean isMarshmallowOrAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isGranted(int... grantResults) {
        if (grantResults.length < 1)
            return false;

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isGranted(String... permissions) {
        if(mActivityReference != null && mActivityReference.get()!= null) {
            for (String permission : permissions) {
                if (mActivityReference.get().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                    return false;
            }
            return true;
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isRationale(String... permissions) {
        if(mActivityReference != null && mActivityReference.get()!= null) {
            for (String permission : permissions) {
                if (mActivityReference.get().shouldShowRequestPermissionRationale(permission))
                    return true;
            }
        }
        return false;
    }

    private void showRationalMessage() {
        if(!isFTRation) {
            isFTRation = true;
            isFTSetting = false;
            if (TextUtils.isEmpty(permissionBean.getRationalTitle()) || TextUtils.isEmpty(permissionBean.getRationalMessage()))
                permissionCallback.onRational(dialogCallback, permissionBean.getPermission());
            else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivityReference.get());
                alertDialogBuilder.setMessage(permissionBean.getRationalMessage());
                alertDialogBuilder.setTitle(permissionBean.getRationalTitle());

                alertDialogBuilder.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg) {
                        dialog.dismiss();
                        startPermissionActivity();
                    }
                });
                alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg) {
                        dialog.dismiss();
                        permissionBean.setStatus(PermissionStatus.DENIED);
                        permissionCallback.onPermission(permissionBean.getStatus(), permissionBean.getPermission());
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
            }
        } else {
            permissionBean.setStatus(PermissionStatus.DENIED);
            permissionCallback.onPermission(permissionBean.getStatus(), permissionBean.getPermission());
        }
    }

    private void doNotAskedEnable() {
        if(!isFTSetting) {
            isFTSetting = true;
            isFTRation = false;
            if (TextUtils.isEmpty(permissionBean.getAccessRemovedTitle()) || TextUtils.isEmpty(permissionBean.getAccessRemovedMessage())) {
                permissionCallback.onAccessRemoved(dialogCallback, permissionBean.getPermission());
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivityReference.get());
                alertDialogBuilder.setMessage(permissionBean.getAccessRemovedMessage());
                alertDialogBuilder.setTitle(permissionBean.getAccessRemovedTitle());

                alertDialogBuilder.setPositiveButton("SETTING", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg) {
                        dialog.dismiss();
                        startSettingActivity();
                    }
                });
                alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg) {
                        dialog.dismiss();
                        permissionBean.setStatus(PermissionStatus.ACCESS_REMOVED);
                        permissionCallback.onPermission(permissionBean.getStatus(), permissionBean.getPermission());
                    }
                });

                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.show();
            }
        } else {
            permissionBean.setStatus(PermissionStatus.ACCESS_REMOVED);
            permissionCallback.onPermission(permissionBean.getStatus(), permissionBean.getPermission());
        }
    }

    private void startPermissionActivity() {
        if(mActivityReference != null && mActivityReference.get() != null) {
            Intent intent = new Intent(mActivityReference.get(), RxPermissionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivityReference.get().startActivity(intent);
        } else {
            permissionBean.setStatus(PermissionStatus.ERROR);
            permissionCallback.onPermission(permissionBean.getStatus(), permissionBean.getPermission());
        }
    }

    private void startSettingActivity() {
        if(mActivityReference != null && mActivityReference.get() != null) {
            Intent intent = new Intent(mActivityReference.get(), RxSettingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivityReference.get().startActivity(intent);
        } else {
            permissionBean.setStatus(PermissionStatus.ERROR);
            permissionCallback.onPermission(permissionBean.getStatus(), permissionBean.getPermission());
        }
    }
}