package com.shakti.quizgame;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class QuestionsExporter extends AppCompatActivity {

    private EditText questionEditText, optionAEditText, optionBEditText, optionCEditText, optionDEditText,ansEditText;
    private Button uploadButton;
    private DatabaseReference questionsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_exporter);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuestionsExporter.this,TestActivity.class));
                finish();
            }
        });

        int testIndex = getIntent().getIntExtra("testIndex", 11);
        // Initialize Firebase Database reference
        questionsRef = FirebaseDatabase.getInstance().getReference("QuestionsParent").child("Questions" + testIndex);

        // Link UI elements
        initializeUI();

        // Set button click listener
        uploadButton.setOnClickListener(view -> determineNextIdAndUpload());
    }

    private void initializeUI() {
        questionEditText = findViewById(R.id.questionEditText);
        optionAEditText = findViewById(R.id.optionAEditText);
        optionBEditText = findViewById(R.id.optionBEditText);
        optionCEditText = findViewById(R.id.optionCEditText);
        optionDEditText = findViewById(R.id.optionDEditText);
        ansEditText=findViewById(R.id.ansEditText);
        uploadButton = findViewById(R.id.uploadButton);
    }

    private void determineNextIdAndUpload() {
        questionsRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int nextId = calculateNextId(snapshot);
                uploadQuestion(nextId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuestionsExporter.this, "Error retrieving data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int calculateNextId(DataSnapshot snapshot) {
        int nextId = 1;
        if (snapshot.exists()) {
            for (DataSnapshot child : snapshot.getChildren()) {
                try {
                    int lastId = Integer.parseInt(child.child("id").getValue().toString());
                    nextId = lastId + 1;
                } catch (Exception e) {
                    Toast.makeText(this, "Invalid ID format in Firebase", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return nextId;
    }

    private void uploadQuestion(int nextId) {
        String question = questionEditText.getText().toString().trim();
        String optionA = optionAEditText.getText().toString().trim();
        String optionB = optionBEditText.getText().toString().trim();
        String optionC = optionCEditText.getText().toString().trim();
        String optionD = optionDEditText.getText().toString().trim();
        String ans = ansEditText.getText().toString().trim();

        if (!areFieldsValid(question, optionA, optionB, optionC, optionD)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ans.matches("[A-Da-d]")) {
            Toast.makeText(this, "Answer must be A, B, C, or D", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create and upload question data
        HashMap<String, Object> questionData = createQuestionData(nextId, question, optionA, optionB, optionC, optionD,ans);

        questionsRef.child(String.valueOf(nextId)).setValue(questionData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Question uploaded successfully!", Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        Toast.makeText(this, "Failed to upload question", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean areFieldsValid(String question, String optionA, String optionB, String optionC, String optionD) {
        return !(TextUtils.isEmpty(question) || TextUtils.isEmpty(optionA) || TextUtils.isEmpty(optionB)
                || TextUtils.isEmpty(optionC) || TextUtils.isEmpty(optionD));
    }

    private HashMap<String, Object> createQuestionData(int id, String question, String optionA, String optionB, String optionC, String optionD,String ans) {
        HashMap<String, Object> questionData = new HashMap<>();
        questionData.put("id", id);
        questionData.put("question", question);
        questionData.put("optionA", optionA);
        questionData.put("optionB", optionB);
        questionData.put("optionC", optionC);
        questionData.put("optionD", optionD);
        questionData.put("answer",ans.toUpperCase());
        return questionData;
    }

    private void clearFields() {
        questionEditText.setText("");
        optionAEditText.setText("");
        optionBEditText.setText("");
        optionCEditText.setText("");
        optionDEditText.setText("");
        ansEditText.setText("");
    }
}
