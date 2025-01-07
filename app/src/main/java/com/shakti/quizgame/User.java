package com.shakti.quizgame;

public class User {

    String email;
    String password;
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

    User(String name,String email,String password,int score,boolean updated)
    {
        this.email=email;
        this.password=password;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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
