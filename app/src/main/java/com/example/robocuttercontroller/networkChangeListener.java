package com.example.robocuttercontroller;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class networkChangeListener extends BroadcastReceiver {
    //function kung naa ma recieve signal
            DatabaseReference mDatabase;
    public void onReceive(Context context, Intent intent) {
        mDatabase = FirebaseDatabase.getInstance().getReference("User/UserStatus");

        if (!internetConn.isConnected(context)) {
            //kung walay internet
            AlertDialog.Builder build = new AlertDialog.Builder(context);
            View layout = LayoutInflater.from(context).inflate(R.layout.connection_error, null);
            build.setView(layout);

            mDatabase.setValue("Offline");
            Button retry = layout.findViewById(R.id.retry);

            AlertDialog dialog = build.create();
            dialog.show();
            dialog.setCancelable(false);
            dialog.getWindow().setGravity(Gravity.CENTER);

            //kung ang retry button e click
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    onReceive(context, intent);
                }
            });
        }
    }
}
