package com.example.tetris;

public class AllEntries {
    private final String username;
    private final int score;
    private final int time;

    public AllEntries(String username, int score, int time) {
        this.username = username;
        this.score = score;
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public int getTime() {
        return time;
    }
}
