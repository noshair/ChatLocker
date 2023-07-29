package com.locker.whatschatlocker.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.locker.whatschatlocker.adapter.TMAllContactAdapter;
import com.locker.whatschatlocker.databinding.AllAddedAppListActivityBinding;
import com.locker.whatschatlocker.modal.TMAppListModal;
import com.locker.whatschatlocker.utills.Constant;
import com.locker.whatschatlocker.utills.PreferenceUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TMAllContactListActivity extends AppCompatActivity implements TMAllContactAdapter.AddedAdapterCallback {

    public TMAllContactAdapter mAdapter;
    public AllAddedAppListActivityBinding mAllAddedAppListActivityBinding;
    public List<TMAppListModal> mLockList = new ArrayList();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mAllAddedAppListActivityBinding = AllAddedAppListActivityBinding.inflate(getLayoutInflater());
        setContentView((View) mAllAddedAppListActivityBinding.getRoot());
        mAllAddedAppListActivityBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mAllAddedAppListActivityBinding.recyclerViewAdded.setLayoutManager(new LinearLayoutManager(this));
        getLockList();
        mAllAddedAppListActivityBinding.removeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String string = PreferenceUtils.getString(TMAllContactListActivity.this, Constant.LOCK_LIST);
                if (!TextUtils.isEmpty(string)) {
                    mLockList = (List) new Gson().fromJson(string, new TypeToken<List<TMAppListModal>>() {}.getType());
                }
                Iterator it = mLockList.iterator();
                while (it.hasNext()) {
                    if (((TMAppListModal) it.next()).isChecked()) {
                        it.remove();
                    }
                }
                PreferenceUtils.putString(TMAllContactListActivity.this, Constant.LOCK_LIST, new Gson().toJson((Object) mLockList));
                getLockList();
            }
        });
        mAllAddedAppListActivityBinding.cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (mAdapter != null) {

                    mAdapter.toggleSelection(z);
                    mAllAddedAppListActivityBinding.checkText.setText(z ? "Deselect All" : "Select All");
                }
            }
        });
    }

    public void getLockList() {
        String string = PreferenceUtils.getString(this, Constant.LOCK_LIST);
        if (!TextUtils.isEmpty(string)) {
            mLockList = (List) new Gson().fromJson(string, new TypeToken<List<TMAppListModal>>() {
            }.getType());
        }
        if (mLockList.size() > 0) {

            mAllAddedAppListActivityBinding.noDataFound.setVisibility(View.GONE);
        } else {

            mAllAddedAppListActivityBinding.noDataFound.setVisibility(View.VISIBLE);
        }
        mAdapter = new TMAllContactAdapter(this, mLockList, this);
        mAllAddedAppListActivityBinding.recyclerViewAdded.setAdapter(mAdapter);
    }


    @Override
    public void onItemClicked(int i, boolean z) {
        String string = PreferenceUtils.getString(this, Constant.LOCK_LIST);
        if (!TextUtils.isEmpty(string)) {
            mLockList = (List) new Gson().fromJson(string, new TypeToken<List<TMAppListModal>>() {
            }.getType());
        }
        TMAppListModal appListModal = mLockList.get(i);
        appListModal.setChecked(z);
        mLockList.set(i, appListModal);
        PreferenceUtils.putString(this, Constant.LOCK_LIST, new Gson().toJson((Object) mLockList));
    }
}
