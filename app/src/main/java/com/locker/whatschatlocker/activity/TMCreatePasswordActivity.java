package com.locker.whatschatlocker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.Observer;

import com.beautycoder.pflockscreen.PFFLockScreenConfiguration;
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment;
import com.beautycoder.pflockscreen.security.PFResult;
import com.beautycoder.pflockscreen.viewmodels.PFPinCodeViewModel;
import com.locker.whatschatlocker.R;
import com.locker.whatschatlocker.utills.PreferenceUtils;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

public class TMCreatePasswordActivity extends AppCompatActivity {
    String FROM_REDIRECT = null;
    private final PFLockScreenFragment.OnPFLockScreenCodeCreateListener mCodeCreateListener = new PFLockScreenFragment.OnPFLockScreenCodeCreateListener() {

        @Override
        public void onCodeCreated(String str) {
            Toast.makeText(TMCreatePasswordActivity.this, "Code created", Toast.LENGTH_SHORT).show();
            PreferenceUtils.saveToPref(TMCreatePasswordActivity.this, str);
            if (PreferenceUtils.getPassCode(TMCreatePasswordActivity.this) == null) {
                showDialogPassCode();
            }
        }

        @Override
        public void onNewCodeValidationFailed() {
            Toast.makeText(TMCreatePasswordActivity.this, "Code validation error", Toast.LENGTH_SHORT).show();
        }
    };
    private final PFLockScreenFragment.OnPFLockScreenLoginListener mLoginListener = new PFLockScreenFragment.OnPFLockScreenLoginListener() {
        @Override
        public void onCodeInputSuccessful() {
            Toast.makeText(TMCreatePasswordActivity.this, "Code successfull", Toast.LENGTH_SHORT).show();
            showMainFragment();
        }

        @Override
        public void onFingerprintSuccessful() {
            Toast.makeText(TMCreatePasswordActivity.this, "Fingerprint successfull", Toast.LENGTH_SHORT).show();
            showMainFragment();
        }

        @Override
        public void onPinLoginFailed() {
            Toast.makeText(TMCreatePasswordActivity.this, "Pin failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFingerprintLoginFailed() {
            Toast.makeText(TMCreatePasswordActivity.this, "Fingerprint failed", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.create_password_activity);
        showLockScreenFragment();
        if (getIntent() != null) {
            this.FROM_REDIRECT = getIntent().getStringExtra("from");
        }
    }


    public void showDialogPassCode() {
        new FancyGifDialog.Builder(this).setTitle("Login Code Recovery").setMessage("Please enter your PIN Code recovery email. In case you forgot your PIN use this email to reset your PIN Code.").setTitleTextColor(R.color.teal_700).setDescriptionTextColor(R.color.gray_800).setNegativeBtnText("Skip").setPositiveBtnBackground(R.color.blue_800).setPositiveBtnText("Okay").setNegativeBtnBackground(R.color.teal_700).setGifResource(R.drawable.gif_code).isCancellable(true).OnPositiveClicked(new FancyGifDialogListener() {
            @Override
            public void OnClick() {
                Intent intent = new Intent(TMCreatePasswordActivity.this, TMVerificationActivity.class);
                intent.putExtra("from", "New");
                startActivity(intent);
                finish();
            }
        }).OnNegativeClicked(new FancyGifDialogListener() {
            @Override
            public void OnClick() {
                startActivity(new Intent(TMCreatePasswordActivity.this, TMMainTabActivity.class));
                finish();
            }
        }).build();
    }

    private void showLockScreenFragment() {
        new PFPinCodeViewModel().isPinCodeEncryptionKeyExist().observe(this, new Observer<PFResult<Boolean>>() {
            @Override
            public void onChanged(PFResult<Boolean> pFResult) {
                if (pFResult != null) {
                    if (pFResult.getError() != null) {
                        Toast.makeText(TMCreatePasswordActivity.this, "Can not get pin code info", Toast.LENGTH_SHORT).show();
                    } else {
                        showLockScreenFragment(pFResult.getResult().booleanValue());
                    }
                }
            }
        });
    }


    public void showLockScreenFragment(boolean z) {
        PFFLockScreenConfiguration.Builder useFingerprint = new PFFLockScreenConfiguration.Builder(this).setTitle(z ? "Unlock with your pin code or fingerprint" : "Create Code").setCodeLength(4).setLeftButton("Can't remember?").setNewCodeValidation(true).setNewCodeValidationTitle("Please input code again").setUseFingerprint(true);
        PFLockScreenFragment pFLockScreenFragment = new PFLockScreenFragment();
        pFLockScreenFragment.setOnLeftButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TMCreatePasswordActivity.this, TMVerificationActivity.class);
                intent.putExtra("from", "recovery");
                startActivity(intent);
            }
        });
        useFingerprint.setMode(z ? PFFLockScreenConfiguration.MODE_AUTH : PFFLockScreenConfiguration.MODE_CREATE);
        if (z) {
            pFLockScreenFragment.setEncodedPinCode(PreferenceUtils.getCode(this));
            pFLockScreenFragment.setLoginListener(this.mLoginListener);
        }
        pFLockScreenFragment.setConfiguration(useFingerprint.build());
        pFLockScreenFragment.setCodeCreateListener(this.mCodeCreateListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_view, pFLockScreenFragment).commit();
    }


    public void showMainFragment() {

        if (FROM_REDIRECT == null || !FROM_REDIRECT.equals("lock"))
        {
            if (FROM_REDIRECT == null || !FROM_REDIRECT.equals("power")) {
                startActivity(new Intent(this, TMMainTabActivity.class));
                finish();
                return;
            }
            PreferenceUtils.setUnlock(this, "0");
            PreferenceUtils.setFullUnlock(this, "1");
            finishAndRemoveTask();
            return;
        }

        PreferenceUtils.setUnlock(this, "1");
        finishAndRemoveTask();
    }

    @Override
    public void onBackPressed() {
        if (FROM_REDIRECT == null || !FROM_REDIRECT.equals("lock")) {
            super.onBackPressed();
        }
    }
}
