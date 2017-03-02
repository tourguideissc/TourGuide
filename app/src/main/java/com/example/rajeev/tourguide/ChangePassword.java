package com.example.rajeev.tourguide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener{

    Intent getUserIntent,intent;
    String userName;
    EditText currentPassword , newPassword , confirmPassword;
    Button changePasswordButton;
    //Variables for SharedPreferences
    SharedPreferences sharedPreferences;
    String USERLOGINSTATUS="UserLoginStatus";
    String USERNAME="username";
    String FIRSTNAME="firstname";
    String LASTNAME="lastname";
    String PASSWORD="password";
    String MOBILE="mobile";
    String EMAILID="emailid";
    String STATUS="status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getUserIntent = getIntent();
        userName = getUserIntent.getStringExtra("username");
        getByID();
        setListeners();
    }

    private void setListeners() {
        changePasswordButton.setOnClickListener(this);
    }

    public void getByID()
    {
      changePasswordButton = (Button) findViewById(R.id.change_password_change_password);
      currentPassword = (EditText) findViewById(R.id.current_password_change_password);
      newPassword = (EditText) findViewById(R.id.new_password_change_password);
      confirmPassword = (EditText) findViewById(R.id.confirm_password_change_password);
    }

    @Override
    public void onClick(View v)
    {
        if(currentPassword.getText().toString().equals("") && newPassword.getText().toString().equals("")
                && confirmPassword.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please enter Current password and New password",Toast.LENGTH_LONG).show();
        }
        else if(!newPassword.getText().toString().equals(confirmPassword.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"New password and Confirm password is not matching \n please enter matching password",Toast.LENGTH_LONG).show();
        }
        else
          {
            NetworkChecker networkChecker = new NetworkChecker(ChangePassword.this);
            if (networkChecker.checkNetwork()) {
                new ChangePasswordAsync(ChangePassword.this).execute(userName, currentPassword.getText().toString(), newPassword.getText().toString());
            } else {
                //Toast.makeText(getApplicationContext(),"Network Not available \n Please Activate your Network",Toast.LENGTH_LONG).show();
                networkChecker.createDialog();
            }
        }
    }



    private void setSharedPreferencesForUser(String username , String password)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME , username);
        Toast.makeText(getBaseContext(),username,Toast.LENGTH_LONG).show();
        editor.putString(PASSWORD , password);
        editor.putString(STATUS,"LoggedIn");
        editor.commit();
    }



    //AsyncTask to validate whether user is valid user or not

    class ChangePasswordAsync extends AsyncTask<String , Void ,String>
    {
        ProgressDialog pdLoading;
        String link;
        String dataToBeSend;
        String result,usernameString,oldPasswordString,newPasswordString;
        Context context;

        ChangePasswordAsync(Context context)
        {
            this.context = context;
            pdLoading = new ProgressDialog(context);
        }
        @Override
        protected void onPreExecute() {
            pdLoading.setMessage("\tPlease wait while Loading.");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }


        @Override
        protected String doInBackground(String... params) {
            usernameString = params[0];
            oldPasswordString = params[1];
            newPasswordString = params[2];
            try {
                dataToBeSend = "?username=" + URLEncoder.encode(usernameString, "UTF-8");
                dataToBeSend += "&oldpassword=" + URLEncoder.encode(oldPasswordString,"UTF-8");
                dataToBeSend += "&newpassword=" + URLEncoder.encode(newPasswordString,"UTF-8");
                link = "http://tourguideissc.esy.es/changepassword.php" + dataToBeSend;
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                result = bufferedReader.readLine();
                return result;
            }
            catch (Exception e)
            {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            String jasonResult =result;
            if(jasonResult != null)
            {
                try {
                    JSONObject jsonObject = new JSONObject(jasonResult);
                    String loginResult = jsonObject.getString("result");

                    if(loginResult.equals("SUCCESS"))
                    {
                        Toast.makeText(getApplicationContext(),"Password Changed",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ChangePassword.this,UserMainScreen.class);
                        sharedPreferences = getSharedPreferences(USERLOGINSTATUS, Context.MODE_PRIVATE);
                        setSharedPreferencesForUser(usernameString,newPasswordString);
                        startActivity(intent);


                    }
                    else if(loginResult.equals("FAILED"))
                    {
                        Toast.makeText(getApplicationContext(),"Failed to change password \n please try again",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Could not connect to database \n Please try again",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Unable to get Data from server",Toast.LENGTH_LONG).show();
            }
            pdLoading.dismiss();
        }


    }

}
