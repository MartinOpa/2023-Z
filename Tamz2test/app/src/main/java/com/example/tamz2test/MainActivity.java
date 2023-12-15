package com.example.tamz2test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDropMenuButton();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
}