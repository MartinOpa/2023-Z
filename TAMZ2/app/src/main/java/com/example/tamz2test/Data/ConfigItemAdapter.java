package com.example.tamz2test.Data;

import static java.lang.String.valueOf;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamz2test.ConfigItem;
import com.example.tamz2test.R;

import java.util.List;

public class ConfigItemAdapter extends RecyclerView.Adapter<ConfigItemViewHolder> {
    private Context context;
    List<ConfigItem> list;

    public ConfigItemAdapter(Context context, List<ConfigItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ConfigItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConfigItemViewHolder(LayoutInflater.from(context).inflate(R.layout.config_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigItemViewHolder holder, int position) {
        holder.setConfigItemName(list.get(position).getName());
        holder.setConfigItemTime(list.get(position).getTimeIntervalStr(
                context.getString(R.string.settingsIntervalYearValue),
                context.getString(R.string.settingsIntervalMonthValue)
        ));
        holder.setConfigItemValue(valueOf(list.get(position).getValue()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
