package com.shakti.quizgame;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class MainMenu extends AppCompatActivity {

    Button start;
    TextView name,score,coin;
    EditText edName,edEmail;
    Bitmap bitmap;

    boolean upadted;
    ImageView dialog_dp;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageViewProfile;
    private Uri imageUri;


    DatabaseReference users=firebaseDatabase.getReference("users").child(FirebaseAuth.getInstance().getUid());
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
        String UserEmail=FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        edEmail.setText(UserEmail);
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
                            upadted=true;
                            users.child("name").setValue(Newname);
                            users.child("updated").setValue(upadted);
                            name.setText(Newname);
                            edName.setText(Newname);
                            Toast.makeText(MainMenu.this, "Name updated as test  ", Toast.LENGTH_SHORT).show();
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
              int scores= Integer.parseInt(snapshot.child("score").getValue().toString());
              score.setText(String.valueOf(scores));
              String coins=  snapshot.child("coin").getValue().toString();
              coin.setText(coins);
              String Name=snapshot.child("name").getValue().toString();
              name.setText(Name);
              edName.setText(Name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                score.setText("0");
            }
        });
    }


}