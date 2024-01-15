package com.example.tamz2test;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamz2test.Data.AddConfigItemAdapter;
import com.example.tamz2test.Data.ConfigItemAdapter;
import com.example.tamz2test.Data.ServiceItemDao;
import com.example.tamz2test.Data.VehicleDao;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {
    private String vin;
    private String make;
    private String model;
    private int year;
    private String plate;
    private int mileage;
    private String customNote;
    private byte[] vehiclePic;
    private int serviceItemTime;

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String getCustomNote() {
        return customNote;
    }

    public void setCustomNote(String customNote) {
        this.customNote = customNote;
    }

    public byte[] getVehiclePic() {
        return vehiclePic;
    }

    public Bitmap getVehiclePicBitmap() {
        return BitmapFactory.decodeByteArray(getVehiclePic(), 0, getVehiclePic().length);
    }

    public View.OnClickListener getVehicleItemAddServiceItemAction() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAddServiceItem(view);
            }
        };
    }

    public void setVehiclePic(byte[] vehiclePic) {
        this.vehiclePic = vehiclePic;
    }

    public Vehicle(String vin, String make, String model, int year, String plate, int mileage, String customNote, byte[] vehiclePic) {
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.year = year;
        this.plate = plate;
        this.mileage = mileage;
        this.customNote = customNote;
        this.vehiclePic = vehiclePic;
    }

    private void handleAddServiceItem(View view) {
        Config config = Config.loadConfig(view.getContext());

        LayoutInflater factory = LayoutInflater.from(view.getContext());
        View addDialogView = factory.inflate(R.layout.service_item_add_from_detail, null);
        AlertDialog addDialog = new AlertDialog.Builder(view.getContext()).create();
        addDialog.setView(addDialogView);

        TextView serviceItemAddVinTitle = (TextView)addDialogView.findViewById(R.id.serviceItemAddVinTitle);
        EditText serviceItemAddMileageInput = (EditText)addDialogView.findViewById(R.id.serviceItemAddMileageInput);
        TextInputEditText serviceItemAddCustomNoteInput = (TextInputEditText)addDialogView.findViewById(R.id.serviceItemAddCustomNoteInput);
        ImageButton serviceItemAddDateButton = (ImageButton)addDialogView.findViewById(R.id.serviceItemAddDateButton);
        TextView serviceItemAddDateDisplay = (TextView)addDialogView.findViewById(R.id.serviceItemAddDateDisplay);

        RecyclerView serviceItemAddIntervalPicker = (RecyclerView)addDialogView.findViewById(R.id.serviceItemAddIntervalPicker);
        serviceItemAddIntervalPicker.setLayoutManager(new LinearLayoutManager(addDialogView.getContext()));
        AddConfigItemAdapter adapter = new AddConfigItemAdapter(addDialogView.getContext(), config.getIntervalConfig());
        serviceItemAddIntervalPicker.setAdapter(adapter);

        serviceItemAddVinTitle.setText(String.format("%d " + make + " " + model, year));

        InputFilter filter = Utils.getDigitFilter();
        serviceItemAddMileageInput.setFilters(new InputFilter[] { filter });
        serviceItemAddMileageInput.setText(Integer.toString(this.mileage));

        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        serviceItemAddDateDisplay.setText(String.format("%d/%d/%d", year, month, day));
        serviceItemTime = Utils.componentTimeToTimestamp(year, month, day);

        addDialogView.findViewById(R.id.settingsIntervalAddCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.dismiss();
            }
        });
        addDialogView.findViewById(R.id.settingsIntervalAddAccept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> intervalList = new ArrayList<String>();

                for (ConfigItem item : config.getIntervalConfig()) {
                    if (item.getSelected()) {
                        intervalList.add(item.getName());
                    }
                }

                if (Integer.parseInt(serviceItemAddMileageInput.getText().toString()) > mileage) {
                    VehicleDao vehicleDao = new VehicleDao(v.getContext());
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    setMileage(Integer.parseInt(serviceItemAddMileageInput.getText().toString()));
                                    vehicleDao.updateVehicleItem(getSelf());
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage(v.getContext().getString(R.string.updateMileage))
                            .setNegativeButton(v.getContext().getString(R.string.no), dialogClickListener)
                            .setPositiveButton(v.getContext().getString(R.string.yes), dialogClickListener)
                            .show();
                }

                ServiceItemDao serviceItemDao = new ServiceItemDao(view.getContext());
                serviceItemDao.addServiceItem( new ServiceItem(
                        0,
                        vin,
                        intervalList,
                        Integer.parseInt(serviceItemAddMileageInput.getText().toString()),
                        serviceItemTime,
                        serviceItemAddCustomNoteInput.getText().toString()
                    )
                );

                addDialog.dismiss();
            }
        });

        serviceItemAddDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        serviceItemTime = Utils.componentTimeToTimestamp(selectedYear, selectedMonth, selectedDay);
                        serviceItemAddDateDisplay.setText(String.format("%d/%d/%d", selectedYear, selectedMonth + 1, selectedDay));
                    }
                };

                DatePickerDialog datePicker = new DatePickerDialog(
                    view.getContext(),
                    R.style.Base_Theme_Tamz2test, datePickerListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                );
                datePicker.show();
            }
        });

        addDialog.show();
    }

    private Vehicle getSelf() {
        return this;
    }
}
