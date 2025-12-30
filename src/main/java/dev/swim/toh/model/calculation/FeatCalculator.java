package dev.swim.toh.model.calculation;

public class FeatCalculator {
    public static int getMaxClassFeats(int classLevel) {
        return 1 + classLevel / 3;
    }

    public static int getMaxFighterBonusFeats(int fighterLevel) {
        return 1 + fighterLevel / 2;
    }
}
