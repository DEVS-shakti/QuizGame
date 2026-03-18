package com.shakti.quizgame;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    EditText Email,Password;
    Button signUp;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    TextView signIn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        signIn=findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
                finish();
            }
        });
        Email=findViewById(R.id.email);
        Password=findViewById(R.id.password);
        signUp=findViewById(R.id.signUp);
        progressBar=findViewById(R.id.progressBar);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=Email.getText().toString().trim();
                String password=Password.getText().toString().trim();
                if(email.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(SignUp.this, "Enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(SignUp.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }


                signUpwithFirebase(email,password);

            }

            private void signUpwithFirebase(String email, String password) {
             progressBar.setVisibility(View.VISIBLE);
             signUp.setEnabled(false);
             auth.createUserWithEmailAndPassword(email,password)
                     .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             progressBar.setVisibility(View.GONE);
                             signUp.setEnabled(true);

                             if (task.isSuccessful()) {
                                 FirebaseUser user = auth.getCurrentUser();
                                 if (user != null) {
                                     DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
                                     database.child(user.getUid()).setValue(new User(getName(email),email,0,false));
                                     Toast.makeText(SignUp.this, "User Data saved", Toast.LENGTH_SHORT).show();
                                     Toast.makeText(SignUp.this, "Sign Up Successful!", Toast.LENGTH_LONG).show();
                                     Intent intent=new Intent(SignUp.this, MainMenu.class);
                                     startActivity(intent);
                                     finish();
                                 }
                             } else {
                                 String message = task.getException() != null
                                         ? task.getException().getMessage()
                                         : "Authentication failed.";
                                 Toast.makeText(SignUp.this, message, Toast.LENGTH_LONG).show();
                             }
                         }
                     });
            }
        });
    }
    public String getName(String email)
    {

        // Remove "@gmail.com" from email
        if (email != null && email.contains("@")) {
            return email.split("@")[0];
        }
        return email;
    }
}
