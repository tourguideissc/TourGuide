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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SignInScreen extends AppCompatActivity implements View.OnClickListener{

    EditText usernameLoginScreen,passwordLoginScreen;
    Button loginButton,createNewLoginButton;
    TextView skipTextView,forgotPasswordTextView;
    String usernameString,passwordString,RESULT="result",loginValidation;
    boolean validater;
    Intent intent;

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
        setContentView(R.layout.activity_sign_in_screen);

        findById();
        addListeners();
    }

    private void addListeners()
    {
        loginButton.setOnClickListener(this);
        forgotPasswordTextView.setOnClickListener(this);
        skipTextView.setOnClickListener(this);
        createNewLoginButton.setOnClickListener(this);
    }

    private void findById()
    {
        usernameLoginScreen = (EditText) findViewById(R.id.username_on_login_screen);
        passwordLoginScreen = (EditText) findViewById(R.id.password_on_login_screen);
        loginButton = (Button) findViewById(R.id.login);
        createNewLoginButton = (Button) findViewById(R.id.create_new_login);
        skipTextView = (TextView) findViewById(R.id.skip_login);
        forgotPasswordTextView = (TextView) findViewById(R.id.forgot_password);
        sharedPreferences = getSharedPreferences(USERLOGINSTATUS, Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.login : validater = validateData(usernameLoginScreen.getText().toString().trim(),passwordLoginScreen.getText().toString().trim());
                              if(validater == true)
                              {   NetworkChecker networkChecker = new NetworkChecker(SignInScreen.this);
                                  if(networkChecker.checkNetwork()) {
                                      new ValidateLogin(SignInScreen.this).execute(usernameLoginScreen.getText().toString().trim()
                                              , passwordLoginScreen.getText().toString().trim());
                                  }
                                  else
                                  {
                                      //Toast.makeText(getApplicationContext(),"Network Not available \n Please Activate your Network",Toast.LENGTH_LONG).show();
                                      networkChecker.createDialog();
                                  }

                              }
                              else
                              {
                                  Toast.makeText(getApplicationContext(),"Please enter username and password",Toast.LENGTH_LONG).show();
                              }
                              break;
            case R.id.create_new_login: intent = new Intent(getApplicationContext() , SignUpScreen.class);
                                        startActivity(intent);
                                        break;
            case R.id.skip_login: intent = new Intent(getApplicationContext() , UserMainScreen.class);
                                  //intent = new Intent(getApplicationContext(),UploadProfilePictureAfterSignUp.class);
                                  intent.putExtra("username","null");
                                  startActivity(intent);
                                  break;
            case R.id.forgot_password: intent = new Intent(SignInScreen.this,ForgotPasswordScreen.class);
                                                startActivity(intent);
                                                break;
        }
    }

    private boolean validateData(String username , String password)
    {

        if(username.equals("") || password.equals(""))
          return false;
        else
          return true;
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

        class ValidateLogin extends AsyncTask<String , Void ,String>
        {
            ProgressDialog pdLoading;
            String link;
            String dataToBeSend;
            String result;
            Context context;
            ValidateLogin(Context context)
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
                passwordString = params[1];
                try {
                    dataToBeSend = "?username=" + URLEncoder.encode(usernameString, "UTF-8");
                    dataToBeSend += "&password=" + URLEncoder.encode(passwordString,"UTF-8");
                    link = "http://tourguideissc.esy.es/validateLogin.php" + dataToBeSend;
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
                            Toast.makeText(getApplicationContext(),"Login Sucessful",Toast.LENGTH_LONG).show();
                            setSharedPreferencesForUser(usernameString , passwordString);
                            intent = new Intent(getApplicationContext() , UserMainScreen.class);
                            //intent = new Intent(getApplicationContext(),UploadProfilePictureAfterSignUp.class);
                            intent.putExtra("username",usernameString);
                            startActivity(intent);
                        }
                        else if(loginResult.equals("FAILED"))
                              {
                                  Toast.makeText(getApplicationContext(),"Login Failed \n Invalid username or password",Toast.LENGTH_LONG).show();
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

