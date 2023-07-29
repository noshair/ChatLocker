package com.locker.whatschatlocker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.locker.whatschatlocker.R;
import com.locker.whatschatlocker.modal.TMAppListModal;
import com.locker.whatschatlocker.utills.Constant;
import com.locker.whatschatlocker.utills.PreferenceUtils;

import java.util.List;
import java.util.Random;

public class TMAllContactAdapter extends RecyclerView.Adapter<TMAllContactAdapter.ViewHolder> {
    public List<TMAppListModal> appList;
    public AddedAdapterCallback callback;
    private Context context;

    public interface AddedAdapterCallback {
        void onItemClicked(int i, boolean z);
    }

    public TMAllContactAdapter(Context context2, List<TMAppListModal> list, AddedAdapterCallback addedAdapterCallback) {
        context = context2;
        appList = list;
        callback = addedAdapterCallback;
    }


    public void toggleSelection(boolean z) {
        if (appList != null && appList.size() > 0) {
            for (int i = 0; i < appList.size(); i++) {
                appList.get(i).setChecked(z);
            }
        }
        PreferenceUtils.putString(context, Constant.LOCK_LIST, new Gson().toJson((Object) appList));
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView icon;
        public CheckBox recycler_checkbox;
        public TextView recycler_text_view;

        public ViewHolder(View view) {
            super(view);
            recycler_text_view = (TextView) view.findViewById(R.id.name);
            icon = (TextView) view.findViewById(R.id.icon);
            recycler_checkbox = (CheckBox) view.findViewById(R.id.checkbox);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.added_chat_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        if (appList != null) {
            new Random().nextInt(254);
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setShape(GradientDrawable.OVAL);
            gradientDrawable.setColor(Color.parseColor("#" + "d9f0e8"));
            viewHolder.icon.setBackground(gradientDrawable);
            viewHolder.icon.setText(appList.get(i).getName().charAt(0) + "");
            viewHolder.recycler_checkbox.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
            viewHolder.recycler_text_view.setText(appList.get(i).getName());
            viewHolder.recycler_checkbox.setChecked(appList.get(i).isChecked());
            viewHolder.recycler_checkbox.setTag(appList.get(i));
            viewHolder.recycler_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    if (z) {
                        appList.get(i).setChecked(true);
                        callback.onItemClicked(i, true);
                        return;
                    }
                    appList.get(i).setChecked(false);
                    callback.onItemClicked(i, false);
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        if (appList == null) {
            return 0;
        }
        return appList.size();
    }
}
