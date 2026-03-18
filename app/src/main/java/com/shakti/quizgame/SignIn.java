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
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignIn extends AppCompatActivity {

    EditText Email,Password;
    GoogleSignInClient googleSignInClient;
    Button signIn;
    TextView signUp,forgot;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    ActivityResultLauncher<Intent> activityResultLauncher;

    ProgressBar progressBar;
    SignInButton signInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        registerActivityForGoogleSignIn();
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Email=findViewById(R.id.email);
        signInButton=findViewById(R.id.signIngoogleButton);
        Password=findViewById(R.id.password);
        signUp=findViewById(R.id.signUp);
        signIn=findViewById(R.id.signIn);
        forgot=findViewById(R.id.forgot);
        progressBar=findViewById(R.id.progressBar);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignIn.this,Forgot.class);
                startActivity(intent);
                finish();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=Email.getText().toString().trim();
                String password=Password.getText().toString().trim();
                if(email.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(SignIn.this, "Enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                signInwithFirebase(email,password);

            }

            private void signInwithFirebase(String email, String password) {
                progressBar.setVisibility(View.VISIBLE);
                signIn.setEnabled(false);
                auth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                signIn.setEnabled(true);
                                if (task.isSuccessful()) {
                                    FirebaseUser user = auth.getCurrentUser();
                                    if (user != null) {

                                        Toast.makeText(SignIn.this, "Sign in successful!", Toast.LENGTH_LONG).show();
                                        Intent intent=new Intent(SignIn.this,MainMenu.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                } else {
                                    String message = task.getException() != null
                                            ? task.getException().getMessage()
                                            : "Authentication failed.";
                                    Toast.makeText(SignIn.this, message, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

    }
    private void signInWithGoogle() {
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        signIn();



    }

    @Override
    protected void onStart() {
            super.onStart();
        FirebaseUser user=auth.getCurrentUser();
        if (user != null) {
            Intent intent=new Intent(SignIn.this,MainMenu.class);
            startActivity(intent);
            finish();
        }
    }

    public void signIn()
    {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        activityResultLauncher.launch(signInIntent);
    }
    public void registerActivityForGoogleSignIn()
    {
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                int resultCode=result.getResultCode();
                Intent data=result.getData();

                if(resultCode==RESULT_OK&&data!=null)
                {
                    Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
                    firebaseAuthWithGoogle(task);

                }

            }

            private void firebaseAuthWithGoogle(Task<GoogleSignInAccount> task)  {
                try {
                GoogleSignInAccount account =task.getResult(ApiException.class);
                firebaseSignInWithGoogle(account);
                } catch (ApiException e) {
                    Toast.makeText(SignIn.this, "Google sign-in failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void firebaseSignInWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if(user!=null)
                        {
                            Toast.makeText(this, "Google sign-in successful", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(SignIn.this,MainMenu.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(this, "Firebase Authentication failed.", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
