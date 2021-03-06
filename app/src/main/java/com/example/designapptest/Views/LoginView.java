package com.example.designapptest.Views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.designapptest.Controller.UserController;
import com.example.designapptest.Model.Device;
import com.example.designapptest.Model.UserModel;
import com.example.designapptest.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.dialogs.signal.DialogPropertiesSignal;
import org.imaginativeworld.oopsnointernet.dialogs.signal.NoInternetDialogSignal;

import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginView extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener
        , FirebaseAuth.AuthStateListener {

    public static int REQUEST_CODE_LOGIN_WITH_GOOGLE = 99;
    //Bi???n ki???m tra xem ??ang login ki???u n??o: google, facebook, t??i kho???n app
    public static int CHECK_TYPE_PROVIDER_LOGIN = 0;
    public static int CODE_PROVIDER_LOGIN_WITH_GOOGLE = 1;
    public static int CODE_PROVIDER_LOGIN_WITH_FACEBOOK = 2;
    private static final int NOTIFICATION_ID = 11;
    public static final String SHARE_UID = "currentUserId";
    public static final String IS_ADMIN = "isAdmin";
    public static final String PREFS_DATA_NAME = "currentUserId";

    ImageButton btnLoginWithGoogle;
    GoogleApiClient apiClient;
    FirebaseAuth firebaseAuth;
    UserController userController;

    Button btn_signUp;
    Button btn_login;

    EditText edt_username_login;
    EditText edt_password_login;
    TextView tvForgotPassword, errgioitinh;

    ProgressDialog progressDialog;
    SweetAlertDialog pDialog;
    SharedPreferences sharedPreferences;

    DatabaseReference nodeRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Kh???i t???o firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        //Text ????ng xu???t
//        firebaseAuth.signOut();
        userController = new UserController(this);
        // L??u m?? user ????ng nh???p v??o app
        sharedPreferences = getSharedPreferences(PREFS_DATA_NAME, MODE_PRIVATE);
        errgioitinh = findViewById(R.id.errgioitinh);
        btnLoginWithGoogle = (ImageButton) findViewById(R.id.btnImg_google_login);
        btn_signUp = (Button) findViewById(R.id.btn_signUp);
        btn_login = (Button) findViewById(R.id.btn_login);
        edt_username_login = (EditText) findViewById(R.id.edt_username_login);
        edt_password_login = (EditText) findViewById(R.id.edt_password_login);
        tvForgotPassword = (TextView) findViewById(R.id.tv_forgot_password);
        isOnline();


        btnLoginWithGoogle.setOnClickListener(this);
        btn_signUp.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        edt_username_login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null) {
                    errgioitinh.setVisibility(View.GONE);
                }
            }
        });

        edt_password_login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    if (s.toString() != null) {
                        errgioitinh.setVisibility(View.GONE);
                    }
            }
        });

        CreateClientLoginWithGoogle();
        /*ClickForgotPassword();*/
        nodeRoot = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Th??m s??? ki???n listenerStateChange
        firebaseAuth.addAuthStateListener(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            pDialog.show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //X??a s??? ki???n ListenerStateChange
        firebaseAuth.removeAuthStateListener(this);
    }

    //T???o client ????ng nh???p b???ng google
    private void CreateClientLoginWithGoogle() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //T???o ra sign client
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();
    }
    //end T???o client ????ng nh???p b???ng google

    //????ng nh???p v??o t??i kho???n google
    private void LoginGoogle(GoogleApiClient apiClient) {
        //set code
        CHECK_TYPE_PROVIDER_LOGIN = CODE_PROVIDER_LOGIN_WITH_GOOGLE;
        Intent ILoginGoogle = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        //Hi???n th??? client google ????? ????ng nh???p
        startActivityForResult(ILoginGoogle, REQUEST_CODE_LOGIN_WITH_GOOGLE);
    }
    //end ????ng nh???p v??o t??i kho???n google

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Ki???m tra n???u resultcode tr??? v??? l?? c???a client Login with google

        if (requestCode == REQUEST_CODE_LOGIN_WITH_GOOGLE) {


            if (resultCode == RESULT_OK) {

                GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                //L???y ra account google ???????c ????ng nh???p
                GoogleSignInAccount account = signInResult.getSignInAccount();
                //L???y ra token c???a account google
                String tokenID = account.getIdToken();
                firebaseAuthWithGoogle(tokenID);

            }

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserModel userModel = new UserModel();
                            userModel.setName(user.getDisplayName());
                            userModel.setEmail(user.getEmail());
                            userModel.setAvatar(""+user.getPhotoUrl());
                            userModel.setGender(true);
                            userModel.setOwner(false);
                            nodeRoot = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
                            nodeRoot.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                                    String phone = dataSnapshot.child("phoneNumber").getValue(String.class);
                                    userModel.setPhoneNumber(phone);
                                    String uid = task.getResult().getUser().getUid();
                                    userController.addUser(userModel, uid);
                                    CheckLoginFirebase(idToken);
                                }
                                @Override
                                public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginView.this, "Sorry auth failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //L???y token id v?? ????ng nh???p v??o firebase
    private void CheckLoginFirebase(String tokenID) {
        if (CHECK_TYPE_PROVIDER_LOGIN == CODE_PROVIDER_LOGIN_WITH_GOOGLE) {
            AuthCredential authCredential = GoogleAuthProvider.getCredential(tokenID, null);
            //SignIn to firebase
            firebaseAuth.signInWithCredential(authCredential);
        }
    }
    //end L???y token id v?? ????ng nh???p v??o firebase

    public void isOnline() {
        NoInternetDialogSignal.Builder builder = new NoInternetDialogSignal.Builder(
                this,
                getLifecycle()
        );

        DialogPropertiesSignal properties = builder.getDialogProperties();

        properties.setConnectionCallback(new ConnectionCallback() { // Optional
            @Override
            public void hasActiveConnection(boolean hasActiveConnection) {
                // ...
            }
        });

        properties.setCancelable(false); // Optional
        properties.setNoInternetConnectionTitle("Kh??ng c?? internet!"); // Optional
        properties.setNoInternetConnectionMessage("??i kh??ng! ng?????i ngo??i h??nh tinh ???? c???n tr??? k???t n???i c???a ch??ng ta, h??y ki???m tra v?? th??? l???i!"); // Optional
        properties.setShowInternetOnButtons(true); // Optional
        properties.setPleaseTurnOnText("H??y b???t ch???c n??ng internet c???a b???n l??n"); // Optional
        properties.setWifiOnButtonText("Wifi"); // Optional
        properties.setMobileDataOnButtonText("Mobile data"); // Optional

        properties.setOnAirplaneModeTitle("Kh??ng c?? internet!"); // Optional
        properties.setOnAirplaneModeMessage("??i kh??ng! ng?????i ngo??i h??nh tinh ???? c???n tr??? k???t n???i c???a ch??ng ta, h??y ki???m tra v?? th??? l???i!"); // Optional
        properties.setPleaseTurnOffText("C?? ph???i b???n ch??a t???t ch??? ????? m??y bay?"); // Optional
        properties.setAirplaneModeOffButtonText("T???t ch??? ????? m??y bay"); // Optional
        properties.setShowAirplaneModeOffButtons(true); // Optional

        builder.build();

    }
    //Showing the No internet connection Custom Dialog =)

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void login() {
        String username = edt_username_login.getText().toString();
        String password = edt_password_login.getText().toString();
        Boolean check = true;
        if (username.trim().length() == 0) {
            edt_username_login.setError("Kh??ng ????? tr???ng");
            check = false;
        } else if(!Pattern.matches("^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$", username)){
            edt_username_login.setError("Email sai ?????nh d???ng!");
            check = false;
        }
        if (password.trim().length() == 0) {
            edt_password_login.setError("Kh??ng ????? tr???ng");
            check = false;
        }
        if (check == true) {
            pDialog.show();

            firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        pDialog.dismiss();
                        errgioitinh.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnImg_google_login:
                LoginGoogle(apiClient);
                break;
            case R.id.btn_signUp:
                Intent iSignup = new Intent(LoginView.this, SignUpView.class);
                startActivity(iSignup);
                break;
            case R.id.btn_login:
                login();
                break;
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            checkAdminLogin(user.getUid());
        } else {

        }
    }

    private void checkAdminLogin(String UID) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean isAdmin = false;

                // L??u l???i m?? user ????ng nh???p v??o app
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(SHARE_UID, UID);

//                for(DataSnapshot adminValue : dataSnapshot.getChildren()) {
                    if (UID.equals("cwEEwMym4lP5W8A6dw7JEHz6EyL2")) {
                        isAdmin = true;
                        pDialog.dismiss();

                        editor.putBoolean(IS_ADMIN, isAdmin);
                        editor.commit();

                        Toast.makeText(LoginView.this, "????ng nh???p th??nh c??ng", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), adminView.class);
                        startActivity(intent);
                        finishAffinity();
                      return;
                    }
//                }

                //Load trang ch???
                //Intent intent = new Intent(this, MainActivity.class);
//                if(isAdmin == false) {
                pDialog.dismiss();
                    editor.putBoolean(IS_ADMIN, isAdmin);
                    editor.commit();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    return;
                }


                nodeRoot = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
                nodeRoot.child("phoneNumber").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        Object phone = task.getResult().getValue();
                        if (phone == null) {
                            Intent intent = new Intent(getApplicationContext(), AuthPhoneUserNewOTP.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {

                            Toast.makeText(LoginView.this, "????ng nh???p th??nh c??ng", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Main_Menu.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        nodeRoot.child("Admins").addListenerForSingleValueEvent(valueEventListener);
    }

    public void ClickForgotPassword(View v) {

        Intent intent = new Intent(LoginView.this, resetPasswordByEmail.class);
        startActivity(intent);

    }
}