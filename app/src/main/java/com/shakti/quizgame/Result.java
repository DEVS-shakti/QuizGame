package com.shakti.quizgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class Result extends AppCompatActivity {

    ProgressBar progressBar;
    TextView wish,ca,wa,Score;
    View back;
    Button backbtn,rank;

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
        if (score < 0) {
            score = 0;
        } else if (score > 10) {
            score = 10;
        }
        String CA=String.valueOf(score);
        String WA=String.valueOf(10-score);
        Score.setText(CA+"/10");
        progressBar.setProgress(score);
        ca.setText("Correct: " + CA);
        wa.setText("Wrong: " + WA);
        if (score >= 8) {
            wish.setText("Excellent work");
        } else if (score >= 5) {
            wish.setText("Good effort");
        } else {
            wish.setText("Keep practicing");
        }
    }




}
