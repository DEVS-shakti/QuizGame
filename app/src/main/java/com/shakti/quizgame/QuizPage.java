package com.shakti.quizgame;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class QuizPage extends AppCompatActivity {

    TextView oA, oB, oC, oD, quiz, no;
    CountDownTimer c;
    String A, B, C, D;
    int score;

    int quizCount, quizCurrent = 1;
    Button next;
    ProgressBar progressBar;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    DatabaseReference users = firebaseDatabase.getReference().child("users").child(FirebaseAuth.getInstance().getUid());
    String question;
    String answer;
    int possition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        possition = getIntent().getIntExtra("test", 1);
        loadQuestion(possition);
        setContentView(R.layout.activity_quiz_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        LinearLayout ly = findViewById(R.id.ly);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ly.setVisibility(View.GONE);
            }
        }, 1800);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.cancel();
                startActivity(new Intent(QuizPage.this, TestActivity.class));
                finish();
            }
        });
        no = findViewById(R.id.no);
        progressBar = findViewById(R.id.progress);
        next = findViewById(R.id.next);
        oA = findViewById(R.id.oA);
        oB = findViewById(R.id.oB);
        oC = findViewById(R.id.oC);
        oD = findViewById(R.id.oD);
        quiz = findViewById(R.id.quiz);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizCurrent++;
                c.cancel();
                reset();
                loadQuestion(possition);
            }


        });

    }

    private void loadQuestion(int index) {
        DatabaseReference reference = firebaseDatabase.getReference().child("QuestionsParent").child("Questions" + String.valueOf(index + 1));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                quizCount = (int) dataSnapshot.getChildrenCount();
                if (quizCount <= quizCurrent) {
                    Toast.makeText(QuizPage.this, "Question khatam hogya", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(QuizPage.this, Result.class);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    finish();
                    return;
                }
                question = dataSnapshot.child(String.valueOf(quizCurrent)).child("question").getValue().toString();
                A = dataSnapshot.child(String.valueOf(quizCurrent)).child("optionA").getValue().toString();
                B = dataSnapshot.child(String.valueOf(quizCurrent)).child("optionB").getValue().toString();
                C = dataSnapshot.child(String.valueOf(quizCurrent)).child("optionC").getValue().toString();
                D = dataSnapshot.child(String.valueOf(quizCurrent)).child("optionD").getValue().toString();
                String optionAns = dataSnapshot.child(String.valueOf(quizCurrent)).child("answer").getValue().toString();
                answer = dataSnapshot.child(String.valueOf(quizCurrent)).child("option" + optionAns).getValue().toString();
                quiz.setText("Q." + question);
                oA.setText("A." + A);
                oB.setText("B." + B);
                oC.setText("C." + C);
                oD.setText("D." + D);
                no.setText(String.valueOf(quizCurrent) + "/10");
                setProgressBar(progressBar);
                checkListener(oA, "A.");
                checkListener(oB, "B.");
                checkListener(oC, "C.");
                checkListener(oD, "D.");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(QuizPage.this, "Failed to load quiz", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setProgressBar(ProgressBar progressBar) {
        c = new CountDownTimer(60 * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int progress = (int) ((60 * 1000 - millisUntilFinished) / 1000);
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                Dialog dialog = new Dialog(QuizPage.this);
                dialog.setContentView(R.layout.alert);
                Button cancel = dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button next = dialog.findViewById(R.id.save);
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quizCurrent++;
                        loadQuestion(possition);
                        dialog.dismiss();
                    }
                });
                dialog.show();


            }
        }.start();
    }

    public void checkListener(TextView o, String op) {
        o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = o.getText().toString();
                if (txt.equals(op + answer)) {
                    o.setBackgroundColor(Color.GREEN);
                    score++;
                    users.child("score").setValue(score);
                    Toast.makeText(QuizPage.this, "score" + String.valueOf(score), Toast.LENGTH_SHORT).show();
                    o.setClickable(false);
                } else {
                    o.setBackgroundColor(Color.RED);
                    correctFlag(oA, "A.");
                    correctFlag(oB, "B.");
                    correctFlag(oC, "C.");
                    correctFlag(oD, "D.");

                }
                oA.setClickable(false);
                oB.setClickable(false);
                oC.setClickable(false);
                oD.setClickable(false);

            }


            private void correctFlag(TextView o, String op) {
                String txt = o.getText().toString();
                if (txt.equals(op + answer)) {
                    o.setBackgroundColor(Color.GREEN);
                }
            }
        });
    }

    private void reset() {
        oA.setBackgroundColor(Color.WHITE);
        oB.setBackgroundColor(Color.WHITE);
        oC.setBackgroundColor(Color.WHITE);
        oD.setBackgroundColor(Color.WHITE);
        oA.setClickable(true);
        oB.setClickable(true);
        oC.setClickable(true);
        oD.setClickable(true);
    }

}