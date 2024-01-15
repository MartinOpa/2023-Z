package com.example.tamz2test.Data;

import static java.lang.String.valueOf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamz2test.R;
import com.example.tamz2test.Vehicle;

import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleViewHolder> {
    private Context context;
    private List<Vehicle> list;

    public VehicleAdapter(Context context, List<Vehicle> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VehicleViewHolder(LayoutInflater.from(context).inflate(R.layout.vehicle_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        holder.setVehicleItemMake(list.get(position).getMake());
        holder.setVehicleItemModel(list.get(position).getModel());
        holder.setVehicleItemYear(list.get(position).getYear());
        holder.setVehicleItemPlate(list.get(position).getPlate());
        holder.setVehicleItemPicture(list.get(position).getVehiclePicBitmap());
        holder.setVehicleItemAddServiceItemAction(list.get(position).getVehicleItemAddServiceItemAction());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
