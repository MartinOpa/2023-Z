package com.example.tamz2test.Data;

import static java.lang.String.valueOf;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamz2test.ConfigItem;
import com.example.tamz2test.R;

import java.util.List;

public class AddConfigItemAdapter extends RecyclerView.Adapter<AddConfigItemViewHolder> {
    private Context context;
    List<ConfigItem> list;

    public AddConfigItemAdapter(Context context, List<ConfigItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AddConfigItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddConfigItemViewHolder(LayoutInflater.from(context).inflate(R.layout.vehicle_config_item_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddConfigItemViewHolder holder, int position) {
        int pos = position;
        holder.setConfigItemName(list.get(position).getName());

        holder.getCheckBox().setChecked(list.get(position).getSelected());
        holder.getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                list.get(pos).setSelected(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
