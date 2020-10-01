package com.siwuxie095.concurrent.chapter6th.example13th;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Jiajing Li
 * @date 2020-10-01 14:46:02
 */
@SuppressWarnings("all")
public class Student {

    public int id;

    public String name;

    public int score;

    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Student student = new Student();
            student.id = i;
            student.name = String.valueOf(i);
            student.score = new Random().nextInt(100);
            students.add(student);
        }
        double serialAvg = students.stream().mapToInt(s -> s.score).average().getAsDouble();
        System.out.println(serialAvg);
        double parallelAvg = students.parallelStream().mapToInt(s -> s.score).average().getAsDouble();
        System.out.println(parallelAvg);

    }

}
