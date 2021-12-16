package com.example.designapptest.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.designapptest.R;

public class FeedbackScreen extends AppCompatActivity {
    RatingBar ratingBar2;
    TextView sao;
    EditText text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ratingBar2 = findViewById(R.id.ratingBar2);
        text = findViewById(R.id.text);
        sao = findViewById(R.id.sao);
        ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar , float rating , boolean fromUser) {
                if (rating==0 || rating==0.5){
                    sao.setText("Rất tệ!");
                }else if (rating ==1 || rating==1.5){
                    sao.setText("Hơi tệ!");
                }else if (rating ==2 ||rating==2.5){
                    sao.setText("Kém!");
                }else if (rating ==3 || rating==3.5){
                    sao.setText("Tốt!");
                }else if (rating==4 || rating==4.5){
                    sao.setText("Khá tốt!");
                }else {
                    sao.setText("Rất tốt!");
                }
            }
        });
        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("");
                Toast.makeText(FeedbackScreen.this, "Cám ơn bạn đã feedback cho chung tôi", Toast.LENGTH_SHORT).show();
            }
        });
    }
}