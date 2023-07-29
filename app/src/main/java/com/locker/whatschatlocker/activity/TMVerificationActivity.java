package com.locker.whatschatlocker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.beautycoder.pflockscreen.security.PFResult;
import com.beautycoder.pflockscreen.security.PFSecurityManager;
import com.beautycoder.pflockscreen.security.callbacks.PFPinCodeHelperCallback;
import com.locker.whatschatlocker.databinding.VerificationActivityBinding;
import com.locker.whatschatlocker.utills.PreferenceUtils;

public class TMVerificationActivity extends AppCompatActivity {
    VerificationActivityBinding mBinding;

    public String mFrom;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mBinding = VerificationActivityBinding.inflate(getLayoutInflater());
        setContentView((View) mBinding.getRoot());
        if (getIntent() != null) {
            mFrom = getIntent().getStringExtra("from");
        }
        mBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mBinding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFrom.equals("New")) {
                    if (!mBinding.email.getText().toString().isEmpty()) {
                        PreferenceUtils.setPassCode(TMVerificationActivity.this, mBinding.email.getText().toString());
                        startActivity(new Intent(TMVerificationActivity.this, TMMainTabActivity.class));
                        finish();
                    }
                } else if (mBinding.email.getText().toString().equals(PreferenceUtils.getPassCode(TMVerificationActivity.this))) {
                    PFSecurityManager.getInstance().getPinCodeHelper().delete(new PFPinCodeHelperCallback<Boolean>() {
                        @Override
                        public void onResult(PFResult<Boolean> pFResult) {
                            if (pFResult != null) {
                                Intent intent = new Intent(TMVerificationActivity.this, TMCreatePasswordActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                } else {
                    Toast.makeText(TMVerificationActivity.this, "Email did not matched", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
