package com.siwuxie095.concurrent.chapter7th.example14th;

/**
 * @author Jiajing Li
 * @date 2020-10-04 16:19:18
 */
@SuppressWarnings("all")
public final class PBestMsg {

    private final PsoValue value;

    public PBestMsg(PsoValue value) {
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
