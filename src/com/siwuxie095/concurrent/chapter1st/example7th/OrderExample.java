package com.siwuxie095.concurrent.chapter1st.example7th;

/**
 * @author Jiajing Li
 * @date 2020-08-20 23:04:34
 */
public class OrderExample {

    int a = 0;

    boolean flag = false;

    public void writer() {
        a = 1;
        flag = true;
    }

    public void reader() {
        if (flag) {
            int i = a + 1;
        }
    }

}
