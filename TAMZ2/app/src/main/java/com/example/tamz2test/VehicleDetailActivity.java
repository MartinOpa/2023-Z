package com.example.tamz2test;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tamz2test.Data.VehicleDao;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class VehicleDetailActivity extends AppCompatActivity {
    private Vehicle vehicle;
    private VehicleDao vehicleDao;
    private ImageView vehicleDetailCarPic;
    private Bitmap bitmap;
    private EditText vehicleDetailMakeInput;
    private EditText vehicleDetailModelInput;
    private EditText vehicleDetailYearInput;
    private EditText vehicleDetailVinInput;
    private EditText vehicleDetailMileageInput;
    private EditText vehicleDetailPlateInput;
    private TextInputEditText vehicleDetailCustomNoteInput;
    private TextView mainTitleVehicleDetail;

    private void initWidgets() {
        vehicleDetailCarPic = (ImageView)findViewById(R.id.vehicleDetailCarPic);
        vehicleDetailMakeInput = (EditText)findViewById(R.id.vehicleDetailMakeInput);
        vehicleDetailModelInput = (EditText)findViewById(R.id.vehicleDetailModelInput);
        vehicleDetailYearInput = (EditText)findViewById(R.id.vehicleDetailYearInput);
        vehicleDetailVinInput = (EditText)findViewById(R.id.vehicleDetailVinInput);
        vehicleDetailMileageInput = (EditText)findViewById(R.id.vehicleDetailMileageInput);
        vehicleDetailPlateInput = (EditText)findViewById(R.id.vehicleDetailPlateInput);
        vehicleDetailCustomNoteInput = (TextInputEditText)findViewById(R.id.vehicleDetailCustomNoteInput);
        mainTitleVehicleDetail = (TextView)findViewById(R.id.mainTitleVehicleDetail);

        InputFilter filter = Utils.getDigitFilter();
        vehicleDetailMileageInput.setFilters(new InputFilter[] { filter });

        vehicleDetailCarPic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showImage();
            }
        });

        TextWatcher mainTitleWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mainTitleVehicleDetail.setText(
                                vehicleDetailMakeInput.getText().toString() +
                                " " +
                                vehicleDetailModelInput.getText().toString()
                );
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        vehicleDetailMakeInput.addTextChangedListener(mainTitleWatcher);
        vehicleDetailModelInput.addTextChangedListener(mainTitleWatcher);

        ImageButton vehicleDetailPicCamera = (ImageButton)findViewById(R.id.vehicleDetailPicCamera);
        vehicleDetailPicCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeImage();
            }
        });

        ImageButton vehicleDetailPicGallery = (ImageButton)findViewById(R.id.vehicleDetailPicGallery);
        vehicleDetailPicGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_detail);

        initBackButton();
        initSaveButton();
        initWidgets();

        this.vehicleDao = new VehicleDao(getApplicationContext());
        loadVehicle(getIntent().getStringExtra("VIN"));
    }

    private void initBackButton() {
        ImageButton backButton = (ImageButton)findViewById(R.id.backButtonVehicleDetail);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initSaveButton() {
        ImageButton backButton = (ImageButton)findViewById(R.id.saveButtonVehicleDetail);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveVehicle();
            }
        });
    }

    //tu init

    private void saveVehicle() {
        if (vehicleDetailVinInput.getText().toString().equals("")) {
            Utils.showErrorDialog(getString(R.string.vehicleDetailEmptyVinError), VehicleDetailActivity.this);
            return;
        }

        if (this.vehicle == null) {
            this.vehicle = new Vehicle(
                    vehicleDetailVinInput.getText().toString(),
                    vehicleDetailMakeInput.getText().toString(),
                    vehicleDetailModelInput.getText().toString(),
                    Integer.parseInt(vehicleDetailYearInput.getText().toString().equals("") ? "0" : vehicleDetailYearInput.getText().toString()),
                    vehicleDetailPlateInput.getText().toString(),
                    Integer.parseInt(vehicleDetailMileageInput.getText().toString().equals("") ? "0" : vehicleDetailMileageInput.getText().toString()),
                    vehicleDetailCustomNoteInput.getText().toString(),
                    bitmapToByte(this.bitmap)
            );
            this.bitmap.recycle();
            this.vehicleDao.addVehicleItem(this.vehicle);
        } else {
            if (vehicleDetailVinInput.getText().toString().equals(this.vehicle.getVin())) {
                this.vehicle.setMake(vehicleDetailMakeInput.getText().toString());
                this.vehicle.setModel(vehicleDetailModelInput.getText().toString());
                this.vehicle.setYear(Integer.parseInt(vehicleDetailYearInput.getText().toString().equals("") ? "0" : vehicleDetailYearInput.getText().toString()));
                this.vehicle.setPlate(vehicleDetailPlateInput.getText().toString());
                this.vehicle.setMileage(Integer.parseInt(vehicleDetailMileageInput.getText().toString().equals("") ? "0" : vehicleDetailMileageInput.getText().toString()));
                this.vehicle.setCustomNote(vehicleDetailCustomNoteInput.getText().toString());

                this.vehicle.setVehiclePic(bitmapToByte(this.bitmap));
                this.bitmap.recycle();
                this.vehicleDao.updateVehicleItem(this.vehicle);
            } else {
                Utils.showErrorDialog(getString(R.string.vehicleDetailEditVinError), VehicleDetailActivity.this);
                vehicleDetailVinInput.setText(this.vehicle.getVin());
                return;
            }
        }

        finish();
    }

    private byte[] bitmapToByte(Bitmap bm) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void loadVehicle(String vinArg) {
        this.vehicle = vehicleDao.getVehicle(vinArg);

        if (this.vehicle != null) {
            this.bitmap = this.vehicle.getVehiclePicBitmap();
            this.vehicleDetailCarPic.setImageBitmap(this.bitmap);
            this.vehicleDetailMakeInput.setText(vehicle.getMake());
            this.vehicleDetailModelInput.setText(vehicle.getModel());
            this.vehicleDetailYearInput.setText(Integer.toString(vehicle.getYear()));
            this.vehicleDetailVinInput.setText(vehicle.getVin());
            this.vehicleDetailMileageInput.setText(Integer.toString(vehicle.getMileage()));
            this.vehicleDetailPlateInput.setText(vehicle.getPlate());
            this.vehicleDetailCustomNoteInput.setText(vehicle.getCustomNote());
            this.mainTitleVehicleDetail.setText(vehicle.getMake() + " " + vehicle.getModel());
        } else {
            setDefaultPicture();
        }
    }

    private void setDefaultPicture() {
        Drawable drawable = getResources().getDrawable(R.drawable.baseline_directions_car_24);
        Canvas canvas = new Canvas();
        Bitmap bm = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bm);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        this.bitmap = bm;
        vehicleDetailCarPic.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri selectedFileURI = data.getData();
                if (selectedFileURI != null && !Objects.requireNonNull(selectedFileURI.getPath()).isEmpty()) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedFileURI);
                        vehicleDetailCarPic.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private void pickImage() {
        ImagePicker.with(this)
                .galleryOnly()
                .crop(1, 1)
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start();
    }

    private void takeImage() {
        ImagePicker.with(this)
                .cameraOnly()
                .crop(1, 1)
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start();
    }

    private void showImage() {
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });

        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }
}