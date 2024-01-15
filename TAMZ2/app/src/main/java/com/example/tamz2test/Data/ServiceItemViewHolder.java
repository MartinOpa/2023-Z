package com.example.tamz2test.Data;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamz2test.R;

public class ServiceItemViewHolder extends RecyclerView.ViewHolder {
    private TextView serviceItemVehicle;
    private TextView serviceItemMileage;
    private TextView serviceItemUnits;
    private TextView serviceItemDate;
    private TextView serviceItemIntervals;
    private ImageView serviceItemDueSoon;

    public ServiceItemViewHolder(@NonNull View itemView) {
        super(itemView);

        this.serviceItemVehicle = (TextView)itemView.findViewById(R.id.serviceItemVehicle);
        this.serviceItemMileage = (TextView)itemView.findViewById(R.id.serviceItemDueMileage);
        this.serviceItemUnits = (TextView)itemView.findViewById(R.id.serviceItemUnits);
        this.serviceItemDate = (TextView)itemView.findViewById(R.id.serviceItemDueDate);
        this.serviceItemIntervals = (TextView)itemView.findViewById(R.id.serviceItemInterval);
        this.serviceItemDueSoon = (ImageView)itemView.findViewById(R.id.serviceItemDueSoon);
    }

    public void setServiceItemVehicle(String serviceItemVehicle) {
        this.serviceItemVehicle.setText(serviceItemVehicle);
    }

    public void setServiceItemMileage(String serviceItemMileage) {
        this.serviceItemMileage.setText(serviceItemMileage);
    }

    public void setServiceItemUnits(String serviceItemUnits) {
        this.serviceItemUnits.setText(serviceItemUnits);
    }

    public void setServiceItemDate(String serviceItemDate) {
        this.serviceItemDate.setText(serviceItemDate);
    }

    public void setServiceItemIntervals(String serviceItemIntervals) {
        this.serviceItemIntervals.setText(serviceItemIntervals);
    }

    public void setServiceItemDueSoon(boolean dueSoon) {
        if (dueSoon) {
            this.serviceItemDueSoon.setImageResource(R.drawable.baseline_error_outline_24);
        } else {
            this.serviceItemDueSoon.setVisibility(View.INVISIBLE);
        }
    }
}
