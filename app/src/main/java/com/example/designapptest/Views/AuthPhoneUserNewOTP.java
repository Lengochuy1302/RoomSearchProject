package com.example.designapptest.Views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.designapptest.Controller.UserController;
import com.example.designapptest.R;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AuthPhoneUserNewOTP extends AppCompatActivity {
        Button btn_login,guilai, xacminh;
        EditText sdtotp, nhapotp;
        LinearLayout linersendotp;
    FirebaseAuth firebaseAuth;
    private String textverificationId;
    DatabaseReference nodeRoot;
    UserController userController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_phone_user_new_otp);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        btn_login = findViewById(R.id.btn_login);
        sdtotp = findViewById(R.id.sdtotp);
        firebaseAuth = FirebaseAuth.getInstance();
        linersendotp = findViewById(R.id.linersendotp);
        guilai = findViewById(R.id.guilai);
        xacminh = findViewById(R.id.xacminh);
        nhapotp = findViewById(R.id.nhapotp);
        doccode();
        xacMinh();
        requestSMSPermission();
    }

    private void requestSMSPermission()
    {
        String permission = Manifest.permission.RECEIVE_SMS;

        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED)
        {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(AuthPhoneUserNewOTP.this, permission_list,56);
        }
    }

    private void xacMinh() {
        xacminh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = sdtotp.getText().toString().trim();
                Boolean check = true;
                if (phone.trim().length() == 0) {
                    sdtotp.setError("Không để trống");
                    check = false;
                } else if(!Pattern.matches("0\\d{3}\\d{3}\\d{3}", phone)){
                    sdtotp.setError("Sai định dạng");
                    check = false;
                }

                if (check == true) {
                    xacminh.setVisibility(View.GONE);
                    btn_login.setVisibility(View.VISIBLE);
                    linersendotp.setVisibility(View.VISIBLE);
                    guilai.isEnabled();
                    guilai.isEnabled();
                    String verifyphone = phone.substring(1,10) ;
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

                    btn_login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Boolean checkError = true;
                            if(nhapotp.getText().toString().trim().isEmpty()){
                                nhapotp.setError("Mã OTP không được để trống");
                                checkError = false;

                            }

                            if(checkError) {
                                btn_login.setEnabled(false);
                                String OTPcode = nhapotp.getText().toString().trim();
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
        });
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
                                Toast.makeText(AuthPhoneUserNewOTP.this, "Xác thực không thành công",Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(AuthPhoneUserNewOTP.this, "Xác thực không thành công",Toast.LENGTH_SHORT).show();
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
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    textverificationId = "";
                    textverificationId = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(AuthPhoneUserNewOTP.this, "Xác thực không thành công",Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(textverificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void doccode(){
        nhapotp = (EditText) findViewById(R.id.nhapotp);
        new OTP_Receiver().setEditText(nhapotp);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Đang hoàn tất thiết lập!");
        pDialog.setCancelable(false);
        pDialog.show();
        String phone = sdtotp.getText().toString().trim();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            return;
        }

        nodeRoot = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
        nodeRoot.child("phoneNumber").setValue(phone);
                    new SweetAlertDialog(AuthPhoneUserNewOTP.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Thiết lập tài khoản thành công!")
                            .show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent iSignin = new Intent(AuthPhoneUserNewOTP.this, Main_Menu.class);
                            startActivity(iSignin);
                        }
                    },1000);


    }
}