package com.siwuxie095.concurrent.chapter6th.example11th;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiajing Li
 * @date 2020-09-29 22:50:15
 */
@SuppressWarnings("all")
public class ConstructMethodRef {

    @FunctionalInterface
    interface UserFactory<T extends User> {

        T create(int id, String name);

    }

    private static UserFactory<User> factory = User::new;

    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            users.add(factory.create(i, "jack" + i));
        }
        users.stream().map(User::getName).forEach(System.out::println);
    }

}
