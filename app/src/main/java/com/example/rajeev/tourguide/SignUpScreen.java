package com.example.rajeev.tourguide;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpScreen extends AppCompatActivity implements View.OnFocusChangeListener , View.OnClickListener{

    // All Views Declaration
    ImageButton historicalButton,religiousButton,adventureButton,foodButton,entertainmentButton,beachesButton,natureButton;
    EditText firstnameEditText,lastnameEditText,contactEditText,emailEditText,birthdateEditText,usernameEditText,passwordEditText,reEnterPasswordEditText,securityAnswerEditText;
    Spinner securityQuestionSpinner;
    Button createUserButton;
    ImageView imageInterest;
    TextView interestName;
    LinearLayout linearLayout;
    final Calendar c = Calendar.getInstance();
    int mYear = 1995;
    int mMonth = 0;
    int mDay = 1;
    int interestCounter[] = {0,0,0,0,0,0,0};
    int counters[] =  {0,0,0,0,0,0};
    ArrayList<String> interests = new ArrayList<String>();
    String tagName;
    View layoutView;
    Toast toast;

// End Declaration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
        findById(); // find Views using ID
        setListeners(); // setListeners

    } // End onCreate()

    private void findById()
    {
        //Image Buttons
        historicalButton = (ImageButton) findViewById(R.id.historical);
        religiousButton = (ImageButton) findViewById(R.id.religious);
        adventureButton = (ImageButton) findViewById(R.id.adventure);
        foodButton = (ImageButton) findViewById(R.id.food);
        entertainmentButton = (ImageButton) findViewById(R.id.entertainment);
        beachesButton = (ImageButton) findViewById(R.id.beaches);
        natureButton = (ImageButton) findViewById(R.id.nature);

        //EditTexts
        firstnameEditText = (EditText) findViewById(R.id.firstname_sign_up);
        lastnameEditText = (EditText) findViewById(R.id.lastname_sign_up);
        contactEditText = (EditText) findViewById(R.id.contact_sign_up);
        emailEditText = (EditText) findViewById(R.id.email_address_sign_up);
        birthdateEditText = (EditText) findViewById(R.id.birthdate_sign_up);
        usernameEditText = (EditText) findViewById(R.id.username_sign_up);
        passwordEditText = (EditText) findViewById(R.id.password_sign_up);
        reEnterPasswordEditText = (EditText) findViewById(R.id.reenter_password_sign_up);
        securityAnswerEditText = (EditText) findViewById(R.id.seq_answer_sign_up);

        //Spinner
         securityQuestionSpinner = (Spinner) findViewById(R.id.seq_question_sign_up);

        //Button
         createUserButton = (Button) findViewById(R.id.create_new_user_button);


        //For custom toast

        LayoutInflater layoutInflater = getLayoutInflater();
        layoutView = layoutInflater.inflate(R.layout.custom_toast,null);
        imageInterest = (ImageView) layoutView.findViewById(R.id.interest_imageview);
        interestName = (TextView) layoutView.findViewById(R.id.interest_name_textview);
        toast = Toast.makeText(getApplicationContext(),"Toast:Gravity.TOP",Toast.LENGTH_SHORT);
        toast.setView(layoutView);
        toast.setGravity(Gravity.CENTER,0,0);


    } //End findByID()

    private void setListeners()
    {
        //clickListener on ImageButtons

          historicalButton.setOnClickListener(this);
        religiousButton.setOnClickListener(this);
        adventureButton.setOnClickListener(this);
        foodButton.setOnClickListener(this);
        entertainmentButton.setOnClickListener(this);
        beachesButton.setOnClickListener(this);
        natureButton.setOnClickListener(this);

        birthdateEditText.setOnFocusChangeListener(this);
        createUserButton.setOnClickListener(this);

        // Add events to validate EditText
        firstnameEditText.addTextChangedListener(new GenericTextWatcher(firstnameEditText));
        lastnameEditText.addTextChangedListener(new GenericTextWatcher(lastnameEditText));
        contactEditText.addTextChangedListener(new GenericTextWatcher(contactEditText));
        emailEditText.addTextChangedListener(new GenericTextWatcher(emailEditText));
        passwordEditText.addTextChangedListener(new GenericTextWatcher(passwordEditText));
        reEnterPasswordEditText.addTextChangedListener(new GenericTextWatcher(reEnterPasswordEditText));

    } // End setListners

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId())
        {
            case R.id.birthdate_sign_up :
                             if(hasFocus) {
                                     DatePickerDialog dpd = new DatePickerDialog(this,
                                      new DatePickerDialog.OnDateSetListener() {

                                            @Override
                                             public void onDateSet(DatePicker view, int year,
                                                       int monthOfYear, int dayOfMonth) {
                                                  birthdateEditText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                                                   }
                                             }, mYear, mMonth, mDay);
                                        dpd.show();
                                           }
                                          break;

        }
    }//End onFocusChange()

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.create_new_user_button : String contents="";
                for(String str : interests)
               contents +=str+"\n";
               contents=contents.trim();
               // Toast.makeText(getApplicationContext(),contents,Toast.LENGTH_LONG).show();
                String firstname1=firstnameEditText.getText().toString();
                String lastname1=lastnameEditText.getText().toString();
                String contact1=contactEditText.getText().toString();
                String username1=usernameEditText.getText().toString();
                String birthdate1=birthdateEditText.getText().toString();
                String password1=passwordEditText.getText().toString();
                String ReEnterPassword1=reEnterPasswordEditText.getText().toString();
                String ans = securityAnswerEditText.getText().toString();
                String question = securityQuestionSpinner.getSelectedItem().toString();
               // Toast.makeText(getBaseContext(),contents , Toast.LENGTH_LONG).show();


                if(firstname1.contains(" ")||firstname1.toString().isEmpty()
                        || lastname1.contains(" ")|| lastname1.toString().isEmpty()
                        || contact1.contains(" ")|| contact1.toString().isEmpty()
                        || username1.contains(" ")|| username1.toString().isEmpty()
                        || birthdate1.contains(" ")|| birthdate1.toString().isEmpty()
                        || password1.contains(" ")|| password1.toString().isEmpty()
                        || ReEnterPassword1.contains(" ")|| ReEnterPassword1.toString().isEmpty()
                        || ans.contains(" ")|| ans.toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "You must fill all fields and there should not be any space", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(passwordEditText.getText().toString().equals(reEnterPasswordEditText.getText().toString()))
                    {
                        if(contents.equals(""))
                        {
                            Toast.makeText(getBaseContext(),"Please select atleast ONE interest" , Toast.LENGTH_LONG).show();
                        }
                        else
                        if(question.equals("Select Question")) {
                            Toast.makeText(getBaseContext(),"Please select Question" , Toast.LENGTH_LONG).show();
                        }
                        else {
                          //  Toast.makeText(getBaseContext(),"Correct" , Toast.LENGTH_LONG).show();
                            NetworkChecker networkChecker = new NetworkChecker(SignUpScreen.this);
                            if(networkChecker.checkNetwork()) {
                                CreateUser createUser = new CreateUser(SignUpScreen.this);
                                createUser.execute(firstname1, lastname1, contact1, emailEditText.getText().toString(), birthdate1, username1, password1, securityQuestionSpinner.getSelectedItem().toString(), securityAnswerEditText.getText().toString(), contents);
                            }
                            else
                            {
                                networkChecker.createDialog();
                                //Toast.makeText(getApplicationContext(),"Network Not available \n Please Activate your Network",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    else
                    {
                        reEnterPasswordEditText.setError("Password and ReEnter password must have same password");
                    }
                }


                break;
               // end createUserButton


            case R.id.historical :
                if(interestCounter[0] == 0) {
                historicalButton.setImageResource(R.drawable.historical_red);
                tagName = historicalButton.getTag().toString();

                imageInterest.setImageResource(R.drawable.historical_red);
                interestName.setText(tagName);
                toast.show();

                interests.add(tagName);
                interestCounter[0]=1;
            }
            else
            {
                historicalButton.setImageResource(R.drawable.historical_black);
                tagName = historicalButton.getTag().toString();
                interests.remove(tagName);
                interestCounter[0]=0;
            }break;


            case R.id.religious :
                if(interestCounter[1] == 0) {
                    religiousButton.setImageResource(R.drawable.religious_red);
                    tagName = religiousButton.getTag().toString();

                    imageInterest.setImageResource(R.drawable.religious_red);
                    interestName.setText(tagName);
                    toast.show();

                    interests.add(tagName);
                    interestCounter[1]=1;
                }
                else
                {
                    religiousButton.setImageResource(R.drawable.religious_black);
                    tagName = religiousButton.getTag().toString();
                    interests.remove(tagName);
                    interestCounter[1]=0;
                }break;


            case R.id.adventure :
                if(interestCounter[2] == 0) {
                    adventureButton.setImageResource(R.drawable.adventure_red);
                    tagName = adventureButton.getTag().toString();

                    imageInterest.setImageResource(R.drawable.adventure_red);
                    interestName.setText(tagName);
                    toast.show();

                    interests.add(tagName);
                    interestCounter[2]=1;
                }
                else
                {
                    adventureButton.setImageResource(R.drawable.adventure_black);
                    tagName = adventureButton.getTag().toString();
                    interests.remove(tagName);
                    interestCounter[2]=0;
                }break;


            case R.id.food :
                if(interestCounter[3] == 0) {
                    foodButton.setImageResource(R.drawable.food_red);
                    tagName = foodButton.getTag().toString();

                    imageInterest.setImageResource(R.drawable.food_red);
                    interestName.setText(tagName);
                    toast.show();

                    interests.add(tagName);
                    interestCounter[3]=1;
                }
                else
                {
                    foodButton.setImageResource(R.drawable.food_black);
                    tagName = foodButton.getTag().toString();
                    interests.remove(tagName);
                    interestCounter[3]=0;
                }break;


            case R.id.entertainment :
                if(interestCounter[4] == 0) {
                    entertainmentButton.setImageResource(R.drawable.entertainment_red);
                    tagName = entertainmentButton.getTag().toString();

                    imageInterest.setImageResource(R.drawable.entertainment_red);
                    interestName.setText(tagName);
                    toast.show();

                    interests.add(tagName);
                    interestCounter[4]=1;
                }
                else
                {
                    entertainmentButton.setImageResource(R.drawable.entertainment_black);
                    tagName = entertainmentButton.getTag().toString();
                    interests.remove(tagName);
                    interestCounter[4]=0;
                }break;


            case R.id.beaches :
                if(interestCounter[5] == 0) {
                    beachesButton.setImageResource(R.drawable.beahes_red);
                    tagName = beachesButton.getTag().toString();

                    imageInterest.setImageResource(R.drawable.beahes_red);
                    interestName.setText(tagName);
                    toast.show();

                    interests.add(tagName);
                    interestCounter[5]=1;
                }
                else
                {
                    beachesButton.setImageResource(R.drawable.beahes_black);
                    tagName = beachesButton.getTag().toString();
                    interests.remove(tagName);
                    interestCounter[5]=0;
                }break;


            case R.id.nature :
                if(interestCounter[6] == 0) {
                    natureButton.setImageResource(R.drawable.nature_red);
                    tagName = natureButton.getTag().toString();

                    imageInterest.setImageResource(R.drawable.nature_red);
                    interestName.setText(tagName);
                    toast.show();

                    interests.add(tagName);
                    interestCounter[6]=1;
                }
                else
                {
                    natureButton.setImageResource(R.drawable.nature_black);
                    tagName = natureButton.getTag().toString();
                    interests.remove(tagName);
                    interestCounter[6]=0;
                }break;
        }
    }//End onClick();





    //Text Watcher

    public class GenericTextWatcher implements TextWatcher
    {
        View view;
        public GenericTextWatcher(View view)
        {
            this.view = view;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //  view.setBackgroundResource(R.drawable.edittextshapeonfocus);

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch(view.getId())
            {
                case R.id.firstname_sign_up :
                    if(!s.toString().matches("[a-zA-Z]*") && !s.toString().equals(""))
                    {
                        firstnameEditText.setError("Numbers and Symbols are not allowed in Firstname");
                        counters[0]=0;
                    }
                    else {
                        firstnameEditText.setError(null);
                        counters[0]=1;
                    }break;
                case R.id.lastname_sign_up: if(!s.toString().matches("[a-zA-Z]*") && !s.toString().equals(""))
                {
                    lastnameEditText.setError("Numbers and Symbols are not allowed in Lastname");
                    counters[1]=0;

                }
                else {
                    lastnameEditText.setError(null);
                    counters[1]=1;

                }break;

                case R.id.contact_sign_up : if(!(s.toString().length()==10) && !s.toString().equals("")) {
                    contactEditText.setError("Number should be of 10 digits");
                    counters[2]=0;
                }
                else {
                    contactEditText.setError(null);
                    counters[2]=1;
                }break;

                case R.id.email_address_sign_up : if(!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches() && !s.toString().equals("")) {
                    emailEditText.setError("Please Enter valis EMAIL ADDRESS (abc@website.com / .co.in)");
                    counters[3]=0;
                }
                else
                {
                    emailEditText.setError(null);
                    counters[3]=1;
                }break;

                case R.id.password_sign_up : String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";
                    Pattern pattern= Pattern.compile(PASSWORD_PATTERN);
                    Matcher matcher=pattern.matcher(s.toString());
                    if(!matcher.matches() && !s.toString().equals(""))
                    {
                        passwordEditText.setError("Password must contain atleast 1 number and special character");
                        counters[4]=0;
                    }
                    else {
                        passwordEditText.setError(null);
                        counters[4]=1;
                    }break;
                case R.id.reenter_password_sign_up : String PASSWORD_PATTERN1 = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";
                    Pattern pattern1= Pattern.compile(PASSWORD_PATTERN1);
                    Matcher matcher1=pattern1.matcher(s.toString());
                    if(!matcher1.matches() && !s.toString().equals(""))
                    {
                        reEnterPasswordEditText.setError("Password must contain atleast 1 number and special character");
                        counters[5]=0;
                    }
                    else {
                        reEnterPasswordEditText.setError(null);
                        counters[5]=1;
                    }break;
            }//end switch
            //view.setBackgroundResource(R.drawable.edittextshape);

            for(int i=0;i<6;i++)
            {
                if(counters[i]!=1)
                {
                    createUserButton.setEnabled(false);
                }
                else
                    createUserButton.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }//end class GenericTextWatcher












//AsyncTask to validate whether user is valid user or not

    class CreateUser extends AsyncTask<String , Void ,String>
    {
        ProgressDialog pdLoading;
        String link;
        String dataToBeSend;
        String result;
        Context context;
        String firstname,lastname,contact,email,birthdate,username,password,securityquestion,securityanswer,interests;
        CreateUser(Context context)
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
            firstname = params[0];
            lastname = params[1];
            contact = params[2];
            email = params[3];
            birthdate = params[4];
            username = params[5];
            password = params[6];
            securityquestion=params[7];
            securityanswer = params[8];
            interests=params[9];
            try {
                dataToBeSend = "?firstname=" + URLEncoder.encode(firstname, "UTF-8");
                dataToBeSend += "&lastname=" + URLEncoder.encode(lastname,"UTF-8");
                dataToBeSend += "&contact=" + URLEncoder.encode(contact, "UTF-8");
                dataToBeSend += "&email=" + URLEncoder.encode(email,"UTF-8");
                dataToBeSend += "&birthdate=" + URLEncoder.encode(birthdate, "UTF-8");
                dataToBeSend += "&username=" + URLEncoder.encode(username,"UTF-8");
                dataToBeSend += "&password=" + URLEncoder.encode(password, "UTF-8");
                dataToBeSend += "&securityquestion=" + URLEncoder.encode(securityquestion,"UTF-8");
                dataToBeSend += "&securityanswer=" + URLEncoder.encode(securityanswer, "UTF-8");
                dataToBeSend += "&interests=" + URLEncoder.encode(interests,"UTF-8");
                link = "http://tourguideissc.esy.es/createUser.php" + dataToBeSend;
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
              // String loginResult = result;

                    if(loginResult.equals("SUCCESS"))
                    {
                        Toast.makeText(getApplicationContext(),"Created SuccessFully",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignUpScreen.this,UploadProfilePictureAfterSignUp.class);
                        intent.putExtra("username",username);
                        intent.putExtra("password",password);
                        startActivity(intent);
                        finish();
                    }
                    else if(loginResult.equals("FAILED"))
                    {
                        Toast.makeText(getApplicationContext(),"Login Failed \n Invalid username or password",Toast.LENGTH_LONG).show();
                    }
                    else if (loginResult.equals("USER PRESENT"))
                    {
                        Toast.makeText(getApplicationContext(),"Username '"+username+"' Already Exists",Toast.LENGTH_LONG).show();
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
            {Toast.makeText(getApplicationContext(),"Unable to get Data from server",Toast.LENGTH_LONG).show();
            }
            pdLoading.dismiss();
        }


    }













}  //End class SignUpScreen
