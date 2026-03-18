package com.shakti.quizgame;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainMenu extends AppCompatActivity {

    Button start;
    TextView name, score, coin;
    EditText edName, edEmail;
    boolean updated;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(MainMenu.this, SignIn.class));
            finish();
            return;
        }
        users = firebaseDatabase.getReference("users").child(currentUser.getUid());

        findViewById(R.id.leaderBoard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this,LeaderBoard.class));
                finish();
            }
        });
        LinearLayout ly=findViewById(R.id.ly);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ly.setVisibility(View.GONE);
            }
        },1000);
        start=findViewById(R.id.startquiz);
        name=findViewById(R.id.name);
        edName=findViewById(R.id.edName);
        edEmail=findViewById(R.id.email);
        String userEmail = currentUser.getEmail();
        edEmail.setText(userEmail == null ? "" : userEmail);
        score=findViewById(R.id.score);
        coin=findViewById(R.id.coin);
        findViewById(R.id.buttonLogout).setOnClickListener(view -> {
            Dialog dialog=new Dialog(MainMenu.this);
            dialog.setContentView(R.layout.dialog);
            Button cancel=dialog.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            Button yes =dialog.findViewById(R.id.save);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainMenu.this, SignIn.class));
                    finish();
                }
            });
            dialog.show();

        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainMenu.this,TestActivity.class);
                startActivity(intent);
                finish();
            }
        });
        setData();
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(MainMenu.this);
                dialog.setContentView(R.layout.namedialog);
                Button cancel=dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button save =dialog.findViewById(R.id.save);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editTextName=dialog.findViewById(R.id.name);
                        String Newname=editTextName.getText().toString().trim();
                        if(Newname.isEmpty())
                        {
                            Toast.makeText(MainMenu.this, "Enter name", Toast.LENGTH_SHORT).show();
                        }else {
                            updated = true;
                            users.child("name").setValue(Newname);
                            users.child("updated").setValue(updated);
                            name.setText(Newname);
                            edName.setText(Newname);
                            Toast.makeText(MainMenu.this, "Name updated", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    private void setData() {
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              int scores = parseIntSafely(snapshot.child("score").getValue());
              score.setText(String.valueOf(scores));
              String coins = valueOrEmpty(snapshot.child("coin").getValue());
              coin.setText(coins.isEmpty() ? "0" : coins);
              String Name = valueOrEmpty(snapshot.child("name").getValue());
              name.setText(Name.isEmpty() ? "Player" : Name);
              edName.setText(Name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                score.setText("0");
            }
        });
    }

    private int parseIntSafely(Object value) {
        if (value == null) {
            return 0;
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String valueOrEmpty(Object value) {
        return value == null ? "" : value.toString();
    }


}
