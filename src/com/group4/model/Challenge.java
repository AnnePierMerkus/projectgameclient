package com.group4.model;

public class Challenge {
    private String username;
    private String code;
    private String game_type;

    public Challenge(String username, String code, String game_type) {
        this.username = username;
        this.code = code;
        this.game_type = game_type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGame_type() {
        return game_type;
    }

    public void setGame_type(String game_type) {
        this.game_type = game_type;
    }

    @Override
    public String toString() {
        return this.getUsername() + ", " + this.getGame_type() + ", " + this.getCode();
    }
}
