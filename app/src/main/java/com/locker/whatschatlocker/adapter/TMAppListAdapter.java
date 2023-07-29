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
import com.locker.whatschatlocker.R;
import com.locker.whatschatlocker.modal.TMAppListModal;

import java.util.List;
import java.util.Random;

public class TMAppListAdapter extends RecyclerView.Adapter<TMAppListAdapter.ViewHolder> {
    public List<TMAppListModal> appList;
    public AdapterCallback callback;
    private Context context;

    public interface AdapterCallback {
        void onItemClicked(int i, String str);
    }

    public TMAppListAdapter(Context context2, List<TMAppListModal> list, AdapterCallback adapterCallback) {
        context = context2;
        appList = list;
        callback = adapterCallback;
    }
    public void toggleSelection(boolean z) {

        if (appList != null && appList.size() > 0) {
            for (int i = 0; i < appList.size(); i++) {
                appList.get(i).setSelected(z);
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView icon;
        public CheckBox recycler_checkbox;
        public TextView recycler_text_view;
        public TextView status;

        public ViewHolder(View view) {
            super(view);
            recycler_text_view = (TextView) view.findViewById(R.id.name);
            status = (TextView) view.findViewById(R.id.status);
            icon = (TextView) view.findViewById(R.id.icon);
            recycler_checkbox = (CheckBox) view.findViewById(R.id.checkbox);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_group_list_item, viewGroup, false));
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
            viewHolder.recycler_checkbox.setChecked(appList.get(i).isSelected());
            viewHolder.recycler_checkbox.setTag(appList.get(i));
            if (viewHolder.recycler_checkbox.isChecked()) {
                viewHolder.status.setText("Locked");
                viewHolder.status.setTextColor(context.getResources().getColor(R.color.whatsapp_green));
            } else {
                viewHolder.status.setText("UnLocked");
                viewHolder.status.setTextColor(context.getResources().getColor(R.color.red));
            }
            viewHolder.recycler_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    if (z) {
                        appList.get(i).setSelected(true);
                        callback.onItemClicked(i, "1");
                        return;
                    }
                    appList.get(i).setSelected(false);
                    callback.onItemClicked(i, "0");
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
