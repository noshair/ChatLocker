package com.locker.whatschatlocker.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;

import androidx.appcompat.app.AppCompatActivity;

import com.locker.whatschatlocker.R;
import com.rbddevs.splashy.Splashy;

public class TMSplashActivity extends AppCompatActivity {

    private static final long SPLASH_DISPLAY_LENGTH = 3000;

   @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.my_splash_activity);
        setSplashy();
   }

   public  void setSplashy(){

        String tempString="Whats Chat Locker";
        SpannableString spanString = new SpannableString(tempString);
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 6, tempString.length(), 0);

        new Splashy(this)
                .setLogo(R.drawable.splash_logo)
                .setAnimation(Splashy.Animation.SLIDE_IN_LEFT_BOTTOM,1500)
                .setBackgroundResource(R.color.bg_drk)
                .setTitleColor(R.color.white)
                .setProgressColor(R.color.white)
                .setTitleFontStyle("fonts/outfit_semibold.ttf")
                .setTitleSize(25)
                .setLogoWHinDp(120,120)
                .setTitle(tempString)
                .setFullScreen(false)
                .setClickToHide(true)
                .show();
        Splashy.Companion.onComplete(new Splashy.OnComplete() {
            @Override
            public void onComplete() {
                startActivity(new Intent(TMSplashActivity.this, TMCreatePasswordActivity.class));
                finish();
            }
        });

/*
        Splashy.onComplete(object : Splashy.OnComplete {
            override fun onComplete() {
                Toast.makeText(this@MainActivity, "Welcome", Toast.LENGTH_SHORT).show()
            }

        })*/
    }
}
