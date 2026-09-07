package dev.swim.toh.model.calculation;

import dev.swim.toh.model.data.race.Race;

public class FeatCalculator {
    public static int getMaxClassFeats(int classLevel) {
        return 1 + classLevel / 3;
    }

    public static int getMaxFighterBonusFeats(int fighterLevel) {
        return 1 + fighterLevel / 2;
    }

    public static int getMaxWizardBonusFeats(int wizardLevel) {
        return wizardLevel / 5;
    }

    public static int getRaceBonusFeats(Race race) {
        return race == Race.HUMAN ? 1 : 0;
    }
}
