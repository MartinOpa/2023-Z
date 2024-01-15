package com.example.tamz2test.Data;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamz2test.R;

public class VehicleViewHolder extends RecyclerView.ViewHolder {
    private TextView vehicleItemMake;
    private TextView vehicleItemModel;
    private TextView vehicleItemYear;
    private TextView vehicleItemPlate;
    private ImageView vehicleItemPicture;
    private ImageButton vehicleItemAddServiceItem;
    public VehicleViewHolder(@NonNull View itemView) {
        super(itemView);
        vehicleItemMake = itemView.findViewById(R.id.vehicleItemMake);
        vehicleItemModel = itemView.findViewById(R.id.vehicleItemModel);
        vehicleItemYear = itemView.findViewById(R.id.vehicleItemYear);
        vehicleItemPlate = itemView.findViewById(R.id.vehicleItemPlate);
        vehicleItemPicture = itemView.findViewById(R.id.vehicleItemPicture);
        vehicleItemAddServiceItem = itemView.findViewById(R.id.vehicleItemAddServiceItem);
    }

    public void setVehicleItemMake(String vehicleItemMake) {
        this.vehicleItemMake.setText(vehicleItemMake);
    }

    public void setVehicleItemModel(String vehicleItemModel) {
        this.vehicleItemModel.setText(vehicleItemModel);
    }

    public void setVehicleItemYear(int vehicleItemYear) {
        this.vehicleItemYear.setText(Integer.toString(vehicleItemYear));
    }

    public void setVehicleItemPlate(String vehicleItemPlate) {
        this.vehicleItemPlate.setText(vehicleItemPlate);
    }

    public void setVehicleItemPicture(Bitmap vehicleItemPicture) {
        this.vehicleItemPicture.setImageBitmap(vehicleItemPicture);
    }

    public void setVehicleItemAddServiceItemAction(View.OnClickListener onClickListener) {
        this.vehicleItemAddServiceItem.setOnClickListener(onClickListener);
    }
}