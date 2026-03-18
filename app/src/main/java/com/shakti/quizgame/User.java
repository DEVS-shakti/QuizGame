package com.shakti.quizgame;

public class User {

    String email;
    int score;
    boolean updated;
    String Name;
    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    User()
    {

    }

    User(String name,String email,int score,boolean updated)
    {
        this.email=email;
        this.score=score;
        this.Name = name;
        this.updated=updated;
    }

    public User(String name, int score) {
        this.Name = name;
        this.score = score;
    }



    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public int getScore() {
        return score;
    }
    public String getName()
    {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
