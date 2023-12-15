package com.example.tamz2test;

import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamz2test.Data.ConfigItemAdapter;
import com.example.tamz2test.Unit;
import com.google.gson.Gson;

import android.content.DialogInterface;
import android.content.Intent;
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

import org.w3c.dom.Text;

import java.text.Format;
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

        this.unitsColumnLabel = (TextView)findViewById(R.id.unitsColumnLabel);
        switch(this.config.getUnits()) {
            case IMPERIAL:
                this.unitsColumnLabel.setText(getString(R.string.unitsColumnLabelImperial));
            case METRIC:
                this.unitsColumnLabel.setText(getString(R.string.unitsColumnLabelMetric));
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

                if (nameStr.isEmpty() || valueStr.isEmpty()) {
                    addDialog.dismiss();
                    return;
                }

                if (position > -1) {
                    config.getIntervalConfig().remove(position);
                }

                config.addInterval(new ConfigItem(nameStr, Integer.parseInt(valueStr)));
                Collections.sort(config.getIntervalConfig());
                reloadItemList();
                addDialog.dismiss();
            }
        });

        addDialog.show();
    }
}