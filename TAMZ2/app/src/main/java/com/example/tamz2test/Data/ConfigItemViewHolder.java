package com.example.tamz2test.Data;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamz2test.R;

public class ConfigItemViewHolder extends RecyclerView.ViewHolder {
    private TextView configItemName;
    private TextView configItemValue;
    private TextView configItemTime;
    public ConfigItemViewHolder(@NonNull View itemView) {
        super(itemView);
        configItemName = itemView.findViewById(R.id.configItemName);
        configItemValue = itemView.findViewById(R.id.configItemValue);
        configItemTime = itemView.findViewById(R.id.configItemTime);
    }

    public void setConfigItemName(String configItemName) {
        this.configItemName.setText(configItemName);
    }

    public void setConfigItemValue(String configItemValue) {
        this.configItemValue.setText(configItemValue);
    }

    public void setConfigItemTime(String configItemTime) {
        this.configItemTime.setText(configItemTime);
    }
}
