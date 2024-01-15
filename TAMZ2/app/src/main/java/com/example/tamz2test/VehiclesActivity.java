package com.example.tamz2test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamz2test.Data.VehicleAdapter;
import com.example.tamz2test.Data.VehicleDao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class VehiclesActivity extends AppCompatActivity {
    public static final int VEHICLE_UPDATE = 1001;
    private List<Vehicle> vehicleList;
    private VehicleDao vehicleDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);
        this.vehicleDao = new VehicleDao(getApplicationContext());

        initBackButton();
        initAddButton();
        initWidgets();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VEHICLE_UPDATE) {
            reloadItemList();
        }
    }

    private void initAddButton() {
        ImageButton addButtonVehicles = (ImageButton)findViewById(R.id.addButtonVehicles);
        addButtonVehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VehiclesActivity.this, VehicleDetailActivity.class);
                intent.putExtra("VIN", "new");
                startActivityForResult(intent, VEHICLE_UPDATE);
                reloadItemList();
            }
        });
    }

    private void initBackButton() {
        ImageButton backButton = (ImageButton)findViewById(R.id.backButtonVehicles);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initWidgets() {
        reloadItemList();
    }

    private void loadVehicles() {
        this.vehicleList = this.vehicleDao.getVehicleItems();
    }

    private void reloadItemList() {
        RecyclerView vehiclesItemList = (RecyclerView)findViewById(R.id.vehiclesItemList);
        vehiclesItemList.setLayoutManager(new LinearLayoutManager(this));
        loadVehicles();
        VehicleAdapter adapter = new VehicleAdapter(this, this.vehicleList);

        vehiclesItemList.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.Callback() {
                    @Override
                    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                        return makeMovementFlags(
                                0,
                                ItemTouchHelper.END | ItemTouchHelper.START
                        );
                    }

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        switch(direction) {
                            case ItemTouchHelper.END:
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                vehicleDao.deleteVehicleItem(vehicleList.get(viewHolder.getAdapterPosition()).getVin());
                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                break;
                                        }

                                        reloadItemList();
                                    }
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());

                                builder.setMessage(
                                        String.format(
                                                getString((R.string.vehiclesReallyDelete)),
                                                vehicleList.get(viewHolder.getAdapterPosition()).getMake(),
                                                vehicleList.get(viewHolder.getAdapterPosition()).getModel(),
                                                vehicleList.get(viewHolder.getAdapterPosition()).getPlate()
                                            )
                                        )
                                        .setNegativeButton(getString(R.string.no), dialogClickListener)
                                        .setPositiveButton(getString(R.string.yes), dialogClickListener)
                                        .show();

                                break;
                            case ItemTouchHelper.START:
                                Intent intent = new Intent(VehiclesActivity.this, VehicleDetailActivity.class);
                                intent.putExtra("VIN", vehicleList.get(viewHolder.getAdapterPosition()).getVin());
                                startActivityForResult(intent, VEHICLE_UPDATE);
                                break;
                        }
                    }
                }
        );

        helper.attachToRecyclerView(vehiclesItemList);
    }
}
