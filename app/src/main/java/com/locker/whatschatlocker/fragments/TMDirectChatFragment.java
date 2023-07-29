package com.locker.whatschatlocker.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.Fragment;


import com.locker.whatschatlocker.BuildConfig;
import com.locker.whatschatlocker.R;

import com.locker.whatschatlocker.activity.TMAboutUsActivity;
import com.locker.whatschatlocker.activity.TMTransparentActivity;
import com.locker.whatschatlocker.databinding.ActivityMainBinding;
import com.locker.whatschatlocker.databinding.DirectChatActivityBinding;
import com.locker.whatschatlocker.utills.TMAdsUtils;

import java.net.URLEncoder;

public class TMDirectChatFragment extends Fragment {
    DirectChatActivityBinding mBinding;
    public String mCountryCode;
    ImageView setting;
    TextView send;



    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        mBinding = DirectChatActivityBinding.inflate(layoutInflater, viewGroup, false);
        return  mBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCountryCode = mBinding.ccp.getSelectedCountryCode();
        TMAdsUtils.initAd(getContext());
        TMAdsUtils.loadBannerAd(getContext(),mBinding.adsView);
        mBinding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCountryCode = mBinding.ccp.getSelectedCountryCode();
                if (!mBinding.mobileNumber.getText().toString().isEmpty()) {
                    sendMessageToWhatsAppContact(mCountryCode + mBinding.mobileNumber.getText().toString(), mBinding.etemail.getText().toString());
                } else {
                    mBinding.mobileNumber.setError("Enter mobile number");
                }

            }
        });

        mBinding. setting.setOnClickListener(new View.OnClickListener() {
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

    public void sendMessageToWhatsAppContact(String str, String str2) {
        PackageManager packageManager = getActivity().getPackageManager();
        Intent intent = new Intent("android.intent.action.VIEW");
        try {
            intent.setPackage("com.whatsapp");
            intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + str + "&text=" + URLEncoder.encode(str2, "UTF-8")));
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
