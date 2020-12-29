package com.example.sgh17122019.LoginAndRegistration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.sgh17122019.R;
import com.example.sgh17122019.Utility.All_URL;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;


public class Registration extends AppCompatActivity implements  View.OnClickListener{
    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 10;
    public static String temp;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private boolean status;

    private ViewGroup mPhoneNumberViews;
    private ViewGroup mSignedInViews;

    private TextView mStatusText;
    private TextView mDetailText;

    private EditText mPhoneNumberField;
    private EditText mVerificationField;

    private Button mStartButton;
    private Button mVerifyButton;
    private Button mResendButton;
    private Button mSignOutButton;

    ImageView top_curve;
    CardView register_card;
    LinearLayout already_have_account_layout;
    EditText name, email, password,hostelname,hosteladd;
    TextView name_text, email_text, password_text, login_title,phone_text,verification_text,hostel_name_text,hostel_add_text;
    TextView logo;
    public static String verificationcode;


    //URL LINK SET CLASS
    All_URL ar= new All_URL();
    public final String URL_REGISTER = All_URL.REGISTER_LINK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        top_curve = findViewById(R.id.top_curve);
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }







        name = findViewById(R.id.name);
        name_text = findViewById(R.id.name_text);
        email = findViewById(R.id.email);
        email_text = findViewById(R.id.email_text);
        password = findViewById(R.id.password);
        password_text = findViewById(R.id.password_text);
        //phone = findViewById(R.id.field_phone_number);
        phone_text=findViewById(R.id.phonenotext);
        verification_text=findViewById(R.id.verificationtext);
        already_have_account_layout = findViewById(R.id.already_have_account_text);
        register_card = findViewById(R.id.register_card);
        hostel_name_text =findViewById(R.id.hostelname);
        hostelname=findViewById(R.id.edit_hostelname);
        hostel_add_text =findViewById(R.id.hosteladd);
        hosteladd =findViewById(R.id.edit_hosteladd);




        mPhoneNumberViews = findViewById(R.id.phone_auth_fields);
        //mSignedInViews = findViewById(R.id.signed_in_buttons);

        mStatusText = findViewById(R.id.status);
        mDetailText = findViewById(R.id.detail);


        mPhoneNumberField = findViewById(R.id.field_phone_number);
        mVerificationField = findViewById(R.id.field_verification_code);

        mStartButton = findViewById(R.id.button_start_verification);
        mVerifyButton = findViewById(R.id.button_verify_phone);
        mResendButton = findViewById(R.id.button_resend);
        //mSignOutButton = findViewById(R.id.sign_out_button);


        Animation top_curve_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.top_down);
        top_curve.startAnimation(top_curve_anim);


        Animation editText_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.edittext_anim);
        name.startAnimation(editText_anim);
        email.startAnimation(editText_anim);
        password.startAnimation(editText_anim);
        mPhoneNumberField.startAnimation(editText_anim);
        mVerificationField.startAnimation(editText_anim);
        hostelname.startAnimation(editText_anim);
        hosteladd.startAnimation(editText_anim);

        Animation field_name_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.field_name_anim);
        name_text.startAnimation(field_name_anim);
        email_text.startAnimation(field_name_anim);
        email_text.startAnimation(field_name_anim);
        password_text.startAnimation(field_name_anim);
        phone_text.startAnimation(field_name_anim);
        verification_text.startAnimation(field_name_anim);
        hostel_name_text.startAnimation(field_name_anim);
        hostel_add_text.startAnimation(field_name_anim);


        Animation center_reveal_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.center_reveal_anim);
        register_card.startAnimation(center_reveal_anim);
        mStartButton.startAnimation(center_reveal_anim);
        mVerifyButton.startAnimation(center_reveal_anim);
        mResendButton.startAnimation(center_reveal_anim);



        Animation new_user_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.down_top);
        already_have_account_layout.startAnimation(new_user_anim);
//        mSignedInViews.startAnimation(new_user_anim);






        // Assign click listeners
        mStartButton.setOnClickListener(this);
        mVerifyButton.setOnClickListener(this);
        mResendButton.setOnClickListener(this);
       // mSignOutButton.setOnClickListener(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    mPhoneNumberField.setError("Invalid phone number.");
                    //name.setError("Please Enter Name");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }



                // Show a message and update the UI
                // [START_EXCLUDE]
                updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }


            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                verificationcode =verificationId;


                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // [START_EXCLUDE]
                // Update UI
                updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        // [START_EXCLUDE]
        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(mPhoneNumberField.getText().toString());
        }
        // [END_EXCLUDE]
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }
    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
        //enableViews(mStartButton);
    }
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        if (!phoneNumber.isEmpty()) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks,         // OnVerificationStateChangedCallbacks
                    token);             // ForceResendingToken from callbacks
        }else{
            mPhoneNumberField.setError("Enter Phone Number");
        }
    }
    public static Task<AuthResult> task1;
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        task1= task;
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            if (!validatePhoneNumber()) {

                                Toasty.info(getApplicationContext(),"Please Fill empty field!",Toasty.LENGTH_SHORT).show();

                                return;
                            }

                            DatabaseRegistration(false);
/**
                            final String email_string = email.getText().toString();
                            final String name_string = name.getText().toString();
                            final String password_string = password.getText().toString();
                            final String phone_string = mPhoneNumberField.getText().toString();
                            class LoginData extends AsyncTask<Void, Void, String> {
                                ProgressDialog pdLoading = new ProgressDialog(Registration.this);


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
                                    params.put("email", email_string);
                                    params.put("password", password_string);
                                    params.put("username", name_string);
                                    params.put("gender",phone_string);
                                    params.put("UserType","Abhishek");
                                    params.put("company_Name","gh");
                                    //returing the response
                                    return requestHandler.sendPostRequest(URL_REGISTER, params);
                                }

                                @Override
                                protected void onPostExecute(String s) {
                                    super.onPostExecute(s);
                                    pdLoading.dismiss();

                                    try {
                                        //converting response to json object
                                        JSONObject obj = new JSONObject(s);
                                        //if no error in response
                                        String reponse = obj.getString("message");
                                        if (!obj.getBoolean("error")) {

                                            if(reponse.equals("User Registered Successfully")){
                                                Toasty.success(getApplicationContext(), reponse , Toasty.LENGTH_LONG).show();
                                                Intent intent = new Intent(Registration.this,Login.class);
                                                startActivity(intent);
                                            }else if(reponse.equals("Cannot complete user registration")){
                                                Toasty.error(getApplicationContext(), reponse , Toasty.LENGTH_LONG).show();
                                            }else{
                                                Toasty.error(getApplicationContext(), reponse , Toasty.LENGTH_LONG).show();
                                            }

                                            Intent intent = new Intent(Registration.this,Login.class);
                                            startActivity(intent);
                                            //Toasty.success(getApplicationContext(),"Registration Successful!",Toasty.LENGTH_SHORT).show();
                                            //startActivity(new Intent(getApplicationContext(), Mobile_varification.class));
                                        }
                                        else{
                                            //Toast.makeText(getApplicationContext(), obj.getString("Something Wrong"), Toast.LENGTH_LONG).show();
                                            Toasty.error(getApplicationContext(),"Something Went Wrong!",Toasty.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        //Toast.makeText(RegisterActivity.this, , Toast.LENGTH_LONG).show();
                                        //Toast.makeText(getApplicationContext(), "Exception: " + e, Toast.LENGTH_LONG).show();
                                        //Toasty.error(getApplicationContext(),"Invalid Email_id OR Password!",Toasty.LENGTH_SHORT).show();
                                        Toasty.error(getApplicationContext(),""+e.getMessage(),Toasty.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            // Intent intent = new Intent(Registration.this, Registration.class);
                            //startActivity(intent);


                            LoginData login = new LoginData();
                            login.execute();


*/



                            //enableViews(mStartButton);

                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            updateUI(STATE_SIGNIN_SUCCESS, user);
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                mVerificationField.setError("Invalid code.");
                                //disableViews(mStartButton);
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }else{
                            mVerificationField.setError("Invalid code.");
                            Toast.makeText(Registration.this,"Innnnnnnnnnn Code Eroror",Toast.LENGTH_LONG).show();

                        }

                    }
                });
    }
    /*private void signOut() {
        mAuth.signOut();
        updateUI(STATE_INITIALIZED);
    }*/

    private void updateUI(int uiState) {
        updateUI(uiState, mAuth.getCurrentUser(), null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user);
        } else {
            updateUI(STATE_INITIALIZED);
        }
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
        switch (uiState) {
            case STATE_INITIALIZED:
                // Initialized state, show only the phone number field and start button
                enableViews( mPhoneNumberField,mVerifyButton,mStartButton);
                disableViews(mResendButton, mVerificationField);
                mDetailText.setText(null);
                break;
            case STATE_CODE_SENT:
                // Code sent state, show the verification field, the
                enableViews(mStartButton,mVerifyButton, mResendButton, mPhoneNumberField, mVerificationField);
                //disableViews(mStartButton);
                mDetailText.setText(R.string.status_code_sent);
                break;
            case STATE_VERIFY_FAILED:
                // Verification has failed, show all options
                enableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField,
                        mVerificationField);
                //disableViews(mStartButton);
                mDetailText.setText(R.string.status_verification_failed);
                break;
            case STATE_VERIFY_SUCCESS:
                // Verification has succeeded, proceed to firebase sign in
                enableViews(mStartButton, mResendButton, mPhoneNumberField,
                        mVerificationField);
                disableViews(mVerifyButton);
                mDetailText.setText(R.string.status_verification_succeeded);

                // Set the verification text based on the credential
                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        mVerificationField.setText(cred.getSmsCode());
                    } else {
                        mVerificationField.setText(R.string.instant_validation);
                    }
                }

                break;
            case STATE_SIGNIN_FAILED:
                // No-op, handled by sign-in check
                //mDetailText.setText(R.string.status_sign_in_failed);
                break;
            case STATE_SIGNIN_SUCCESS:
                // Np-op, handled by sign-in check
                break;
        }

        if (user == null) {
            // Signed out
            mPhoneNumberViews.setVisibility(View.VISIBLE);
            //mSignedInViews.setVisibility(View.GONE);

            //mStatusText.setText(R.string.signed_out);
        } else {
            // Signed in
          /*  Intent intent = new Intent(Registration.this,MainActivity.class);
            startActivity(intent);*/
            /*mPhoneNumberViews.setVisibility(View.GONE);
            mSignedInViews.setVisibility(View.VISIBLE);

            enableViews(mPhoneNumberField, mVerificationField);
            mPhoneNumberField.setText(null);
            mVerificationField.setText(null);

            mStatusText.setText(R.string.signed_in);
            mDetailText.setText(getString(R.string.firebase_status_fmt, user.getUid()));*/
        }
    }
    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        final String email_string = email.getText().toString();
        final String name_string = name.getText().toString();
        final String password_string = password.getText().toString();
        final String phone_string = mPhoneNumberField.getText().toString();

        /*if(name_string.isEmpty()){
            if(email_string.isEmpty()){
                if(password_string.isEmpty()){
                   if(phone_string.isEmpty()){
                       name.setError("Enter Name..");
                       email.setError("Enter Email..");
                       password.setError("Password must be contain two digit..");
                       mPhoneNumberField.setError("Invalid phone number.");
                       return false;
                   }else{
                       name.setError("Enter Name..");
                       email.setError("Enter Email..");
                       password.setError("Password must be contain two digit..");
                       return false;
                   }
                }else{
                    name.setError("Enter Name..");
                    email.setError("Enter Email..");
                    return false;
                }
            }else{
                name.setError("Enter Name..");
                return false;
            }
        }*/

        String code = mVerificationField.getText().toString();
         if(TextUtils.isEmpty(name_string)){
            name.setError("Error Name");
            return false;
        }else if(TextUtils.isEmpty(email_string)){
            email.setError("Error Email");
            return false;
        }else if(TextUtils.isEmpty(password_string)){
            password.setError("Error Password");
            return false;
        }else if (TextUtils.isEmpty(phoneNumber) ) {
            mPhoneNumberField.setError("Invalid phone number.");
            return false;
        }else if(TextUtils.isEmpty(code)){
            mVerificationField.setError("Cannot be empty.");
            startPhoneNumberVerification(mPhoneNumberField.getText().toString());
            return false;
        }

        return true;
    }
    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
        }
    }





    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.button_start_verification:
                    if (!validatePhoneNumber()) {

                        Toasty.info(getApplicationContext(),"Please Fill empty field!",Toasty.LENGTH_SHORT).show();
                        return;
                    }
                    String otp = mVerificationField.getText().toString();
                final String hostelname_string=hostelname.getText().toString();
                final String hostel_add_string=hosteladd.getText().toString();

                    if (otp.equals("(instant validation)")){
                         if(TextUtils.isEmpty(hostelname_string)){
                             //hostelname.setError("Error HostelName");
                             //Toasty.info(getApplicationContext(),"HostelName Empty!",Toasty.LENGTH_SHORT).show();
                        }else if(TextUtils.isEmpty(hostel_add_string)){
                             //hosteladd.setError("Error HostelAdd");
                             //Toasty.info(getApplicationContext(),"Hostel Address Empty!",Toasty.LENGTH_SHORT).show();
                         }else{
                             DatabaseRegistration(true);
                         }
                        /*final String email_string = email.getText().toString();
                final String name_string = name.getText().toString();
                final String password_string = password.getText().toString();
                final String phone_string = mPhoneNumberField.getText().toString();

//                verifyPhoneNumberWithCode(mVerificationId, code);
                //startPhoneNumberVerification(mPhoneNumberField.getText().toString());
                class Login extends AsyncTask<Void, Void, String> {
                    ProgressDialog pdLoading = new ProgressDialog(Registration.this);


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
                        params.put("email", email_string);
                        params.put("password", password_string);
                        params.put("username", name_string);
                        params.put("gender",phone_string);
                        params.put("UserType","Abhishek");
                        params.put("company_Name","gh");
                        //returing the response
                        return requestHandler.sendPostRequest(URL_REGISTER, params);
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        pdLoading.dismiss();

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(s);
                            //if no error in response
                            String reponse = obj.getString("message");
                            if (!obj.getBoolean("error")) {

                                if(reponse.equals("User Registered Successfully")){
                                    Toasty.success(getApplicationContext(), reponse+" reg button" , Toasty.LENGTH_LONG).show();
                                    Intent intent = new Intent(Registration.this,Login.class);
                                    startActivity(intent);
                                }else if(reponse.equals("Cannot complete user registration")){
                                    Toasty.error(getApplicationContext(), reponse , Toasty.LENGTH_LONG).show();
                                }else{
                                    Toasty.error(getApplicationContext(), reponse , Toasty.LENGTH_LONG).show();
                                }


                                //Toasty.success(getApplicationContext(),"Registration Successful!",Toasty.LENGTH_SHORT).show();
                                //startActivity(new Intent(getApplicationContext(), Mobile_varification.class));
                            }
                            else{
                                //Toast.makeText(getApplicationContext(), obj.getString("Something Wrong"), Toast.LENGTH_LONG).show();
                                Toasty.error(getApplicationContext(),"Something Went Wrong!",Toasty.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(RegisterActivity.this, , Toast.LENGTH_LONG).show();
                            //Toast.makeText(getApplicationContext(), "Exception: " + e, Toast.LENGTH_LONG).show();
                            //Toasty.error(getApplicationContext(),"Invalid Email_id OR Password!",Toasty.LENGTH_SHORT).show();
                            Toasty.error(getApplicationContext(),""+e.getMessage(),Toasty.LENGTH_SHORT).show();
                        }
                    }
                }

                // Intent intent = new Intent(Registration.this, Registration.class);
                //startActivity(intent);


                Login login = new Login();
                login.execute();*/

                    }else{
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationcode, otp);
                        signInWithPhoneAuthCredential(credential);
                    }
                    break;
            case R.id.button_verify_phone:

                    String code = mVerificationField.getText().toString();
                    final String phone_string1 = mPhoneNumberField.getText().toString();
                    if (!validatePhoneNumber()) {
                        final String hostelname_string1=hostelname.getText().toString();
                        final String hostel_add_string1=hosteladd.getText().toString();
                        if(TextUtils.isEmpty(hostelname_string1)){
                            //hostelname.setError("Error HostelName");
                            //Toasty.info(getApplicationContext(),"HostelName Empty!",Toasty.LENGTH_SHORT).show();
                        }else if(TextUtils.isEmpty(hostel_add_string1)) {
                            //hosteladd.setError("Error HostelAdd");
                            //Toasty.info(getApplicationContext(), "Hostel Address Empty!", Toasty.LENGTH_SHORT).show();
                        }
                            Toasty.info(getApplicationContext(),"Please Fill empty field!",Toasty.LENGTH_SHORT).show();
                            return;

                    }
                    startPhoneNumberVerification(mPhoneNumberField.getText().toString());
                    if (TextUtils.isEmpty(code) ) {
                        mVerificationField.setError("Cannot be empty.");
                        return;
                    }

                    break;
            case R.id.button_resend:
                    resendVerificationCode(mPhoneNumberField.getText().toString(), mResendToken);
                    break;
            /*case R.id.sign_out_button:
                    signOut();
                    break;*/
        }
    }
    //Login Screen
    public void login(View view) {
            Intent intent = new Intent(Registration.this,Login.class);
            //Toasty.info(getApplicationContext(),"Login Scrren On!!",Toasty.LENGTH_SHORT).show();
            startActivity(intent);
    }

    //Database JsonObject
    public void DatabaseRegistration(final Boolean flag){
        final String email_string = email.getText().toString();
        final String name_string = name.getText().toString();
        final String password_string = password.getText().toString();
        final String phone_string = mPhoneNumberField.getText().toString();
        final String hostelname_string=hostelname.getText().toString();
        final String hostel_add_string=hosteladd.getText().toString();



        class LoginData extends AsyncTask<Void, Void, String> {
                ProgressDialog pdLoading = new ProgressDialog(Registration.this);
                @Override
                    protected void onPreExecute() {

                        super.onPreExecute();
                        //this method will be running on UI thread
                        if(flag==true) {
                            pdLoading.setMessage("\tLoading...");
                            pdLoading.setCancelable(false);
                            pdLoading.show();
                        }else{
                            //Toasty.error(getApplicationContext(), "Loading oFf" , Toasty.LENGTH_LONG).show();
                        }
                    }
                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();
                   //creating request parameters
                    /*HashMap<String, String> params = new HashMap<>();
                    params.put("email", email_string);
                    params.put("password", password_string);
                    params.put("username", name_string);
                    params.put("gender",phone_string);
                    params.put("UserType","Abhishek");
                    params.put("company_Name","gh");*/
                    HashMap<String, String> params = ar.databaseEntry(email_string, password_string, name_string, phone_string,hostelname_string,hostel_add_string);
                    //returing the response
                    return requestHandler.sendPostRequest(URL_REGISTER, params);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pdLoading.dismiss();

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    //if no error in response
                    String reponse = obj.getString("message");
                    if (!obj.getBoolean("error")) {

                        if(reponse.equals("User Registered Successfully")){
                            Toasty.success(getApplicationContext(), reponse , Toasty.LENGTH_LONG).show();
                            Intent intent = new Intent(Registration.this,Login.class);
                            startActivity(intent);
                        }else if(reponse.equals("Cannot complete user registration")){
                            Toasty.error(getApplicationContext(), reponse , Toasty.LENGTH_LONG).show();
                        }else{
                            Toasty.error(getApplicationContext(), reponse , Toasty.LENGTH_LONG).show();
                        }

                        Intent intent = new Intent(Registration.this,Login.class);
                        startActivity(intent);
                        //Toasty.success(getApplicationContext(),"Registration Successful!",Toasty.LENGTH_SHORT).show();
                        //startActivity(new Intent(getApplicationContext(), Mobile_varification.class));
                    }
                    else{
                        if(flag==true){
                        //Toast.makeText(getApplicationContext(), obj.getString("Something Wrong"), Toast.LENGTH_LONG).show();
                        Toasty.error(getApplicationContext(),"Something Went Wrong! Please Fill All Details",Toasty.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(RegisterActivity.this, , Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), "Exception: " + e, Toast.LENGTH_LONG).show();
                    //Toasty.error(getApplicationContext(),"Invalid Email_id OR Password!",Toasty.LENGTH_SHORT).show();
                    Toasty.error(getApplicationContext(),"Catch Block"+e.getMessage(),Toasty.LENGTH_SHORT).show();
                }
            }
        }

        // Intent intent = new Intent(Registration.this, Registration.class);
        //startActivity(intent);


        LoginData login = new LoginData();
        login.execute();
    }




}