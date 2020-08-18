package com.siwuxie095.concurrent.chapter1st.example5th;

/**
 * @author Jiajing Li
 * @date 2020-01-07 23:18:54
 */
public class Main {


    public static void main(String[] args) {
        new Thread(new MultiThreadLong.ChangeT(111L)).start();
        new Thread(new MultiThreadLong.ChangeT(-999L)).start();
        new Thread(new MultiThreadLong.ChangeT(333L)).start();
        new Thread(new MultiThreadLong.ChangeT(-444L)).start();
        new Thread(new MultiThreadLong.ReadT()).start();
    }

}
