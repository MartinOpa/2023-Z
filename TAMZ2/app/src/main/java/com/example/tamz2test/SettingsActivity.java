package com.example.tamz2test;

import android.app.AlarmManager;
import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamz2test.Data.ConfigItemAdapter;
import com.example.tamz2test.Data.Notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.Collections;

public class SettingsActivity extends AppCompatActivity {
    private static Config config;
    private TextView unitsColumnLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.config = Config.loadConfig(getApplicationContext());
        if (this.config == null) {
            this.config = new Config();
            this.config.restoreFactorySettings(getApplicationContext());
        }

        this.unitsColumnLabel = (TextView)findViewById(R.id.unitColumnLabel);
        switch(this.config.getUnits()) {
            case IMPERIAL:
                this.unitsColumnLabel.setText(getString(R.string.unitsColumnLabelImperial));
                break;
            case METRIC:
                this.unitsColumnLabel.setText(getString(R.string.unitsColumnLabelMetric));
                break;
        }

        initBackButton();
        initDropMenuButton();
        initWidgets();
    }

    @Override
    protected void onStop() {
        config.save(getApplicationContext());
        super.onStop();
    }

    private void initDropMenuButton() {
        ImageButton dropMenuButton = (ImageButton)findViewById(R.id.dropMenuSettings);
        dropMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(SettingsActivity.this, dropMenuButton);
                dropMenuButton.setImageResource(R.drawable.baseline_arrow_drop_up_24);

                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu_settings, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch(item.getTitleCondensed().toString()) {
                            case "settingsAddInterval":
                                handleAddInterval(-1);
                                break;
                            case "settingsRestoreFactory":
                                handleRestoreFactorySettings(view);
                                break;
                            case "convertUnits":
                                convertUnits(view);
                                break;
                        }

                        initWidgets();

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
    private void initBackButton() {
        ImageButton backButton = (ImageButton)findViewById(R.id.backButtonSettings);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initWidgets() {
        RadioButton metricButton = (RadioButton)findViewById(R.id.rbMetric);
        metricButton.setChecked(config.getUnits() == Unit.METRIC);
        metricButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    convertUnitsPrompt(Unit.METRIC, SettingsActivity.this);
                    config.setUnits(Unit.METRIC);
                    unitsColumnLabel.setText(getString(R.string.unitsColumnLabelMetric));
                }
            }
        });

        RadioButton imperialButton = (RadioButton)findViewById(R.id.rbImperial);
        imperialButton.setChecked(config.getUnits() == Unit.IMPERIAL);
        imperialButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    convertUnitsPrompt(Unit.IMPERIAL, SettingsActivity.this);
                    config.setUnits(Unit.IMPERIAL);
                    unitsColumnLabel.setText(getString(R.string.unitsColumnLabelImperial));
                }
            }
        });

        Switch notificationSwitch = (Switch)findViewById(R.id.notificationsSwitch);
        notificationSwitch.setChecked(config.isNotifyOn());
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                config.setNotifyOn(b);
                if (b) {
                    setNotifications();
                } else {
                    cancelNotifications();
                }
            }
        });

        reloadItemList();
    }

    private void reloadItemList() {
        RecyclerView configItemList = (RecyclerView)findViewById(R.id.configItemList);
        configItemList.setLayoutManager(new LinearLayoutManager(this));
        ConfigItemAdapter adapter = new ConfigItemAdapter(this, this.config.getIntervalConfig());

        configItemList.setAdapter(adapter);

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
                                                config.getIntervalConfig().remove(viewHolder.getAdapterPosition());
                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                break;
                                        }

                                        reloadItemList();
                                    }
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());

                                builder.setMessage(String.format(getString((R.string.settingsReallyDelete)), config.getIntervalConfig().get(viewHolder.getAdapterPosition()).getName()))
                                        .setNegativeButton(getString(R.string.no), dialogClickListener)
                                        .setPositiveButton(getString(R.string.yes), dialogClickListener)
                                        .show();

                                break;
                            case ItemTouchHelper.START:
                                handleAddInterval(viewHolder.getAdapterPosition());
                                break;
                        }
                    }
                }
        );

        helper.attachToRecyclerView(configItemList);
    }

    private void handleRestoreFactorySettings(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        config.restoreFactorySettings(view.getContext());
                        cancelNotifications();
                        setNotifications();
                        reloadItemList();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage(getString(R.string.settingsReallyRestoreFactory))
                .setNegativeButton(getString(R.string.no), dialogClickListener)
                .setPositiveButton(getString(R.string.yes), dialogClickListener)
                .show();
    }

    private void handleAddInterval(int position) {
        LayoutInflater factory = LayoutInflater.from(this);
        View addDialogView = factory.inflate(R.layout.config_recycler_view_add, null);
        AlertDialog addDialog = new AlertDialog.Builder(this).create();
        addDialog.setView(addDialogView);

        EditText name = (EditText)addDialogView.findViewById(R.id.settingsIntervalNameField);
        EditText value = (EditText)addDialogView.findViewById(R.id.settingsIntervalValueField);
        Button accept = (Button)addDialogView.findViewById(R.id.settingsIntervalAddAccept);
        NumberPicker years = (NumberPicker)addDialogView.findViewById(R.id.settingsIntervalTimeFieldValueYears);
        NumberPicker months = (NumberPicker)addDialogView.findViewById(R.id.settingsIntervalTimeFieldValueMonths);
        years.setMinValue(0);
        years.setMaxValue(15);
        months.setMinValue(0);
        months.setMaxValue(11);

        if (position > -1) {
            accept.setText(R.string.settingsIntervalEditAccept);
            name.setText(config.getIntervalConfig().get(position).getName());
            value.setText(String.valueOf(config.getIntervalConfig().get(position).getValue()));
            years.setValue(config.getIntervalConfig().get(position).getMonths()/12);
            months.setValue(config.getIntervalConfig().get(position).getMonths()%12);
        } else {
            accept.setText(R.string.settingsIntervalAddAccept);
        }

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start,
                                       int end, Spanned dest, int dstart, int dend) {

                if (dstart == 0 && source.length() > 0 && source.charAt(0) == '0') {
                    return "";
                }

                for (int i = start; i < end; i++) {
                    if (!Character.isDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };

        value.setFilters(new InputFilter[] { filter });

        addDialogView.findViewById(R.id.settingsIntervalAddCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadItemList();
                addDialog.dismiss();
            }
        });
        addDialogView.findViewById(R.id.settingsIntervalAddAccept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = name.getText().toString();
                String valueStr = value.getText().toString();
                int monthVal = months.getValue();
                int yearVal = years.getValue();

                if (nameStr.isEmpty() || valueStr.isEmpty()) {
                    addDialog.dismiss();
                    return;
                }

                if (position > -1) {
                    config.getIntervalConfig().remove(position);
                }

                config.addInterval(new ConfigItem(nameStr, Integer.parseInt(valueStr), (yearVal*12)+monthVal));
                Collections.sort(config.getIntervalConfig());
                reloadItemList();
                addDialog.dismiss();
            }
        });

        addDialog.show();
    }

    private void convertUnits(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        convertToMetric();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        convertToImperial();
                        break;
                }

                reloadItemList();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setMessage(getString(R.string.convertUnitsTo))
                .setNegativeButton(getString(R.string.unitsColumnLabelImperial), dialogClickListener)
                .setPositiveButton(getString(R.string.unitsColumnLabelMetric), dialogClickListener)
                .show();
    }

    private void convertToImperial() {
        for (ConfigItem item : config.getIntervalConfig()) {
            item.setValue((int)(item.getValue() * 0.6214));
        }

        reloadItemList();
    }

    private void convertToMetric() {
        for (ConfigItem item : config.getIntervalConfig()) {
            item.setValue((int)(item.getValue() * 1.6093));
        }

        reloadItemList();
    }

    private void convertUnitsPrompt(Unit unit, Context context) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        switch(unit) {
                            case METRIC:
                                convertToMetric();
                                break;
                            case IMPERIAL:
                                convertToImperial();
                                break;
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }

                reloadItemList();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        String unitStr = "";
        switch(unit) {
            case METRIC:
                unitStr = getString(R.string.unitsColumnLabelMetric);
                break;
            case IMPERIAL:
                unitStr = getString(R.string.unitsColumnLabelImperial);
                break;
        }

        builder.setMessage(String.format(getString(R.string.convertUnitsPrompt), unitStr))
                .setNegativeButton(getString(R.string.no), dialogClickListener)
                .setPositiveButton(getString(R.string.yes), dialogClickListener)
                .show();
    }


    public void setNotifications() {
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(SettingsActivity.this, Notifications.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, 0, intent, PendingIntent.FLAG_MUTABLE);

        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        c.set(Calendar.HOUR, 7);
        c.set(Calendar.MINUTE, 20);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void cancelNotifications() {
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(SettingsActivity.this, Notifications.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, 0, intent, PendingIntent.FLAG_MUTABLE);

        alarmManager.cancel(pendingIntent);
    }
}