package dev.swim.toh.model.data.feat;

public enum RepeatType {
    SINGLE,
    MULTIPLE,
    STACKS;

    public boolean isRepeatable() {
        return this == MULTIPLE || this == STACKS;
    }
}
