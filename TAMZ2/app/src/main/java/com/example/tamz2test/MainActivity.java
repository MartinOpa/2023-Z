package com.example.tamz2test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamz2test.Data.AddConfigItemAdapter;
import com.example.tamz2test.Data.Assets;
import com.example.tamz2test.Data.ConfigItemAdapter;
import com.example.tamz2test.Data.Notifications;
import com.example.tamz2test.Data.ServiceItemAdapter;
import com.example.tamz2test.Data.ServiceItemAdapterHistory;
import com.example.tamz2test.Data.ServiceItemDao;
import com.example.tamz2test.Data.VehicleDao;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private int serviceItemTime;
    private TabLayout tabs;
    private RecyclerView serviceItemsList;
    private List<ServiceItem> upcomingServiceItemList;
    private List<ServiceItem> historyServiceItemList;
    private ServiceItemAdapterHistory serviceItemAdapterHistory;
    private ServiceItemAdapter serviceItemAdapterUpcoming;
    private SearchView serviceItemFilter;
    private ItemTouchHelper serviceHistoryItemTouchHelper;

    private void createNotificationsChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Utils.notificationChannelName, "servicenotify", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Carfaux notification channel");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDropMenuButton();
        initCameraButton();
        initTabs();
        createNotificationsChannel();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri selectedFileURI = data.getData();
                if (selectedFileURI != null && !Objects.requireNonNull(selectedFileURI.getPath()).isEmpty()) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedFileURI);

//                        TessBaseAPI tess = new TessBaseAPI();
//                        Assets.extractAssets(getApplicationContext());
//                        String dataPath = Assets.getTessDataPath(getApplicationContext());
//                        if (!tess.init(dataPath, "eng")) {
//                            tess.recycle();
//                        }
//                        tess.setVariable("tessedit_char_whitelist", "0123456789");
//                        tess.setVariable("classify_bln_numeric_mode", "1");
//                        tess.setImage(bitmap);
//                        String text = tess.getUTF8Text();
//                        tess.recycle();

                        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
                        Task<Text> result =
                                recognizer.process(InputImage.fromBitmap(bitmap, 0))
                                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                                            @Override
                                            public void onSuccess(Text visionText) {
                                                int res = Utils.getMileageFromPicture(visionText);

                                                if (res > 0) {
                                                    handleAddServiceItem(-1, res);
                                                } else {
                                                    Utils.showErrorDialog(getString(R.string.mainMenuMileageApiError), MainActivity.this);
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Utils.showErrorDialog(getString(R.string.mainMenuMileageApiError), MainActivity.this);
                                            }
                                        });
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {

                }
            }
        }
    }

    private void pickImage() {
        ImagePicker.with(this)
                .crop()
                .start();
    }

    private void initCameraButton() {
        ImageButton cameraButton = (ImageButton)findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });
    }

    private void initDropMenuButton() {
        ImageButton dropMenuButton = (ImageButton)findViewById(R.id.dropMenu);
        dropMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this, dropMenuButton);

                dropMenuButton.setImageResource(R.drawable.baseline_arrow_drop_up_24);

                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch(item.getTitleCondensed().toString()) {
                            case "menuSettingsActivity":
                                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                                break;
                            case "menuVehiclesActivity":
                                startActivity(new Intent(MainActivity.this, VehiclesActivity.class));
                                break;
                            case "menuAddServiceItem":
                                handleAddServiceItem(-1, 0);
                                break;
                        }

                        return true;
                    }
                });

                popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu popupMenu) {
                        dropMenuButton.setImageResource(R.drawable.baseline_arrow_drop_down_24);
                    }
                });
                popup.show();
            }
        });
    }

    private void handleAddServiceItem(int serviceItemId, int mileage) {
        Config config = Config.loadConfig(getApplicationContext());
        VehicleDao vehicleDao = new VehicleDao(getApplicationContext());
        List<Vehicle> vehicleList = vehicleDao.getVehicleItems();

        if (vehicleList.size() == 0) {
            Toast.makeText(this, getString(R.string.noVehicles), Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> choices = new ArrayList<String>();
        Map<String, String> carVinVals = new HashMap<String, String>();
        for (Vehicle veh : vehicleList) {
            carVinVals.put(String.format("%d %s %s", veh.getYear(), veh.getMake(), veh.getModel()), veh.getVin());
            choices.add(String.format("%d %s %s", veh.getYear(), veh.getMake(), veh.getModel()));
        }

        LayoutInflater factory = LayoutInflater.from(MainActivity.this);
        View addDialogView = factory.inflate(R.layout.service_item_add, null);
        AlertDialog addDialog = new AlertDialog.Builder(MainActivity.this).create();
        addDialog.setView(addDialogView);

        EditText serviceItemAddMileageInput = (EditText)addDialogView.findViewById(R.id.serviceItemAddMileageInput);
        TextInputEditText serviceItemAddCustomNoteInput = (TextInputEditText)addDialogView.findViewById(R.id.serviceItemAddCustomNoteInput);
        ImageButton serviceItemAddDateButton = (ImageButton)addDialogView.findViewById(R.id.serviceItemAddDateButton);
        TextView serviceItemAddDateDisplay = (TextView)addDialogView.findViewById(R.id.serviceItemAddDateDisplay);

        Spinner serviceItemAddVinInput = (Spinner)addDialogView.findViewById(R.id.serviceItemAddVinInput);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.vehicle_spinner, choices);
        arrayAdapter.setDropDownViewResource(R.layout.vehicle_spinner);

        serviceItemAddVinInput.setAdapter(arrayAdapter);

        InputFilter filter = Utils.getDigitFilter();
        serviceItemAddMileageInput.setFilters(new InputFilter[] { filter });

        if (serviceItemId > -1) {
            ServiceItemDao serviceItemDao = new ServiceItemDao(MainActivity.this);
            ServiceItem serviceItem = serviceItemDao.getServiceItem(serviceItemId);

            serviceItemAddMileageInput.setText(Integer.toString(serviceItem.getMileage()));
            serviceItemAddDateDisplay.setText(Utils.timestampToString(serviceItem.getDate()));
            serviceItemTime = serviceItem.getDate();

            Vehicle serviceVehicle = vehicleDao.getVehicle(serviceItem.getVehicleVin());
            String serviceVehicleString = String.format("%d %s %s", serviceVehicle.getYear(), serviceVehicle.getMake(), serviceVehicle.getModel());

            for (int i = 0; i < choices.size(); i++) {
                if (choices.get(i).equals(serviceVehicleString)) {
                    serviceItemAddVinInput.setSelection(i);
                    break;
                }
            }

            for (ConfigItem item : config.getIntervalConfig()) {
                if (serviceItem.getIntervalTypes().contains(item.getName())) {
                    item.setSelected(true);
                }
            }

            Button addButton = (Button)addDialogView.findViewById(R.id.settingsIntervalAddAccept);
            addButton.setText(R.string.settingsIntervalEditAccept);
        } else {
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            serviceItemAddDateDisplay.setText(String.format("%d/%d/%d", year, month, day));
            serviceItemTime = Utils.componentTimeToTimestamp(year, month - 1, day);
        }

        if (mileage > 0) {
            serviceItemAddMileageInput.setText(Integer.toString(mileage));
        } else {
            String carVin = carVinVals.get(serviceItemAddVinInput.getSelectedItem().toString());
            serviceItemAddMileageInput.setText(Integer.toString(vehicleDao.getVehicle(carVin).getMileage()));

            serviceItemAddVinInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    serviceItemAddMileageInput.setText(Integer.toString(vehicleDao.getVehicle(carVinVals.get(serviceItemAddVinInput.getSelectedItem().toString())).getMileage()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        RecyclerView serviceItemAddIntervalPicker = (RecyclerView)addDialogView.findViewById(R.id.serviceItemAddIntervalPicker);
        serviceItemAddIntervalPicker.setLayoutManager(new LinearLayoutManager(addDialogView.getContext()));
        AddConfigItemAdapter adapter = new AddConfigItemAdapter(addDialogView.getContext(), config.getIntervalConfig());
        serviceItemAddIntervalPicker.setAdapter(adapter);

        addDialogView.findViewById(R.id.settingsIntervalAddCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tabs.getSelectedTabPosition() == 1) {
                    reloadHistoryItemList();
                }
                addDialog.dismiss();
            }
        });
        addDialogView.findViewById(R.id.settingsIntervalAddAccept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String carVin = carVinVals.get(serviceItemAddVinInput.getSelectedItem().toString());
                List<String> intervalList = new ArrayList<String>();

                for (ConfigItem item : config.getIntervalConfig()) {
                    if (item.getSelected()) {
                        intervalList.add(item.getName());
                    }
                }


                Vehicle vehicleUpdate = vehicleDao.getVehicle(carVin);
                if (Integer.parseInt(serviceItemAddMileageInput.getText().toString()) > vehicleUpdate.getMileage()) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    vehicleUpdate.setMileage(Integer.parseInt(serviceItemAddMileageInput.getText().toString()));
                                    vehicleDao.updateVehicleItem(vehicleUpdate);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                            reloadHistoryItemList();
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage(getString(R.string.updateMileage))
                            .setNegativeButton(getString(R.string.no), dialogClickListener)
                            .setPositiveButton(getString(R.string.yes), dialogClickListener)
                            .show();
                }

                ServiceItemDao serviceItemDao = new ServiceItemDao(getApplicationContext());
                if (serviceItemId > -1) {
                    serviceItemDao.updateServiceItem( new ServiceItem(
                                    serviceItemId,
                                    carVin,
                                    intervalList,
                                    Integer.parseInt(serviceItemAddMileageInput.getText().toString()),
                                    serviceItemTime,
                                    serviceItemAddCustomNoteInput.getText().toString()
                            )
                    );
                } else {
                    serviceItemDao.addServiceItem( new ServiceItem(
                                    0,
                                    carVin,
                                    intervalList,
                                    Integer.parseInt(serviceItemAddMileageInput.getText().toString()),
                                    serviceItemTime,
                                    serviceItemAddCustomNoteInput.getText().toString()
                            )
                    );
                }

                if (tabs.getSelectedTabPosition() == 1) {
                    reloadHistoryItemList();
                }

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
                        R.style.Base_Theme_Tamz2test,
                        datePickerListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                );
                datePicker.show();
            }
        });

        addDialog.show();
    }

    private void initTabs() {
        this.tabs = (TabLayout)findViewById(R.id.tabs);
        this.serviceItemsList = (RecyclerView)findViewById(R.id.serviceItemsList);
        this.upcomingServiceItemList = new ArrayList<ServiceItem>();
        this.historyServiceItemList = new ArrayList<ServiceItem>();
        reloadUpcomingItemList();
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals(getString(R.string.upcomingTab))) {
                    reloadUpcomingItemList();
                } else if (tab.getText().toString().equals(getString(R.string.historyTab))) {
                    reloadHistoryItemList();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals(getString(R.string.upcomingTab))) {
                    reloadUpcomingItemList();
                } else if (tab.getText().toString().equals(getString(R.string.historyTab))) {
                    reloadHistoryItemList();
                }
            }
        });
    }

    private void reloadUpcomingItemList() {
        ServiceItemDao serviceItemDao = new ServiceItemDao(MainActivity.this);
        this.upcomingServiceItemList = serviceItemDao.getServicePlan(MainActivity.this);
        this.upcomingServiceItemList.sort(new Comparator<ServiceItem>() {
            @Override
            public int compare(ServiceItem item1, ServiceItem item2) {
                if (item1.getDate() > item2.getDate()) {
                    return 1;
                } else if (item1.getDate() < item2.getDate()) {
                    return -1;
                }
                return 0;
            }
        });

        this.serviceItemFilter = (SearchView)findViewById(R.id.serviceItemFilter);
        this.serviceItemFilter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                serviceItemAdapterUpcoming.filter(s);
                return false;
            }
        });

        this.serviceItemsList.setLayoutManager(new LinearLayoutManager(this));
        this.serviceItemAdapterUpcoming = new ServiceItemAdapter(this, upcomingServiceItemList);
        this.serviceItemsList.setAdapter(this.serviceItemAdapterUpcoming);

        if (this.serviceHistoryItemTouchHelper != null) {
            this.serviceHistoryItemTouchHelper.attachToRecyclerView(null);
        }
    }

    private void reloadHistoryItemList() {
        ServiceItemDao serviceItemDao = new ServiceItemDao(MainActivity.this);
        historyServiceItemList = serviceItemDao.getServiceItems("");
        this.serviceItemFilter = (SearchView)findViewById(R.id.serviceItemFilter);
        serviceItemFilter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                serviceItemAdapterHistory.filter(s);
                return false;
            }
        });

        this.serviceItemsList.setLayoutManager(new LinearLayoutManager(this));
        this.serviceItemAdapterHistory = new ServiceItemAdapterHistory(this, historyServiceItemList);
        this.serviceItemsList.setAdapter(this.serviceItemAdapterHistory);

        if (this.serviceHistoryItemTouchHelper == null) {
            this.serviceHistoryItemTouchHelper = new ItemTouchHelper(
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
                                                    serviceItemDao.deleteServiceItem(serviceItemAdapterHistory.getItem(viewHolder.getAdapterPosition()).getId());
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    break;
                                            }
                                            reloadHistoryItemList();
                                        }
                                    };

                                    AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());

                                    builder.setMessage(getString(R.string.reallyDelete))
                                            .setNegativeButton(getString(R.string.no), dialogClickListener)
                                            .setPositiveButton(getString(R.string.yes), dialogClickListener)
                                            .show();

                                    break;
                                case ItemTouchHelper.START:
                                    handleAddServiceItem(serviceItemAdapterHistory.getItem(viewHolder.getAdapterPosition()).getId(), 0);
                                    break;
                            }
                        }
                    }
            );
        }

        this.serviceHistoryItemTouchHelper.attachToRecyclerView(this.serviceItemsList);
    }
}
