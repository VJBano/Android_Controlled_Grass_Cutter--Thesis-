package com.example.robocuttercontroller;

import static com.example.robocuttercontroller.MainActivity.getIp;
import static com.example.robocuttercontroller.MainActivity.randomStringId;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class ControlMain extends AppCompatActivity {
    DatabaseReference mDatabase;

    DatabaseReference stop;
    private String getCurrentTime;
    Activity activity;
    private String getVal;

    networkChangeListener netListener = new networkChangeListener();





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_main);

        //pag kuha sa current time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat gettime = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        //g assign sa laing variable ang nakuhang current date ug time
        getCurrentTime = gettime.format(calendar.getTime());

        final DatabaseReference getSqft = FirebaseDatabase.getInstance().getReference();
            getSqft.child("ControlTrack").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    getVal = String.valueOf(snapshot.child("SquareFT").getValue());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        activity = this;
        WebView webview = findViewById(R.id.web);

//        progDia = ProgressDialog.show(activity,"Loading","Please Wait...", true);
//        progDia.setCancelable(true);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);

        webview.setWebViewClient(new WebViewClient(){

            public boolean shouldOverrideUrlLoading(WebView view, String Url){
//                progDia.show();
                view.loadUrl(Url);

                return true;
            }
            public void onPageFinished(WebView view, final String Url){

//                progDia.dismiss();
            }
        });
 
        webview.loadUrl("http://"+getIp);

        mDatabase = FirebaseDatabase.getInstance().getReference("User/UserStatus");
        stop = FirebaseDatabase.getInstance().getReference("ControlTrack/Exit");

        //firebaseDatabase = FirebaseDatabase.getInstance();

    }


    protected void onDestroy(){

        super.onDestroy();

        stop.setValue("off");

        HashMap<String, Object>operatorData = new HashMap<>();

         operatorData.put("TimeOut",getCurrentTime);
        operatorData.put("SquareFeet",getVal);

        FirebaseFirestore.getInstance().collection("Operator")
                .document(randomStringId)
                .update(operatorData);

    }

    protected void onStart(){
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netListener,filter);

        super.onStart();

    }

    protected void onStop(){
        unregisterReceiver(netListener);
        mDatabase.setValue("Offline");
        super.onStop();
    }
}
