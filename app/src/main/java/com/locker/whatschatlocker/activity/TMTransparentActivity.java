package com.locker.whatschatlocker.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.locker.whatschatlocker.R;
import com.locker.whatschatlocker.databinding.TransparentActivityBinding;

public class TMTransparentActivity extends AppCompatActivity {
    TransparentActivityBinding mBinding;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mBinding = TransparentActivityBinding.inflate(getLayoutInflater());
        setContentView((View) mBinding.getRoot());
        View inflate2 = LayoutInflater.from(this).inflate(R.layout.instruction_layout, (ViewGroup) null);
        final AlertDialog create = new AlertDialog.Builder(this).create();
        create.setView(inflate2);
        inflate2.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create.dismiss();
                finish();
            }
        });
        create.show();
        create.getWindow().setBackgroundDrawable((Drawable) null);
    }
}
