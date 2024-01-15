package com.example.tamz2test.Data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamz2test.Config;
import com.example.tamz2test.ServiceItem;
import com.example.tamz2test.R;
import com.example.tamz2test.Utils;
import com.example.tamz2test.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ServiceItemAdapter extends RecyclerView.Adapter<ServiceItemViewHolder> {
    private Context context;
    private List<ServiceItem> list;
    private List<ServiceItem> listUI;
    private VehicleDao vehicleDao;
    private String unit;

    public ServiceItemAdapter(Context context, List<ServiceItem> list) {
        this.context = context;
        this.list = list;
        this.listUI = new ArrayList<ServiceItem>();
        this.listUI.addAll(this.list);
        this.vehicleDao = new VehicleDao(context);

        Config config = Config.loadConfig(context);
        switch(config.getUnits()) {
            case IMPERIAL:
                this.unit = context.getString(R.string.unitsColumnLabelImperial);
            case METRIC:
                this.unit = context.getString(R.string.unitsColumnLabelMetric);
        }
    }

    @NonNull
    @Override
    public ServiceItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceItemViewHolder(LayoutInflater.from(context).inflate(R.layout.service_item_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceItemViewHolder holder, int position) {
        Vehicle vehicle = vehicleDao.getVehicle(listUI.get(position).getVehicleVin());
        holder.setServiceItemVehicle(String.format("%d " + vehicle.getMake() + " " + vehicle.getModel(), vehicle.getYear()));
        holder.setServiceItemMileage(Integer.toString(listUI.get(position).getMileage()));
        holder.setServiceItemDate(Utils.timestampToString(listUI.get(position).getDate()));
        holder.setServiceItemUnits(this.unit);
        holder.setServiceItemIntervals(listUI.get(position).getStringIntervalTypesForDesc());
        holder.setServiceItemDueSoon(listUI.get(position).isDueSoon());
    }

    @Override
    public int getItemCount() {
        return listUI.size();
    }

    public void filter(String query) {
        query = query.toLowerCase(Locale.getDefault());
        listUI.clear();
        if (query.length() == 0) {
            listUI.addAll(list);
        } else {
            for (ServiceItem item : list) {
                Vehicle vehicle = vehicleDao.getVehicle(item.getVehicleVin());
                if (vehicle.getMake().toLowerCase(Locale.getDefault()).contains(query) ||
                        vehicle.getModel().toLowerCase(Locale.getDefault()).contains(query) ||
                        Integer.toString(vehicle.getYear()).contains(query) ||
                        vehicle.getVin().toLowerCase(Locale.getDefault()).contains(query) ||
                        item.getStringIntervalTypes().toLowerCase(Locale.getDefault()).contains(query) ||
                        Integer.toString(item.getMileage()).contains(query) ||
                        Utils.timestampToString(item.getDate()).contains(query)) {
                    listUI.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
