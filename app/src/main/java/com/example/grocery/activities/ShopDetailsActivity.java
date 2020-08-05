package com.example.grocery.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocery.Constants;
import com.example.grocery.R;
import com.example.grocery.adapters.AdapterProductUser;
import com.example.grocery.models.ModelProduct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShopDetailsActivity extends AppCompatActivity {

    //declare ui views
    private ImageView shopIv;
    private TextView shopNameTv, phoneTv, emailTv, openCloseTv, deliveryFeeTv, addressTv, filteredProductsTv;
    private ImageButton callBtn,mapBtn, cartBtn, backBtn, filterProductBtn;
    private EditText searchProductEt;
    private RecyclerView productsRv;

    private String shopUid;
    private FirebaseAuth firebaseAuth;
    private String myLatitude, myLongitude;
    private String shopName, shopEmail, shopPhone, shopAddress,shopLatitude, shopLongitude;

    private ArrayList<ModelProduct> productsList;
    private AdapterProductUser adapterProductUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);

        //init ui views
        shopIv = findViewById(R.id.shopIv);
        shopNameTv = findViewById(R.id.shopNameTv);
        phoneTv = findViewById(R.id.phoneTv);
        emailTv = findViewById(R.id.emailTv);
        openCloseTv = findViewById(R.id.openCloseTv);
        deliveryFeeTv = findViewById(R.id.deliveryFeeTv);
        addressTv = findViewById(R.id.addressTv);
        filteredProductsTv = findViewById(R.id.filteredProductsTv);
        callBtn = findViewById(R.id.callBtn);
        mapBtn = findViewById(R.id.mapBtn);
        cartBtn = findViewById(R.id.cartBtn);
        backBtn = findViewById(R.id.backBtn);
        filterProductBtn = findViewById(R.id.filterProductBtn);
        searchProductEt = findViewById(R.id.searchProductEt);
        productsRv = findViewById(R.id.productsRv);

        //get uid of the shop from intent
        shopUid = getIntent().getStringExtra("shopUid");
        firebaseAuth = firebaseAuth.getInstance();
        loadMyInfo();
        loadShopDetails();
        loadshopProducts();

        //search
        searchProductEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    adapterProductUser.getFilter().filter(s);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go back
                onBackPressed();
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cartBtn user
            }
        });

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialPhone();
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });

        filterProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShopDetailsActivity.this);
                builder.setTitle("Choose Category: ")
                        .setItems(Constants.productCategories1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get selected item
                                String selected = Constants.productCategories1[which];
                                filteredProductsTv.setText(selected);
                                if(selected.equals("All"))
                                {
                                    //load all
                                    loadshopProducts();
                                }
                                else
                                {
                                    //load filtered
                                    adapterProductUser.getFilter().filter(selected);
                                }
                            }
                        }).show();
            }
        });


    }

    private void openMap() {
        //saddr means source address
        //saddr means destination address
        String address = "https//:maps.google.com/maps?saddr=" + myLatitude + "," + myLongitude + "&daddr=" + shopLatitude + "," +shopLongitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
        startActivity(intent);
    }

    private void dialPhone() {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("Tel: " +Uri.encode(shopPhone))));
        Toast.makeText(this,""+shopPhone, Toast.LENGTH_SHORT).show();
    }

    private void loadMyInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren())
                        {
                            String name = ""+ds.child("name").getValue();
                            String email = ""+ds.child("email").getValue();
                            String phone = ""+ds.child("phone").getValue();
                            String profileImage = ""+ds.child("profileImage").getValue();
                            String accountType = ""+ds.child("accountType").getValue();
                            String city = ""+ds.child("city").getValue();
                            myLatitude = ""+ds.child("latitude").getValue();
                            myLongitude = ""+ds.child("longitude").getValue();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void loadShopDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(shopUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get shop data
                String name = ""+snapshot.child("name").getValue();
                shopName = ""+snapshot.child("shopName").getValue();
                shopEmail = ""+snapshot.child("email").getValue();
                shopPhone = ""+snapshot.child("phone").getValue();
                shopAddress = ""+snapshot.child("address").getValue();
                shopLatitude = ""+snapshot.child("shopLatitude").getValue();
                shopLongitude = ""+snapshot.child("shopLongitude").getValue();
                String deliveryFee = ""+snapshot.child("deliveryFee").getValue();
                String profileImage = ""+snapshot.child("profileImage").getValue();
                String shopOpen = ""+snapshot.child("shopOpen").getValue();

                //set data
                shopNameTv.setText(shopName);
                emailTv.setText(shopEmail);
                deliveryFeeTv.setText("delivery fee $" + deliveryFee);
                addressTv.setText(shopAddress);
                shopNameTv.setText(shopName);
                phoneTv.setText(shopPhone);
                if(shopOpen.equals("true"))
                {
                    openCloseTv.setText("open");
                }
                else
                {
                    openCloseTv.setText("closed");
                }

                try
                {
                    Picasso.get().load(profileImage).into(shopIv);
                }
                catch (Exception e)
                {

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadshopProducts() {
        //init list
        productsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(shopUid).child("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //clear list before adding items
                        productsList.clear();
                        for (DataSnapshot ds: snapshot.getChildren())
                        {
                            ModelProduct modelProduct = ds.getValue(ModelProduct.class);
                            productsList.add(modelProduct);
                        }
                        //setup adapter
                        adapterProductUser = new AdapterProductUser(ShopDetailsActivity.this, productsList);
                        //set adapter
                        productsRv.setAdapter(adapterProductUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}