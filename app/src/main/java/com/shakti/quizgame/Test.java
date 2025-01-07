package com.shakti.quizgame;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Test {
    private String testName;
    private Class<?> intentClass;
    private String status;
    public Test(String testName, Class<?> intentClass) {
        this.status=status;
        this.testName = testName;
        this.intentClass = intentClass;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void setIntentClass(Class<?> intentClass) {
        this.intentClass = intentClass;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Class<?> getIntentClass() {
        return intentClass;
    }
}

