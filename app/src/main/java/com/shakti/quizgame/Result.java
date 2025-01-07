package com.shakti.quizgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Result extends AppCompatActivity {

    ProgressBar progressBar;
    TextView wish,ca,wa,back,Score;
    Button backbtn,rank;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    DatabaseReference users=firebaseDatabase.getReference().child("users").child(FirebaseAuth.getInstance().getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Result.this,QuizPage.class));
                finish();
            }
        });
        progressBar=findViewById(R.id.progress_bar);
        wish=findViewById(R.id.wish);
        ca=findViewById(R.id.ca);
        wa=findViewById(R.id.wa);
        back=findViewById(R.id.back);
        Score=findViewById(R.id.tv);
        backbtn=findViewById(R.id.backbtn);
        rank=findViewById(R.id.rank);
        rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Result.this, LeaderBoard.class);
                startActivity(intent);
                finish();
            }
        });
        setProgress();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Result.this,TestActivity.class);
                startActivity(intent);
                finish();
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(Result.this,MainMenu.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void setProgress() {
        int score=getIntent().getIntExtra("score",0);
        String CA=String.valueOf(score);
        String WA=String.valueOf(10-score);
        Score.setText(CA+"/10");
        progressBar.setProgress(score);
        ca.setText("CorrectAnswer:"+CA);
        wa.setText("WrongAnswer:"+WA);
    }




}