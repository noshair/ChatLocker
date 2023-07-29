package com.locker.whatschatlocker.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.locker.whatschatlocker.activity.TMCreatePasswordActivity;
import com.locker.whatschatlocker.activity.TMMainTabActivity;
import com.locker.whatschatlocker.modal.TMAppListModal;
import com.locker.whatschatlocker.utills.Constant;
import com.locker.whatschatlocker.utills.PreferenceUtils;
import java.util.ArrayList;
import java.util.List;

public class TMAccessibilityService extends AccessibilityService {
    private static final String TAG = "WhatsappAccessibilityService";
    private static final int Timeout = 300000;
    private String mCurrentPackage;
    private List<TMAppListModal> mLockList = new ArrayList();
    private String mTargetPackage;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "Service Connected");
        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
        accessibilityServiceInfo.eventTypes = -1;
        accessibilityServiceInfo.packageNames = new String[]{"com.whatsapp", "com.google.android.inputmethod.pinyin", "com.android.systemui", "com.iflytek.inputmethod", "com.google.android.packageinstaller"};
        accessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        accessibilityServiceInfo.flags = 1;
        accessibilityServiceInfo.notificationTimeout = 100;
        setServiceInfo(accessibilityServiceInfo);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        try {
            if (accessibilityEvent.getPackageName().toString().equals("com.whatsapp")) {
                AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
                if ((rootInActiveWindow == null || !rootInActiveWindow.getClassName().equals("android.widget.FrameLayout") || rootInActiveWindow.getChild(2) == null || !rootInActiveWindow.getChild(2).getClassName().equals("android.widget.ImageButton") || (!rootInActiveWindow.getChild(2).getContentDescription().equals("New group call") && !rootInActiveWindow.getChild(2).getContentDescription().equals("Video call"))) && !rootInActiveWindow.getChild(2).getContentDescription().equals("More options") && !rootInActiveWindow.getChild(2).getContentDescription().equals("View catalogue")) {
                    Log.e("1111111111111","1111111111");
                    PreferenceUtils.setUnlock(this, "0");

                    if (!rootInActiveWindow.getClassName().equals("android.widget.FrameLayout") || rootInActiveWindow.getChild(2) == null || !rootInActiveWindow.getChild(2).getClassName().equals("android.widget.TextView") || !rootInActiveWindow.getChild(2).getContentDescription().equals("Search")) {
                        Log.e("222222222222","222222222222222222222222");
                        return;
                    }
                    Log.e("333333333333","333333333333333");

                    if (!PreferenceUtils.getFullLock(this).equals("1") || !PreferenceUtils.getFullUnLock(this).equals("0")) {
                        Log.e("44444444444444","4444444444444");
                        return;

                    }
                    Log.e("555555555555","5555555555555555");
                    checkLockStatusFull();
                    return;
                }
                String charSequence = rootInActiveWindow.getChild(1).getChild(0).getText().toString();
                if (PreferenceUtils.getNewAdd(this).equals("1")) {
                    Log.e("66666666666","666666666666");
                    if (!checkInList(charSequence)) {
                        Log.e("77777777777777777","77777777777777");
                        PreferenceUtils.addNew(this, "0");
                        TMAppListModal appListModal = new TMAppListModal();
                        appListModal.setName(charSequence);
                        appListModal.setSelected(true);
                        mLockList.add(appListModal);
                        PreferenceUtils.putString(this, Constant.LOCK_LIST, new Gson().toJson(mLockList));
                        performGlobalAction(1);
                        launchComponent();
                        return;
                    }

                    Log.e("888888888888","88888888888888888");
                    PreferenceUtils.addNew(this, "0");
                    performGlobalAction(1);
                    launchComponent();
                    return;
                } else if (!checkInList(charSequence) || !PreferenceUtils.getUnLock(this).equals("0")) {
                    Log.e("999999999999999999","999999999999999999");
                    return;
                } else {
                    Log.e("100101010010101","10101010101010");
                    checkLockStatus();
                    return;
                }
            }
            Log.e("aaaaaaaaaaaa","aaaaaaaaaaaaaaaaa");
            PreferenceUtils.setFullUnlock(this, "0");
            Intent intent = new Intent();
            intent.setFlags(276856832);
            startActivity(intent);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void checkLockStatus() {
        Intent intent = new Intent();
        intent.setFlags(276856832);
        intent.setClass(this, TMCreatePasswordActivity.class);
        intent.putExtra("from", "lock");
        startActivity(intent);
    }







    private void checkLockStatusFull() {
        Intent intent = new Intent();
        intent.setFlags(276856832);
        intent.setClass(this, TMCreatePasswordActivity.class);
        intent.putExtra("from", "power");
        startActivity(intent);
    }

    public boolean checkInList(String str) {
        String string = PreferenceUtils.getString(this, Constant.LOCK_LIST);
        if (!TextUtils.isEmpty(string)) {
            mLockList = (List) new Gson().fromJson(string, new TypeToken<List<TMAppListModal>>() {
            }.getType());
        }
        if (!mLockList.isEmpty()) {
            for (int i = 0; i < mLockList.size(); i++) {
                if (TextUtils.equals(mLockList.get(i).getName(), str) && mLockList.get(i).isSelected()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onInterrupt() {
        System.out.println("Whatsapp Accessibility Service onInterrupt");
    }


    private void launchComponent() {
        Intent intent = new Intent();
        intent.setFlags(276824064);
        intent.setClass(this, TMMainTabActivity.class);
        startActivity(intent);
    }
}

