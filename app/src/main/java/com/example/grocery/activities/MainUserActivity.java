package com.example.grocery.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocery.R;
import com.example.grocery.adapters.AdapterShop;
import com.example.grocery.models.ModelShop;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class MainUserActivity extends AppCompatActivity {

    private TextView nameTv, emailTv, phoneTv, tabShopsTv, tabOrdersTv;
    private RelativeLayout shopsRl, ordersRl;
    private ImageButton logoutBtn,editProfileBtn;
    private ImageView profileIv;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private RecyclerView shopRv;
    private ArrayList<ModelShop> shopList;
    private AdapterShop adapterShop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        nameTv = findViewById(R.id.nameTv);
        logoutBtn = findViewById(R.id.logoutBtn);
        editProfileBtn = findViewById(R.id.editProfileBtn);
        profileIv = findViewById(R.id.profileIv);
        emailTv = findViewById(R.id.emailTv);
        phoneTv = findViewById(R.id.phoneTv);
        tabShopsTv = findViewById(R.id.tabShopsTv);
        tabOrdersTv = findViewById(R.id.tabOrdersTv);
        shopsRl = findViewById(R.id.shopsRl);
        ordersRl = findViewById(R.id.ordersRl);
        shopRv = findViewById(R.id.shopRv);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait... ");
        progressDialog.setCanceledOnTouchOutside(false);
        checkUser();

        //at start show shops
        showShopsUI();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make offline
                //sign out
                //go to login activity
                makeMeOffline();

            }
        });

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open edit profile edit activity
                startActivity(new Intent(MainUserActivity.this, ProfileEditUserActivity.class));

            }
        });
        
        tabShopsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show shops
                showShopsUI();
            }
        });

        tabOrdersTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show orders
                showOrdersUI();
            }
        });
    }


    private void showShopsUI() {
        //show shops ui, hide orders ui
        shopsRl.setVisibility(View.VISIBLE);
        ordersRl.setVisibility(View.GONE);

        tabShopsTv.setTextColor(getResources().getColor(R.color.colorBlack));
        tabShopsTv.setBackgroundColor(R.drawable.shape_rect04);

        tabOrdersTv.setTextColor(getResources().getColor(R.color.colorWhite));
        tabOrdersTv.setBackgroundColor(getResources().getColor(android.R.color.transparent));

    }

    private void showOrdersUI() {
        //show Orders ui, hide Shops ui
        shopsRl.setVisibility(View.GONE);
        ordersRl.setVisibility(View.VISIBLE);

        tabShopsTv.setTextColor(getResources().getColor(R.color.colorWhite));
        tabShopsTv.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        tabOrdersTv.setTextColor(getResources().getColor(R.color.colorBlack));
        tabOrdersTv.setBackgroundColor(R.drawable.shape_rect04);
    }

    private void makeMeOffline() {
        //after loggin in, make user online
        progressDialog.setMessage("Logging out ....");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", "false");

        //update value to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //update successfully
                        firebaseAuth.signOut();
                        checkUser();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //failed updating data
                progressDialog.dismiss();
                Toast.makeText(MainUserActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null)
        {
            startActivity(new Intent(MainUserActivity.this, LoginActivity.class));
            finish();
        }
        else
        {
            loadMyInfo();
        }

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

                            //set user data
                            //nameTv.setText(name +"("+accountType+")");
                            nameTv.setText(name);
                            emailTv.setText(email);
                            phoneTv.setText(phone);

                            try {
                                Picasso.get().load(profileImage).placeholder(R.drawable.ic_person_grey).into(profileIv);
                            }
                            catch (Exception e)
                            {
                                profileIv.setImageResource(R.drawable.ic_person_grey);

                            }

                            //load only those shops that are in the city of user
                            loadShops(city);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void loadShops(final String myCity) {
        //init list
        shopList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("accountType").equalTo("seller")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //clear list before adding
                        shopList.clear();
                        for (DataSnapshot ds: snapshot.getChildren())
                        {
                            ModelShop modelShop = ds.getValue(ModelShop.class);

                            String shopCity = ""+ds.child("city").getValue();

                        //show only user city shops
                            if(shopCity.equals(myCity))
                            {
                                shopList.add(modelShop);
                            }

                            //if you want to display all shops, skip the if statement and add this
                            //shopList.add(modelShop);
                        }
                        //setup adapter
                        adapterShop = new AdapterShop(MainUserActivity.this, shopList);
                        //set adapter to recyclerView
                        shopRv.setAdapter(adapterShop);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}