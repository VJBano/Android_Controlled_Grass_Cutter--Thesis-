package com.example.robocuttercontroller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class internetConn {

    public static boolean isConnected(Context context){
        ConnectivityManager connection = (ConnectivityManager)
                context.getSystemService(context.CONNECTIVITY_SERVICE);
        if(connection!= null){
            NetworkInfo[] info = connection.getAllNetworkInfo();
            if(info!= null){
                for(int i = 0; i< info.length; i++){
                    if(info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
}
