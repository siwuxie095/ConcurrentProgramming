package com.siwuxie095.concurrent.chapter7th.example4th;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jiajing Li
 * @date 2020-10-02 16:46:48
 */
@SuppressWarnings("all")
public final class ImmutableMessage {

    private final int sequence;

    private final List<String> values;

    public ImmutableMessage(int sequence, List<String> values) {
        this.sequence = sequence;
        this.values = Collections.unmodifiableList(new ArrayList<>(values));
    }

    public int getSequence() {
        return sequence;
    }

    public List<String> getValues() {
        return values;
    }
}
