package com.siwuxie095.concurrent.chapter7th.example14th;

/**
 * @author Jiajing Li
 * @date 2020-10-04 16:20:32
 */
@SuppressWarnings("all")
public final class GBestMsg {

    private final PsoValue value;

    public GBestMsg(PsoValue value) {
        this.value = value;
    }

    public PsoValue getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
