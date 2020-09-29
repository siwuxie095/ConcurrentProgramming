package com.siwuxie095.concurrent.chapter6th.example11th;

/**
 * @author Jiajing Li
 * @date 2020-09-29 22:28:26
 */
@SuppressWarnings("all")
public class User {

    private int id;

    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
