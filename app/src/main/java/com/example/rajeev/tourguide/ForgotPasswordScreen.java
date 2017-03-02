package com.example.rajeev.tourguide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ForgotPasswordScreen extends AppCompatActivity implements View.OnClickListener{

    Spinner securityQuestion;
    EditText usernameOnForgotPassword,securityAnswer;
    Button reserPasswordButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_screen);
        getByID();
        setListeners();
    }

    private void getByID()
    {
        securityQuestion = (Spinner) findViewById(R.id.seq_question_on_forgot_password);
        usernameOnForgotPassword = (EditText) findViewById(R.id.username_on_forgot_password);
        securityAnswer = (EditText) findViewById(R.id.seq_answer_on_forgot_password);
        reserPasswordButton = (Button) findViewById(R.id.reset_password_button);
    }

    private void setListeners()
    {
        reserPasswordButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        String question = securityQuestion.getSelectedItem().toString();
        if(usernameOnForgotPassword.getText().toString().trim().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please enter username",Toast.LENGTH_LONG).show();
        }
        else if(securityAnswer.getText().toString().trim().equals(""))
              {
                  Toast.makeText(getApplicationContext(),"Please enter Security Answer",Toast.LENGTH_LONG).show();
              }
              else if(question.equals("Select Question"))
                    {
                        Toast.makeText(getApplicationContext(),"Please select Security Question",Toast.LENGTH_LONG).show();
                    }
                   else
                    {
                     //   Toast.makeText(getApplicationContext(),"Everything is correct",Toast.LENGTH_LONG).show();
                        NetworkChecker networkChecker = new NetworkChecker(ForgotPasswordScreen.this);
                        if(networkChecker.checkNetwork()) {
                            RecoverPassword recoverPassword = new RecoverPassword(ForgotPasswordScreen.this);
                            recoverPassword.execute(usernameOnForgotPassword.getText().toString().trim(),
                                    question, securityAnswer.getText().toString().trim());
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Network Not available \n Please Activate your Network",Toast.LENGTH_LONG).show();
                        }
                    }

    }











    //AsyncTask to validate whether user is valid user or not

    class RecoverPassword extends AsyncTask<String , Void ,String>
    {
        ProgressDialog pdLoading;
        String link;
        String dataToBeSend;
        String result;
        Context context;
        String usernameString , securityAnswerString,securityQuestionString;
        RecoverPassword(Context context)
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
            securityQuestionString = params[1];
            securityAnswerString = params[2];
            try {
                dataToBeSend = "?username=" + URLEncoder.encode(usernameString, "UTF-8");
                dataToBeSend += "&securityquestion=" + URLEncoder.encode(securityQuestionString,"UTF-8");
                dataToBeSend += "&securityanswer=" + URLEncoder.encode(securityAnswerString,"UTF-8");
                link = "http://tourguideissc.esy.es/recoverPassword.php" + dataToBeSend;
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
            String user;
            String email;
            if(jasonResult != null)
            {
                try {
                    JSONObject jsonObject = new JSONObject(jasonResult);
                    String temp = jsonObject.getString("result");
                    String[] temp1 = temp.split(" ");
                    String loginResult = temp1[0];
                    String UserOREmail = temp1[1];
                    if(loginResult.equals("SUCCESS"))
                    {
                        //email =jsonObject.getString("email");
                        Toast.makeText(getApplicationContext(),"Password has been sent to "+UserOREmail,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context,SignInScreen.class);
                        startActivity(intent);
                    }
                    else if(loginResult.equals("FAILED"))
                    {
                        //user = jsonObject.getString("user");
                        Toast.makeText(getApplicationContext(),"Unable to find user '"+UserOREmail+"' \n Please Enter valid username and Security Answer",Toast.LENGTH_LONG).show();
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
