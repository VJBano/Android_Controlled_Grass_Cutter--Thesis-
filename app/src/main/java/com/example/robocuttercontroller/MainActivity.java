package com.example.robocuttercontroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public EditText ip_add, email, password;
    public Button login;
    ProgressBar progress;
    public static String getTime;
    public static String getDate;
    public static String getIp;
    public static String getEmail;
    public static String getPassword;
    public static String randomStringId;
    private FirebaseAuth loginAuth;
    private FirebaseFirestore db;
    private DatabaseReference mDatabase;
    private DatabaseReference rDatabase;
    private DatabaseReference idDatabase;

    networkChangeListener netListener = new networkChangeListener();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //E hide and Title bar or Action Bar
        getSupportActionBar().hide();

        //e initialize ang mga edit text ug button to variable
        ip_add = findViewById(R.id.input_ip);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        progress = findViewById(R.id.loading);

        db = FirebaseFirestore.getInstance();
        loginAuth = FirebaseAuth.getInstance();

        //pag kuha sa current time ug date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat gettime = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        mDatabase = FirebaseDatabase.getInstance().getReference("User/UserStatus");
        rDatabase = FirebaseDatabase.getInstance().getReference("User/ActiveUser");
        idDatabase = FirebaseDatabase.getInstance().getReference("User/ID");

        //pag butang ug click function sa login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);

                //pag kuha sa mga dara nga  g pang input didto sa EditText
                getIp = ip_add.getText().toString();
                getEmail = email.getText().toString();
                getPassword = password.getText().toString();

                //g assign sa laing variable ang nakuhang current date ug time
                getTime = gettime.format(calendar.getTime());
                getDate = currentDate;

                if(getIp.isEmpty() && getEmail.isEmpty() && getPassword.isEmpty()){
                    login.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                    //kung walay g input tanan
                    Toast.makeText(MainActivity.this, "Fill all Information", Toast.LENGTH_SHORT).show();
                } else if (getIp.isEmpty()){
                    login.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                    //kung wala input ang IP
                    Toast.makeText(MainActivity.this, "Input IP Address", Toast.LENGTH_SHORT).show();
                } else if(getEmail.isEmpty()){
                    login.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                    //kung walay input ang Email
                    Toast.makeText(MainActivity.this, "Input Email", Toast.LENGTH_SHORT).show();
                } else if(getPassword.isEmpty()){
                    login.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                    //kung walay input ang Password
                    Toast.makeText(MainActivity.this, "Input Password", Toast.LENGTH_SHORT).show();
                } else {
                    db.collection("OperatorAcc")
                            .whereEqualTo("Email",getEmail)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value,
                                                    @Nullable FirebaseFirestoreException e) {
                                    if (e != null) {
                                        return;
                                    }

                                    ArrayList<String> userAcc = new ArrayList<>();
                                    for (QueryDocumentSnapshot doc : value) {
                                        if (doc.get("Email") != null) {
                                            userAcc.add(doc.getString("Email"));
                                            userAcc.add(doc.getString("Password"));
                                            userAcc.add(doc.getString("Username"));
                                            userAcc.add(doc.getString("ID"));
                                        }
                                    }
                                        if(userAcc.isEmpty()){
                                            Toast.makeText(MainActivity.this, "Operator Not Registered", Toast.LENGTH_SHORT).show();
                                            login.setVisibility(View.VISIBLE);
                                            progress.setVisibility(View.GONE);
                                        } else{
                                            String fbEmail = userAcc.get(0).toString();
                                            String fbPassword = userAcc.get(1).toString();
                                            String fbUsername = userAcc.get(2).toString();
                                            String fbId = userAcc.get(3).toString();
//                                            Log.i("My Acitvity", "Email: "+fbEmail);

                                            if(getEmail.equals(fbEmail) && getPassword.equals(fbPassword)){
                                                int id;
                                                int docLen = 10;

                                                Random idRand = new Random();
                                                id = idRand.nextInt(10000);

                                                String alphaNum = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

                                                StringBuilder docBuilder = new StringBuilder();
                                                for(int i = 0; i < docLen; i++){
                                                    int index = idRand.nextInt(alphaNum.length());
                                                    char randomChar = alphaNum.charAt(index);
                                                    docBuilder.append(randomChar);
                                                }
                                                randomStringId = docBuilder.toString();

                                                HashMap<String, Object> operatorData = new HashMap<>();
                                                operatorData.put("ID",fbId);
                                                operatorData.put("Username",getEmail);
                                                operatorData.put("Date",getDate);
                                                operatorData.put("TimeIn",getTime);

                                                FirebaseFirestore.getInstance().collection("Operator")
                                                        .document(randomStringId)
                                                        .set(operatorData);
                                                mDatabase.setValue("Online");
                                                rDatabase.setValue(fbUsername);
                                                idDatabase.setValue(fbId);

                                                Toast.makeText(MainActivity.this, "Log In Successfully", Toast.LENGTH_SHORT).show();
                                                //kung tanang input field sakto
                                                Intent nextPage = new Intent(MainActivity.this, ControlMain.class);
                                                startActivity(nextPage);
                                                finish();
                                            }
                                            else if((getEmail!=fbEmail && getPassword.equals(fbPassword)) || (getEmail.equals(fbEmail) && getPassword!=fbPassword) || (getEmail!=fbEmail && getPassword!=fbPassword)){
                                                Toast.makeText(MainActivity.this, "Wrong Password or Email", Toast.LENGTH_SHORT).show();
                                                login.setVisibility(View.VISIBLE);
                                                progress.setVisibility(View.GONE);
                                            }
                                        }


                                }
                            });
                }


            }
        });

    }

    protected void onStart(){
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netListener,filter);
        super.onStart();
    }

    protected void onStop(){
        unregisterReceiver(netListener);
        super.onStop();
    }

    }




