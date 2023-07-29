package com.locker.whatschatlocker.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.viewpager.widget.ViewPager;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iammert.library.readablebottombar.ReadableBottomBar;
import com.locker.whatschatlocker.R;
import com.locker.whatschatlocker.adapter.TMTabLayoutAdapter;
import com.locker.whatschatlocker.utills.PreferenceUtils;
import com.locker.whatschatlocker.utills.TMAdsUtils;

import java.util.Locale;

public class TMMainTabActivity extends AppCompatActivity {

    ViewPager view_pager;
    ReadableBottomBar readableBottomBar;
    long backPressedTime;
    public  static TMMainTabActivity mainTabActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        mainTabActivity=this;
        view_pager=findViewById(R.id.view_pager);
        readableBottomBar=findViewById(R.id.bottom_bar);
        readableBottomBar.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
            @Override
            public void onItemSelected(int i) {
                view_pager.setCurrentItem(i);
            }
        });
        view_pager.setAdapter(new TMTabLayoutAdapter(this, getSupportFragmentManager(), 2));
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                readableBottomBar.selectItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        checkPermission();
    }

    @Override
    public void onBackPressed() {
        if (PreferenceUtils.getRating(TMMainTabActivity.this) == null) {
            showMyRating();
        } else if (PreferenceUtils.getRating(TMMainTabActivity.this) == null || !PreferenceUtils.getRating(this).equals("3")) {
            if (backPressedTime + 1500 > System.currentTimeMillis()) {
                // super.onBackPressed();
            } else {

                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Confirmation");
                alert.setMessage("Are you sure you want to exit?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        finish();
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();

            }
            backPressedTime = System.currentTimeMillis();
        } else {
            showMyRating();
        }
    }

    private void showMyRating() {
        View inflate = LayoutInflater.from(TMMainTabActivity.this).inflate(R.layout.rating_dialog, (ViewGroup) null);
        AlertDialog.Builder builder = new AlertDialog.Builder(TMMainTabActivity.this);
        builder.setView(inflate);
        final AppCompatRatingBar appCompatRatingBar = (AppCompatRatingBar) inflate.findViewById(R.id.rating);
        final AlertDialog create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        create.show();
        ((AppCompatButton) inflate.findViewById(R.id.give_rating)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (appCompatRatingBar.getRating() == 0.0f) {
                    Toast.makeText(TMMainTabActivity.this, "Please select the stars", Toast.LENGTH_SHORT).show();
                } else if (appCompatRatingBar.getRating() <= 3.0f) {
                    PreferenceUtils.setRating(TMMainTabActivity.this, "1");
                    Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("mailto:suppor@gmail.com"));
                    intent.putExtra("android.intent.extra.SUBJECT", "My Suggestion");
                    intent.putExtra("android.intent.extra.TEXT", getResources().getString(R.string.my_suggeston));
                    startActivity(Intent.createChooser(intent, "Chooser Title"));
                    create.dismiss();
                } else if (appCompatRatingBar.getRating() > 3.0f) {
                    PreferenceUtils.setRating(TMMainTabActivity.this, "2");
                    create.dismiss();
                    String packageName = TMMainTabActivity.this.getPackageName();
                    try {
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + packageName)));
                    } catch (ActivityNotFoundException unused) {
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                    }
                }

            }
        });
        ((AppCompatButton) inflate.findViewById(R.id.not_now)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PreferenceUtils.setRating(TMMainTabActivity.this, "3");
                create.dismiss();
                TMMainTabActivity.this.finish();
            }
        });
    }
    public static void addContactList(){
        PreferenceUtils.addNew(mainTabActivity, "1");
        if(!appInstalledOrNot("com.whatsapp")){
            Toast.makeText(mainTabActivity, "Whatsapp not installed", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.HomeActivity"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            TMAdsUtils.initAd(mainTabActivity);
            TMAdsUtils.loadInterAd(mainTabActivity);
            TMAdsUtils.showInterAd(mainTabActivity,intent);
            mainTabActivity.finishAndRemoveTask();
        }

    }
    static boolean appInstalledOrNot(String uri) {
        PackageManager pm = mainTabActivity.getPackageManager();
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

    public  void  checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                if ("xiaomi".equals(Build.MANUFACTURER.toLowerCase(Locale.ROOT))) {
                    final Intent intent =new Intent("miui.intent.action.APP_PERM_EDITOR");
                    intent.setClassName("com.miui.securitycenter",
                            "com.miui.permcenter.permissions.PermissionsEditorActivity");
                    intent.putExtra("extra_pkgname", getPackageName());
                    new AlertDialog.Builder(this)
                            .setTitle("Please Enable the additional permissions")
                            .setMessage("You will not receive notifications while the app is in background if you disable these permissions")
                            .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(intent);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setCancelable(false)
                            .show();
                }
            }
        }
    }
}