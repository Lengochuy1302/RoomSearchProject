package com.example.designapptest.Views;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.designapptest.Controller.UserController;
import com.example.designapptest.Model.UserModel;
import com.example.designapptest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.dialogs.signal.DialogPropertiesSignal;
import org.imaginativeworld.oopsnointernet.dialogs.signal.NoInternetDialogSignal;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SignUpView extends AppCompatActivity implements View.OnClickListener {
    Button btn_signup, guilai, btn_signUp_Activity, btn_signUp;
    FirebaseAuth firebaseAuth;
    EditText edt_email_signUp, edt_password_signUp, edt_retype_password_signUp, edt_name_signUp, edt_phone_signUp, nhapotp, verysdt;
    RadioButton rad_gender_female_signUp, rad_gender_male_signUp;
    ProgressDialog progressDialog;

    TextView errgioitinh, vvvvv;
    LinearLayout layougioitinh, linersendotp;
    private String textverificationId;
    UserController userController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.signup_view);
        firebaseAuth = FirebaseAuth.getInstance();
        errgioitinh = findViewById(R.id.errgioitinh);
        layougioitinh = (LinearLayout) findViewById(R.id.layougioitinh);
        isOnline();
        btn_signup = (Button) findViewById(R.id.btn_signUp);
        edt_email_signUp = (EditText) findViewById(R.id.edt_email_signUp);
        edt_password_signUp = (EditText) findViewById(R.id.edt_password_signUp);
        edt_retype_password_signUp = (EditText) findViewById(R.id.edt_retype_password_signUp);
        edt_name_signUp = (EditText) findViewById(R.id.edt_name_signUp);
        edt_phone_signUp = (EditText) findViewById(R.id.edt_phone_signUp);
        rad_gender_female_signUp = (RadioButton) findViewById(R.id.rad_gender_female_signUp);
        rad_gender_male_signUp = (RadioButton) findViewById(R.id.rad_gender_male_signUp);
        guilai = findViewById(R.id.guilai);
        vvvvv = findViewById(R.id.vvvv);
        nhapotp = findViewById(R.id.nhapotp);
        linersendotp = findViewById(R.id.linersendotp);
        btn_signUp = findViewById(R.id.btn_signUp);
        btn_signUp_Activity = findViewById(R.id.btn_signUp_Activity);
        progressDialog = new ProgressDialog(SignUpView.this, R.style.MyProgessDialogStyle);
        userController = new UserController(this);
        rad_gender_female_signUp.setChecked(true);
        btn_signup.setOnClickListener(this);
        rad_gender_female_signUp.setOnClickListener(this);
        rad_gender_male_signUp.setOnClickListener(this);
        requestSMSPermission();
        doccode();
        edt_retype_password_signUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null) {
                    layougioitinh.setVisibility(View.VISIBLE);
                    edt_name_signUp.setVisibility(View.VISIBLE);
                    edt_phone_signUp.setVisibility(View.VISIBLE);
                    vvvvv.setVisibility(View.VISIBLE);
                } else if (s.toString().equals("")){
                    layougioitinh.setVisibility(View.GONE);
                    edt_name_signUp.setVisibility(View.GONE);
                    edt_phone_signUp.setVisibility(View.GONE);
                    vvvvv.setVisibility(View.GONE);
                }

            }
        });

    }

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
        properties.setNoInternetConnectionTitle("Không có internet!"); // Optional
        properties.setNoInternetConnectionMessage("Ôi không! người ngoài hành tinh đã cản trở kết nối của chúng ta, hãy kiểm tra và thử lại!"); // Optional
        properties.setShowInternetOnButtons(true); // Optional
        properties.setPleaseTurnOnText("Hãy bật chức năng internet của bạn lên"); // Optional
        properties.setWifiOnButtonText("Wifi"); // Optional
        properties.setMobileDataOnButtonText("Mobile data"); // Optional

        properties.setOnAirplaneModeTitle("Không có internet!"); // Optional
        properties.setOnAirplaneModeMessage("Ôi không! người ngoài hành tinh đã cản trở kết nối của chúng ta, hãy kiểm tra và thử lại!"); // Optional
        properties.setPleaseTurnOffText("Có phải bạn chưa tắt chế độ máy bay?"); // Optional
        properties.setAirplaneModeOffButtonText("Tắt chế độ máy bay"); // Optional
        properties.setShowAirplaneModeOffButtons(true); // Optional

        builder.build();

    }

    private void requestSMSPermission()
    {
        String permission = Manifest.permission.RECEIVE_SMS;

        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED)
        {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(SignUpView.this, permission_list,56);
        }
    }

    private void signUp() {
        String email = edt_email_signUp.getText().toString().trim();
        String password = edt_password_signUp.getText().toString().trim();
        String passwordRetype = edt_retype_password_signUp.getText().toString().trim();
        final String name = edt_name_signUp.getText().toString().trim();
        final String avatar = "user.png";
        final Boolean owner = false;
        final String phone = edt_phone_signUp.getText().toString().trim();
        Boolean gender = true;
        String verifyphone = phone.substring(1,10) ;

        if (rad_gender_male_signUp.isChecked()) {
            gender = false;
        }

        final Boolean genderUser = gender;
        Boolean check = true;
        if (email.trim().length() == 0) {
            edt_email_signUp.setError("Không để trống");
            check = false;
        } else if(!Pattern.matches("^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$", email)){
            edt_email_signUp.setError("Email sai định dạng!");
            check = false;
        }
        if (password.trim().length() == 0) {
            edt_password_signUp.setError("Không để trống");
            check = false;
        }
        if (passwordRetype.trim().length() == 0) {
            edt_retype_password_signUp.setError("Không để trống");
            check = false;
        } else if (!passwordRetype.equals(password)) {
            edt_retype_password_signUp.setError("Không đúng");
            check = false;
        }
        if (phone.trim().length() == 0) {
            edt_phone_signUp.setError("Không để trống");
            check = false;
        } else if(!Pattern.matches("0\\d{3}\\d{3}\\d{3}", phone)){
            edt_phone_signUp.setError("Sai định dạng");
            check = false;
        }
        if (name.trim().length() == 0) {
            edt_name_signUp.setError("Không để trống");
            check = false;
        }


        if (check == true) {
            btn_signUp.setVisibility(View.GONE);
            btn_signUp_Activity.setVisibility(View.VISIBLE);
            linersendotp.setVisibility(View.VISIBLE);
            guilai.isEnabled();
            sendVerificationCodeToUser("+84"+verifyphone);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    new CountDownTimer(60000 + 100, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            guilai.setText("00:"+ millisUntilFinished/1000);
                        }

                        @Override
                        public void onFinish() {
                            guilai.setText("Gửi lại");
                            guilai.setEnabled(true);
                        }
                    }.start();

                }
            },1000);

            btn_signUp_Activity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean checkError = true;
                    if(verysdt.getText().toString().trim().isEmpty()){
                        verysdt.setError("Mã OTP không được để trống");
                        checkError = false;

                    }

                    if(checkError) {
                        btn_signUp_Activity.setEnabled(false);
                        String OTPcode = verysdt.getText().toString().trim();
                        verifyCode(OTPcode);
                    }
                }
            });

            guilai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendVerificationCodeToUserAgain("+84"+verifyphone);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            new CountDownTimer(60000 + 100, 1000) {

                                @Override
                                public void onTick(long millisUntilFinished) {
                                    guilai.setText("00:"+ millisUntilFinished/1000);
                                }

                                @Override
                                public void onFinish() {
                                    guilai.setText("Gửi lại");
                                    guilai.setEnabled(true);
                                }
                            }.start();

                        }
                    },1000);
                }
            });

        }
    }


    // gửi otp
    private void sendVerificationCodeToUser(String phoneNo) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential credential) {
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                Toast.makeText(SignUpView.this, "Xác thực không thành công",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                textverificationId = "";
                                textverificationId = verificationId;
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    //gửi lại otp
    private void sendVerificationCodeToUserAgain(String phoneNo) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential credential) {
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                Toast.makeText(SignUpView.this, "Xác thực không thành công",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                textverificationId = "";
                                textverificationId = verificationId;
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    //callback otp

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(textverificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void doccode(){
        verysdt = (EditText) findViewById(R.id.nhapotp);
        new OTP_Receiver().setEditText(verysdt);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Đang tạo tài khoản!");
        pDialog.setCancelable(false);
        pDialog.show();
        String email = edt_email_signUp.getText().toString().trim();
        String password = edt_password_signUp.getText().toString().trim();
        final String name = edt_name_signUp.getText().toString().trim();
        final String avatar = "user.png";
        final Boolean owner = false;
        final String phone = edt_phone_signUp.getText().toString().trim();
        Boolean gender = true;

        if (rad_gender_male_signUp.isChecked()) {
            gender = false;
        }

        final Boolean genderUser = gender;
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UserModel userModel = new UserModel();
                    userModel.setName(name);
                    userModel.setEmail(email);
                    userModel.setAvatar(avatar);
                    userModel.setGender(genderUser);
                    userModel.setOwner(owner);
                    userModel.setPhoneNumber(phone);

                    String uid = task.getResult().getUser().getUid();

                    userController.addUser(userModel, uid);

                    progressDialog.dismiss();
                    new SweetAlertDialog(SignUpView.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Đăng kí thành công")
                            .show();
                    Intent iSignin = new Intent(SignUpView.this, LoginView.class);
                    startActivity(iSignin);
                } else {
                    progressDialog.dismiss();
                    new SweetAlertDialog(SignUpView.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Đăng kí thất bại!")
                            .show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_signUp:
                signUp();
                break;
            case R.id.rad_gender_female_signUp:
                rad_gender_male_signUp.setChecked(false);
                break;
            case R.id.rad_gender_male_signUp:
                rad_gender_female_signUp.setChecked(false);
                break;
        }
    }
}
