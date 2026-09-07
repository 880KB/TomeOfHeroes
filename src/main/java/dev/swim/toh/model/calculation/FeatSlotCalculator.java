package dev.swim.toh.model.calculation;

import dev.swim.toh.model.data.feat.Feat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FeatSlotCalculator {

    public enum FeatPool {
        FIGHTER_BONUS,
        WIZARD_BONUS,
        RACE_BONUS,
        CLASS
    }

    public record Capacities(int fighterBonus, int wizardBonus, int raceBonus) {
    }

    /**
     * One FeatPool per entry in selectedFeatsInOrder (same order/length). Greedy, fixed priority:
     * FIGHTER_BONUS, WIZARD_BONUS, RACE_BONUS, CLASS - filling the most restrictive pool first
     * maximizes the player's remaining flexible slots instead of leaving it to chance. Race
     * accepts any feat, up to its capacity; CLASS is the catch-all and always accepts (its own
     * capacity is a soft target, not a hard cap - a feat count over capacity ends up here too, so
     * the player can pick which of their feats counts as "the extra one"). Fighter/Wizard only
     * accept feats in the matching eligible set. Automatically granted feats (e.g. a Wizard's
     * free Scribe Scroll) must be filtered out by the caller before calling this - they don't
     * compete for slots at all.
     */
    public static List<FeatPool> allocate(List<Feat> selectedFeatsInOrder,
                                           Set<Feat> fighterBonusEligible,
                                           Set<Feat> wizardBonusEligible,
                                           Capacities capacities) {
        int remainingFighterBonus = capacities.fighterBonus();
        int remainingWizardBonus = capacities.wizardBonus();
        int remainingRaceBonus = capacities.raceBonus();

        List<FeatPool> result = new ArrayList<>(selectedFeatsInOrder.size());
        for (Feat feat : selectedFeatsInOrder) {
            if (fighterBonusEligible.contains(feat) && remainingFighterBonus > 0) {
                remainingFighterBonus--;
                result.add(FeatPool.FIGHTER_BONUS);
            } else if (wizardBonusEligible.contains(feat) && remainingWizardBonus > 0) {
                remainingWizardBonus--;
                result.add(FeatPool.WIZARD_BONUS);
            } else if (remainingRaceBonus > 0) {
                remainingRaceBonus--;
                result.add(FeatPool.RACE_BONUS);
            } else {
                result.add(FeatPool.CLASS);
            }
        }
        return result;
    }
}
