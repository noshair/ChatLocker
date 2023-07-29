package com.locker.whatschatlocker.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.locker.whatschatlocker.databinding.AboutUsActivityBinding;

public class TMAboutUsActivity extends AppCompatActivity {
    AboutUsActivityBinding mBindings;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mBindings = AboutUsActivityBinding.inflate(getLayoutInflater());
        setContentView((View) mBindings.getRoot());
        mBindings.tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://templatemela.com")));
            }
        });
        mBindings.version.setText("App Version 1.2.1");
    }
}
