package com.example.robocuttercontroller;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
//import static com.example.robocuttercontroller.MainActivity.str;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SecondActivity extends AppCompatActivity {
    TextView ip_view;
    EditText operator;
    EditText time;
    EditText date;
    Button start,userData;
    private  String name;
    private  String timeVar;
    private  String dateVar;
    private String getTime;
    private String getDate;
    DatabaseReference DBref;
    String Name;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ip_view = findViewById(R.id.connIndicator);
        operator = findViewById(R.id.UoperatorName);
        time = findViewById(R.id.Utime);
        date = findViewById(R.id.Udate);
        start = findViewById(R.id.update);
        userData = findViewById(R.id.viewData);


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat gettime = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());


        timeVar = gettime.format(calendar.getTime());
        dateVar = currentDate;

       time.setText(timeVar);
        date.setText(dateVar);

        //ip_view.setText("Your Connected To "+str);
        ip_view.setBackgroundColor(Color.GREEN);
        ip_view.setTextColor(Color.BLACK);
        DBref = FirebaseDatabase.getInstance().getReference().child("Operator");


        userData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent viewPage = new Intent(SecondActivity.this, DisplayOp.class);
               // startActivity(viewPage);
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = operator.getText().toString().trim();
                getTime = time.getText().toString().trim();
                getDate = date.getText().toString().trim();


                if(name.isEmpty() && getTime.isEmpty() && getDate.isEmpty()){
                    Toast.makeText(SecondActivity.this, "Please Fill All Inputs", Toast.LENGTH_LONG).show();
                } else if(name.isEmpty()){
                    Toast.makeText(SecondActivity.this, "Please Input Name", Toast.LENGTH_LONG).show();
                } else if(getTime.isEmpty()){
                    Toast.makeText(SecondActivity.this, "Please Input Time", Toast.LENGTH_LONG).show();
                } else if(getDate.isEmpty()){
                    Toast.makeText(SecondActivity.this, "Please Input Date", Toast.LENGTH_LONG).show();
                } else if(!name.isEmpty() && !getTime.isEmpty() && !getDate.isEmpty()){

                   // RoboDatabase roboDB = new RoboDatabase(SecondActivity.this);
                   // roboDB.addOperator(name,getTime,getDate);
                    Intent nextPage = new Intent(SecondActivity.this, ControlMain.class);
                    startActivity(nextPage);
                    addOperator();

                }


            }
        });


    }
    public void addOperator(){
        int id = 0;
        Random idRand = new Random();

            id = idRand.nextInt(1000);

            Operator op = new Operator(id, name, getTime, getDate);
        DBref.push().setValue(op);
    }
}
