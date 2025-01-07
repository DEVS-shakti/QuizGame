package com.shakti.quizgame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Forgot extends AppCompatActivity {
    EditText email;
    Button reset;
    TextView back;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        back=findViewById(R.id.back);
        progressBar=findViewById(R.id.progressBar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Forgot.this,SignIn.class);
                startActivity(intent);
                finish();
            }
        });
        email=findViewById(R.id.email);
        reset=findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserEmail=email.getText().toString();
                if(UserEmail.isEmpty())
                {
                    Toast.makeText(Forgot.this, "Enter email to get reset link", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.sendPasswordResetEmail(UserEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBar.setVisibility(View.VISIBLE);
                                reset.setText("");
                                new Handler().postDelayed(new Runnable(){
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.GONE);
                                        reset.setText("Get Email");
                                    }
                                }, 1000);
                                if(task.isSuccessful())
                                {
                                    FirebaseUser user=auth.getCurrentUser();
                                    if (user != null) {
                                        Toast.makeText(Forgot.this,"Reset email sent",Toast.LENGTH_SHORT).show();

                                    }else{
                                        Toast.makeText(Forgot.this, "Email not exist", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(Forgot.this, "Create an account", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }
                        });
            }
        });
    }
}