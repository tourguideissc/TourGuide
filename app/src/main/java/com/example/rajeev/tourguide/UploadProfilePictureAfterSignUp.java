package com.example.rajeev.tourguide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static android.R.attr.bitmap;

public class UploadProfilePictureAfterSignUp extends AppCompatActivity implements View.OnClickListener{

    ImageView userProfileImageView;
    Button cameraButton , gallaryButton,uploadButton;
    int RESULT_LOAD_IMAGE,CAMERA_REQUEST=1;
    String imageName,filePath,imageBaseCode,userName,password;
    Bitmap bitmap;
    Intent intent;
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
        setContentView(R.layout.activity_upload_profile_picture_after_sign_up);
        intent = getIntent();
        userName = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        getByID();
        setListeners();
    }


    public void getByID() {
        userProfileImageView = (ImageView) findViewById(R.id.user_profile_pic_upload_profile_sign_up);
        cameraButton = (Button) findViewById(R.id.camera_button_upload_profile_sign_up);
        gallaryButton = (Button) findViewById(R.id.gallary_button_upload_profile_sign_up);
        uploadButton = (Button) findViewById(R.id.upload_image_button_upload_profile_);
    }

    private void setListeners()
    {
     cameraButton.setOnClickListener(this);
     gallaryButton.setOnClickListener(this);
     uploadButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent i;
      switch(v.getId())
      {
          case R.id.gallary_button_upload_profile_sign_up :i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                                           startActivityForResult(i, RESULT_LOAD_IMAGE);
              break;
          case R.id.camera_button_upload_profile_sign_up : i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i,CAMERA_REQUEST);
                    break;
          case R.id.upload_image_button_upload_profile_: imageBaseCode =getStringImage(bitmap);
                                                          NetworkChecker networkChecker = new NetworkChecker(UploadProfilePictureAfterSignUp.this);
                                                          if(networkChecker.checkNetwork()) {
                                                              UploadProfileImage uploadProfileImage = new UploadProfileImage(UploadProfilePictureAfterSignUp.this);
                                                              uploadProfileImage.execute(imageBaseCode,userName);

                                                          }
                                                          else
                                                          {
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


    private String getNameFromPath(String filePath) {
        String split[] = filePath.split("/");
        int splitLength = split.length;
        return split[splitLength-1];
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


   // @RequiresApi(api = Build.VERSION_CODES.N)
   @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == RESULT_LOAD_IMAGE) && (resultCode == RESULT_OK) && (data != null)) {

            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            filePath = ImageFilePath.getPath(getBaseContext(),selectedImage);

            userProfileImageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
            imageName = getNameFromPath(filePath);
            Toast.makeText(getBaseContext(),imageName,Toast.LENGTH_LONG).show();
        }
       else if ((requestCode == CAMERA_REQUEST) && (resultCode == RESULT_OK) && (data != null)) {

            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            filePath = ImageFilePath.getPath(getBaseContext(),selectedImage);

            userProfileImageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
            imageName = getNameFromPath(filePath);
            Toast.makeText(getBaseContext(),imageName,Toast.LENGTH_LONG).show();
        }

    }











    //AsyncTask to validate whether user is valid user or not

    class UploadProfileImage extends AsyncTask<String , Void ,String>
    {
        ProgressDialog pdLoading;
        String link;
        String dataToBeSend;
        String result;
        Context context;
        String imagecode,username;
        UploadProfileImage(Context context)
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
           /* imagecode = params[0];
            String tmp = "g";
            username = params[1];
            try {
                //dataToBeSend = "?username=" + URLEncoder.encode(username, "UTF-8");
                dataToBeSend = "?username=" + URLEncoder.encode(tmp, "UTF-8");
                dataToBeSend += "&imagecode=" + URLEncoder.encode(imagecode,"UTF-8");
                link = "http://tourguideissc.esy.es/uploadProfile.php" + dataToBeSend;
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                result = bufferedReader.readLine();
                return result;
            }
            catch (Exception e)
            {
                return new String("Exception: " + e.getMessage());
            }*/








            String imageBase64Code = params[0];
            String userName = params[1];

            String URL="http://tourguideissc.esy.es/uploadProfile.php";
            String line;

            try {
                URL LoginURL = new URL(URL);
                HttpURLConnection httpURLConnectionForLogin = (HttpURLConnection) LoginURL.openConnection();
                httpURLConnectionForLogin.setRequestMethod("POST");
                httpURLConnectionForLogin.setDoInput(true);
                ;
                httpURLConnectionForLogin.setDoOutput(true);
                OutputStream outputStreamForLogin = httpURLConnectionForLogin.getOutputStream();
                BufferedWriter bufferedWriterForLogin = new BufferedWriter(new OutputStreamWriter(outputStreamForLogin, "UTF-8"), 8);
                String PostLoginData = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8") + "&"
                        + URLEncoder.encode("imagecode", "UTF-8") + "=" + URLEncoder.encode(imageBase64Code, "UTF-8");
                bufferedWriterForLogin.write(PostLoginData);
                bufferedWriterForLogin.flush();
                bufferedWriterForLogin.close();
                outputStreamForLogin.close();

                InputStream inputStreamForLogin = httpURLConnectionForLogin.getInputStream();
                BufferedReader bufferedReaderForLogin = new BufferedReader(new InputStreamReader(inputStreamForLogin, "UTF-8"));
                //InputStream inputStream = new BufferedInputStream(httpURLConnectionForLogin.getInputStream());

               /* StringBuilder stringBuilder = new StringBuilder();
                while ((line = bufferedReaderForLogin.readLine()) != null) {
                    stringBuilder.append(line);
                }

                bufferedReaderForLogin.close();
                inputStreamForLogin.close();
                httpURLConnectionForLogin.disconnect();

                result = stringBuilder.toString();*/
                result = bufferedReaderForLogin.readLine();
                bufferedReaderForLogin.close();
                inputStreamForLogin.close();
                httpURLConnectionForLogin.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;









        }

        @Override
        protected void onPostExecute(String result) {
            String jasonResult =result;
            if(jasonResult != null)
            {
                //try {
                   // JSONObject jsonObject = new JSONObject(jasonResult);
                    //String loginResult = jsonObject.getString("result");
                    String loginResult=result;

                    if(loginResult.equals("SUCCESS"))
                    {
                        Toast.makeText(getApplicationContext(),"Successfully updated profile picture",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(),UserMainScreen.class);
                        i.putExtra("username",userName);
                        i.putExtra("password",password);
                        sharedPreferences = getSharedPreferences(USERLOGINSTATUS, Context.MODE_PRIVATE);
                        setSharedPreferencesForUser(userName,password);
                        startActivity(i);
                        finish();
                    }
                    else if(loginResult.equals("FAILED"))
                    {
                        Toast.makeText(getApplicationContext(),"Unable to update profile picture",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Could not connect to database \n Please try again \n"+result,Toast.LENGTH_LONG).show();
                    }

            //    } catch (JSONException e) {
              //      e.printStackTrace();
               // }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Unable to get Data from server",Toast.LENGTH_LONG).show();
            }
            pdLoading.dismiss();
        }


    }








}
