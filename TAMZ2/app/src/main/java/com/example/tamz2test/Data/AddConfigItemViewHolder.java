package com.example.tamz2test.Data;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamz2test.R;

public class AddConfigItemViewHolder extends RecyclerView.ViewHolder {
    private TextView configItemName;
    private CheckBox checkBox;
    public AddConfigItemViewHolder(@NonNull View itemView) {
        super(itemView);
        configItemName = itemView.findViewById(R.id.configItemName);
        checkBox = itemView.findViewById(R.id.checkBox);
    }

    public void setConfigItemName(String configItemName) {
        this.configItemName.setText(configItemName);
    }

    public CheckBox getCheckBox() {
        return this.checkBox;
    }
}
