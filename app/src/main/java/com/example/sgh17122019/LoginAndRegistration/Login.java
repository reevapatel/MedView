package com.example.sgh17122019.LoginAndRegistration;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.sgh17122019.AllGetterSetter;
import com.example.sgh17122019.MainActivity;
import com.example.sgh17122019.R;
import com.example.sgh17122019.Utility.All_URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class Login extends AppCompatActivity  implements
        AdapterView.OnItemSelectedListener  {

    String[] type = { "HostelHead", "Student", "Gurdians", "CraeTaker"};

    ImageView top_curve;
    EditText email,password;
    TextView email_text, password_text, login_title;
    ImageView logo;
    LinearLayout new_user_layout;
    CardView login_card;

    SharedPreferences sharedPreferences;
    public static final String MY_PREFERENCES = "MyPrefs";
    public static final String EMAIL = "email";
    public static final String STATUS = "status";
    public static final String USERNAME = "username";
    private boolean status;

    //URL LINK SET CLASS
    All_URL ar= new All_URL();
    public final String URL_LOGIN = ar.LOGIN_LINK;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        top_curve = findViewById(R.id.top_curve);
        email = findViewById(R.id.email);
        email_text = findViewById(R.id.email_text);
        password = findViewById(R.id.password);
        password_text = findViewById(R.id.password_text);
        logo = findViewById(R.id.logo);
        login_title = findViewById(R.id.login_text);
        new_user_layout = findViewById(R.id.new_user_text);
        login_card = findViewById(R.id.login_card);

        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,type);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        status = sharedPreferences.getBoolean(STATUS, false);

       if (status){
            finish();
            Intent intent = new Intent(Login.this, MainActivity.class);
            Toasty.error(getApplicationContext(),"Login Activity On!!",Toasty.LENGTH_SHORT).show();
            startActivity(intent);
        }

        Animation top_curve_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.top_down);
        top_curve.startAnimation(top_curve_anim);

        Animation editText_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.edittext_anim);
        email.startAnimation(editText_anim);
        password.startAnimation(editText_anim);


        Animation field_name_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.field_name_anim);
        email_text.startAnimation(field_name_anim);
        password_text.startAnimation(field_name_anim);
        logo.startAnimation(field_name_anim);
        login_title.startAnimation(field_name_anim);
        spin.startAnimation(field_name_anim);

        Animation center_reveal_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.center_reveal_anim);
        login_card.startAnimation(center_reveal_anim);

        Animation new_user_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.down_top);
        new_user_layout.startAnimation(new_user_anim);



    }
    String typedata = null;
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //Toast.makeText(getApplicationContext(),type[i] , Toast.LENGTH_LONG).show();
        if(type[i]=="HostelHead"){
            typedata="hostelHead";
        }
        if(type[i]=="Student"){
            typedata="student";
        }
        if(type[i]=="Gurdians"){
            typedata="parents";
        }
        if(type[i]=="CareTaker"){
            typedata="familyDoctor";
        }
        Toast.makeText(getApplicationContext(),typedata, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }




    public void register(View view) {
        startActivity(new Intent(this, Registration.class));
    }

    public void loginButton(View view) {

        //Toast.makeText(this,"Login Clicked",Toast.LENGTH_SHORT).show();
        final String email_string = email.getText().toString().trim();
        final String password_string = password.getText().toString();
        if(email_string.isEmpty()){
            email.setError("Enter Email Adderss");


        }else if( password_string.isEmpty()){
            password.setError("Enter Password");
        }
        else {

            class Logindata extends AsyncTask<Void, Void, String> {
                ProgressDialog pdLoading = new ProgressDialog(Login.this);



                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    //this method will be running on UI thread
                    pdLoading.setMessage("\tLoading...");
                    pdLoading.setCancelable(false);
                    pdLoading.show();
                }

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("hostelHeadEmail", email_string);
                    params.put("type",typedata);
                    params.put("hostelHeadPassword", password_string);


                    //returing the response
                    return requestHandler.sendPostRequest(URL_LOGIN, params);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    pdLoading.dismiss();

                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);
                        //if no error in response
                        if (!obj.getBoolean("error")) {
                            String reponse = obj.getString("message");

                            int reponse1 =obj.getInt("message");
                            //String username = obj.getString("username");

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(USERNAME, "Ab");
                            editor.putString(EMAIL, email_string);
                            editor.putBoolean(STATUS, true);
                            editor.apply();

                            finish();

                            //Toasty.success(getApplicationContext(),"ReponseId:-"+ reponse1 , Toasty.LENGTH_LONG).show();
                            Toasty.success(getApplicationContext(),"ReponseMessage:-"+ reponse , Toasty.LENGTH_LONG).show();
                            AllGetterSetter.setStudentId(reponse);


                            //Toasty.success(getApplicationContext(),"Login Successful",Toasty.LENGTH_SHORT).show();
                            //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Login.this, MainActivity.class);


                            startActivity(intent);
                        }
                        else{
                            Toasty.error(getApplicationContext(),"Invalid Email_id or Password",Toasty.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Toast.makeText(LoginActivity.this, "Exception: " + e, Toast.LENGTH_LONG).show();
                        Toasty.error(getApplicationContext(),"Catch Block "+ e.toString(),Toasty.LENGTH_SHORT).show();
                    }
                }
            }

            Logindata login = new Logindata();
            login.execute();
        }
    }



}

