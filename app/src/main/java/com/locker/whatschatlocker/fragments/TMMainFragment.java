package com.locker.whatschatlocker.fragments;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.locker.whatschatlocker.BuildConfig;
import com.locker.whatschatlocker.R;
import com.locker.whatschatlocker.activity.PrivacyActivity;
import com.locker.whatschatlocker.activity.TMAboutUsActivity;
import com.locker.whatschatlocker.activity.TMAllContactListActivity;

import com.locker.whatschatlocker.activity.TMTransparentActivity;
import com.locker.whatschatlocker.adapter.TMAppListAdapter;
import com.locker.whatschatlocker.databinding.ActivityMainBinding;
import com.locker.whatschatlocker.modal.TMAppListModal;
import com.locker.whatschatlocker.services.TMAccessibilityService;
import com.locker.whatschatlocker.utills.Constant;
import com.locker.whatschatlocker.utills.PreferenceUtils;
import com.locker.whatschatlocker.utills.TMAdsUtils;

import java.util.ArrayList;
import java.util.List;

public class TMMainFragment extends Fragment implements TMAppListAdapter.AdapterCallback {
    long backPressedTime;
    private TMAppListAdapter mAdapter;
    List<TMAppListModal> mLockList = new ArrayList();


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    ActivityMainBinding mainBinding;



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



   @Override
    public void onResume() {
        super.onResume();
        getLockList();
        if (!isAccessibilityOn(getActivity(), TMAccessibilityService.class)) {
            checkAccesibleDialog();
        }
    }

    private void checkAccesibleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.accessibility_dialog_layout, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        Window window = alertDialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        CardView confirmButton =dialogView.findViewById(R.id.confirmButton);
        CardView declineButton =dialogView.findViewById(R.id.declineButton);
        CheckBox checkBox=dialogView.findViewById(R.id.checkBox);
        TextView checkBoxWarning=dialogView.findViewById(R.id.checkBoxWarning);
        TextView privacy=dialogView.findViewById(R.id.privacy);

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), PrivacyActivity.class);
                startActivity(intent);
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Toast.makeText(getContext(), "Chat lock function will not work without this permission.", Toast.LENGTH_SHORT).show();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkBox.isChecked()) {
                    checkBoxWarning.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Agree To Use This Accessibility Permission", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    alertDialog.dismiss();
                    checkBoxWarning.setVisibility(View.GONE);
                    startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
                    startActivity(new Intent(getActivity(), TMTransparentActivity.class));

                }
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
      /*  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle((CharSequence) getResources().getString(R.string.app_name));
        builder.setMessage((CharSequence) "Please enable Accessibility Service. Its required to lock chat, group and full app lock.");
        builder.setCancelable(false);
        builder.setPositiveButton((CharSequence) "Enable", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
           @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
                startActivity(new Intent(getActivity(), TMTransparentActivity.class));
            }
        });
        builder.create().show();*/
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mainBinding = ActivityMainBinding.inflate(inflater, container, false);


        return mainBinding.getRoot();
    }
    @Override
    public void onViewCreated(View view, Bundle bundle) {

        mainBinding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContactList();
            }
        });
        mainBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mainBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mainBinding.recyclerView.setNestedScrollingEnabled(false);
        mainBinding.manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TMAllContactListActivity.class));
            }
        });

        mainBinding.setting.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(getContext());
                new SupportMenuInflater(getContext()).inflate(R.menu.main_menu, menuBuilder);
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem menuItem) {
                        // your "setOnMenuItemClickListener" code goes here
                        switch (menuItem.getItemId()) {
                            case R.id.nav_restart:
                                startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
                                startActivity(new Intent(getContext(), TMTransparentActivity.class));
                                return  true;
                            case R.id.nav_support:
                                Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("mailto:support@gmail.com"));
                                intent.putExtra("android.intent.extra.SUBJECT", getResources().getString(R.string.app_name));
                                intent.putExtra("android.intent.extra.TEXT", getResources().getString(R.string.my_suggeston));
                                startActivity(Intent.createChooser(intent, "Chooser Title"));
                                return true;
                            case R.id.nav_about:
                                startActivity(new Intent(getContext(), TMAboutUsActivity.class));
                                return true;
                            case R.id.nav_share:
                                try {
                                    Intent intent1 = new Intent("android.intent.action.SEND");
                                    intent1.setType("text/plain");
                                    intent1.putExtra("android.intent.extra.SUBJECT", getResources().getString(R.string.app_name));
                                    intent1.putExtra("android.intent.extra.TEXT", "\nLet me recommend you this application\n\n" + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n");
                                    startActivity(Intent.createChooser(intent1, "choose one"));
                                } catch (Exception exception) {
                                }
                                return true;
                            case R.id.nav_privacy:
                                try {
                                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://templatemela.com/privacy")));
                                } catch (Exception exception) {
                                }
                                return true;
                        }
                        return false;
                    }

                    @Override
                    public void onMenuModeChange(MenuBuilder menu) {
                    }
                });
                MenuPopupHelper menuHelper = new MenuPopupHelper(getContext(), menuBuilder, v);
                menuHelper.setForceShowIcon(true); // show icons!!!!!!!!
                menuHelper.show();//showing popup menu

            }
        });
    }


    public  void addContactList(){
        PreferenceUtils.addNew(getContext(), "1");
        if(!appInstalledOrNot("com.whatsapp")){
            Toast.makeText(getContext(), "Whatsapp not installed", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.HomeActivity"));
            intent.addFlags(268435456);
            intent.addFlags(67108864);
           // intent.setFlags(276824064);
            TMAdsUtils.initAd(getContext());
            TMAdsUtils.loadInterAd(getContext());
            TMAdsUtils.showInterAd(getContext(),intent);
           // getActivity().finishAndRemoveTask();
        }

    }

     boolean appInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
   /* @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
       mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
       setContentView((View) mainBinding.getRoot());

        ((BottomNavigationView) findViewById(R.id.navigation)).setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }*/

    private void getLockList() {
        String string = PreferenceUtils.getString(getActivity(), Constant.LOCK_LIST);
        if (!TextUtils.isEmpty(string)) {
            mLockList = (List) new Gson().fromJson(string, new TypeToken<List<TMAppListModal>>() {
            }.getType());
        }
        if (mLockList.size() > 0) {
            mainBinding.noDataFound.setVisibility(View.GONE);
        } else {
            mainBinding.noDataFound.setVisibility(View.VISIBLE);
        }
        mAdapter = new TMAppListAdapter(getActivity(), mLockList, this);
        mainBinding.recyclerView.setAdapter(mAdapter);
    }


    public void launchComponent() {
        PreferenceUtils.addNew(getActivity(), "1");
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.HomeActivity"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getActivity().startActivity(intent);
        getActivity().finishAndRemoveTask();
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 10) {
            int i2 = 0;
            while (i2 < iArr.length && iArr[i2] != -1) {
                if (!isAccessibilityOn(getContext(), TMAccessibilityService.class)) {
                    startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
                    startActivity(new Intent(getContext(), TMTransparentActivity.class));
                }
                i2++;
            }
        }
    }

    private boolean isAccessibilityOn(Context context, Class<? extends AccessibilityService> cls) {
        int i;
        String string;
        String str = context.getPackageName() + "/" + cls.getCanonicalName();
        try {
            i = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(), "accessibility_enabled");
        } catch (Settings.SettingNotFoundException unused) {
            i = 0;
        }
        TextUtils.SimpleStringSplitter simpleStringSplitter = new TextUtils.SimpleStringSplitter(':');
        if (i == 1 && (string = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), "enabled_accessibility_services")) != null) {
            simpleStringSplitter.setString(string);
            while (simpleStringSplitter.hasNext()) {
                if (simpleStringSplitter.next().equalsIgnoreCase(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isAppInstalled(Activity activity, String str) {
        try {
            activity.getPackageManager().getPackageInfo(str, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public void onItemClicked(int i, String str) {
        String string = PreferenceUtils.getString(getActivity(), Constant.LOCK_LIST);
        if (!TextUtils.isEmpty(string)) {
            mLockList = (List) new Gson().fromJson(string, new TypeToken<List<TMAppListModal>>() {
            }.getType());
        }
        if (str.equals("1")) {
            TMAppListModal appListModal = new TMAppListModal();
            appListModal.setSelected(true);
            appListModal.setName(mLockList.get(i).getName());
            mLockList.set(i, appListModal);
        } else {
            TMAppListModal appListModal2 = new TMAppListModal();
            appListModal2.setSelected(false);
            appListModal2.setName(mLockList.get(i).getName());
            mLockList.set(i, appListModal2);
        }
        mAdapter.notifyDataSetChanged();
        PreferenceUtils.putString(getContext(), Constant.LOCK_LIST, new Gson().toJson((Object) mLockList));


    }


   /* @Override
    public void finish() {
        if (Build.VERSION.SDK_INT >= 21) {
            super.finishAndRemoveTask();
        } else {
            super.finish();
        }
    }*/
}
