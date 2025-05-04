package dev.swim.toh.model.core;

public abstract class CoreBase<T extends InputBase, U extends ComputedBase> {

    protected T input;
    protected U computed;

    public void init(Character character) {
        input.setCharacter(character);
        computed.setCharacter(character);
    }
}
