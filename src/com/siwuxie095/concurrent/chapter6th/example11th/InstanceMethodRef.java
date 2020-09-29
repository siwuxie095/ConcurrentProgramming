package com.siwuxie095.concurrent.chapter6th.example11th;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiajing Li
 * @date 2020-09-29 22:29:38
 */
@SuppressWarnings("all")
public class InstanceMethodRef {

    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            users.add(new User(i, "Jack" + i));
        }
        users.stream().map(User::getName).forEach(System.out::println);
    }

}
