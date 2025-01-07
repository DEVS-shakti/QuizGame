package com.shakti.quizgame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TestActivity extends AppCompatActivity {


    private RecyclerView rv;
    private List<Test> testList = new ArrayList<>();
    ArrayList<String> status = new ArrayList<>();
    TestAdapter testAdapter;
    int n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, QuestionsExporter.class);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity.this, MainMenu.class));
                finish();
            }
        });
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("QuestionsParent");
        Toast.makeText(this, String.valueOf(n), Toast.LENGTH_SHORT).show();
        for (int i = 0; i < 11; i++) {
            String testname = "Test " + String.valueOf(i + 1);
            testList.add(new Test(testname, QuizPage.class));
        }
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        testAdapter = new TestAdapter(this, testList);
        rv.setAdapter(testAdapter);
    }

    private void setStatus() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        for (int i = 0; i < 10; i++) {
            String test = "Questions";
            test = test + String.valueOf(i + 1);
            DatabaseReference reference = database.getReference().child(test);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    status.add(snapshot.child(String.valueOf(0)).child("status").getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

}