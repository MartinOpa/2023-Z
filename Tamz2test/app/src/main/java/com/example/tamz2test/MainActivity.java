package com.example.tamz2test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log.d("MainActivity", "Test");
        CheckBox cb = (CheckBox)findViewById(R.id.checkBox);
        //btn.setOnClickListener ...
        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), R.string.button1_click_event, Toast.LENGTH_LONG).show();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"));
                //i.setData(Uri.parse("http://google.com"));
                startActivity(i);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}