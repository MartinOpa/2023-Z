package com.example.tamz2test.Data;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamz2test.R;

public class ServiceItemViewHolderHistory extends RecyclerView.ViewHolder {
    private TextView serviceItemVehicle;
    private TextView serviceItemMileage;
    private TextView serviceItemUnits;
    private TextView serviceItemDate;
    private TextView serviceItemIntervals;

    public ServiceItemViewHolderHistory(@NonNull View itemView) {
        super(itemView);

        this.serviceItemVehicle = (TextView)itemView.findViewById(R.id.serviceItemVehicle);
        this.serviceItemMileage = (TextView)itemView.findViewById(R.id.serviceItemMileage);
        this.serviceItemUnits = (TextView)itemView.findViewById(R.id.serviceItemUnits);
        this.serviceItemDate = (TextView)itemView.findViewById(R.id.serviceItemDate);
        this.serviceItemIntervals = (TextView)itemView.findViewById(R.id.serviceItemIntervals);
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
}
