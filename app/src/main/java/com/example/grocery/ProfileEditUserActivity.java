package com.example.grocery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ProfileEditUserActivity extends AppCompatActivity {

    private ImageButton backBtn, gpsBtn;
    private ImageView profileTv;
    private EditText nameEt, shopeNameEt, phoneEt, deliveryFeeEt, countryEt, stateEt, cityEt, addressEt;
    private Button updateBtn;
    private SwitchCompat shopOpenSwitch;

    //permissions contants
    private static final int LOCATION_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;


    //permissions arrays
    private String[] locationPermissions;
    private String[] cameraPermissions;
    private String[] storagePermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit_user);

        backBtn = findViewById(R.id.backBtn);
        gpsBtn = findViewById(R.id.gpsBtn);
        profileTv = findViewById(R.id.profileTv);
        nameEt = findViewById(R.id.nameEt);
        shopeNameEt = findViewById(R.id.shopNameEt);
        phoneEt = findViewById(R.id.phoneEt);
        deliveryFeeEt = findViewById(R.id.deliveryFeeEt);
        countryEt = findViewById(R.id.countryEt);
        stateEt = findViewById(R.id.stateEt);
        cityEt = findViewById(R.id.cityEt);
        addressEt = findViewById(R.id.addressEt);
        updateBtn = findViewById(R.id.updateBtn);
        shopOpenSwitch = findViewById(R.id.shopOpenSwitch);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go back previous activity
                onBackPressed();
            }
        });

        gpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //detect location
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //begin update profile

            }
        });


    }
}