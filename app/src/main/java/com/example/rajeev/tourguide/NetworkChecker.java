package com.example.rajeev.tourguide;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by rajeev on 4/2/17.
 */

public class NetworkChecker {

    private ConnectivityManager connectivityManager;
    private Context context;
    NetworkChecker(Context context)
    {
      this.context =context;
    }

    public boolean checkNetwork()
    {
     connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable())
        {
            return true;
        }
        else
          return false;
    }


    public void createDialog()
    {
        //LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        //View dialogLayout = inflater.inflate(R.layout.custom_alert_dialog, null);
        AlertDialog.Builder alertBuilder =  new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        //alertBuilder.setView(dialogLayout);
        alertBuilder.setMessage("No Network available \n Please select anyone to get network");
        alertBuilder.setTitle("Activate Data");
        alertBuilder.setIcon(R.drawable.tourguidei_con);
        alertBuilder.setPositiveButton("WIFI" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
                setWifiDataEnabled(true);
            }
        });
        alertBuilder.setNegativeButton("Mobile Data" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    setMobileDataEnabled(true);
            }
        });

        AlertDialog aleartDialog = alertBuilder.create();
        aleartDialog.show();
    }



    private void setMobileDataEnabled(boolean enabled)
    {
       /* try
        {
            TelephonyManager telephonyService = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            Method setMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("setDataEnabled", boolean.class);

            if (null != setMobileDataEnabledMethod)
            {
                setMobileDataEnabledMethod.invoke(telephonyService, enabled);
            }
        }
        catch (Exception ex)
        {
            //Log.e(TAG, "Error setting mobile data state", ex);
            ex.printStackTrace();
        }*/

        Toast.makeText(context,"Not yet Implemented",Toast.LENGTH_SHORT).show();
    }


    private void setWifiDataEnabled(boolean enabled)
    {

        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(enabled);
    }
}
