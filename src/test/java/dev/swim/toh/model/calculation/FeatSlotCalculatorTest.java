package dev.swim.toh.model.calculation;

import dev.swim.toh.model.calculation.FeatSlotCalculator.Capacities;
import dev.swim.toh.model.calculation.FeatSlotCalculator.FeatPool;
import dev.swim.toh.model.data.clazz.Clazz;
import dev.swim.toh.model.data.feat.Feat;
import dev.swim.toh.model.data.feat.FeatType;
import dev.swim.toh.model.data.feat.RepeatType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FeatSlotCalculatorTest {

    private static Feat feat(String id, FeatType type, List<Clazz> bonusFeatClasses) {
        return new Feat(id, id, type, RepeatType.MULTIPLE, null, null, List.of(), List.of(), bonusFeatClasses);
    }

    @Test
    void allocate_emptyInput_returnsEmptyList() {
        List<FeatPool> result = FeatSlotCalculator.allocate(List.of(), Set.of(), Set.of(),
                new Capacities(1, 1, 1));
        assertEquals(List.of(), result);
    }

    @Test
    void allocate_eligibleFeatConsumesItsPoolWhenAvailable() {
        Feat powerAttack = feat("HEFTIGER_ANGRIFF", FeatType.GENERAL, List.of(Clazz.FIGHTER));

        List<FeatPool> result = FeatSlotCalculator.allocate(List.of(powerAttack), Set.of(powerAttack), Set.of(),
                new Capacities(1, 0, 0));

        assertEquals(List.of(FeatPool.FIGHTER_BONUS), result);
    }

    @Test
    void allocate_fallsThroughFighterWizardRaceClassInPriorityOrder() {
        Feat generalFeat = feat("TOUGHNESS", FeatType.GENERAL, List.of());

        // no fighter/wizard eligibility, race exhausted -> falls to class
        List<FeatPool> result = FeatSlotCalculator.allocate(List.of(generalFeat), Set.of(), Set.of(),
                new Capacities(0, 0, 0));
        assertEquals(List.of(FeatPool.CLASS), result);

        // race still has room -> race wins over class
        result = FeatSlotCalculator.allocate(List.of(generalFeat), Set.of(), Set.of(),
                new Capacities(0, 0, 1));
        assertEquals(List.of(FeatPool.RACE_BONUS), result);
    }

    @Test
    void allocate_twoFighterEligibleFeatsWithOneSlot_selectionOrderDecides() {
        Feat first = feat("HEFTIGER_ANGRIFF", FeatType.GENERAL, List.of(Clazz.FIGHTER));
        Feat second = feat("WAFFENFOKUS", FeatType.GENERAL, List.of(Clazz.FIGHTER));
        Set<Feat> fighterEligible = Set.of(first, second);

        List<FeatPool> result = FeatSlotCalculator.allocate(List.of(first, second), fighterEligible, Set.of(),
                new Capacities(1, 0, 0));

        assertEquals(FeatPool.FIGHTER_BONUS, result.get(0));
        // no fighter slot left, not race/wizard-eligible either -> falls through to the
        // uncapped CLASS catch-all rather than being rejected
        assertEquals(FeatPool.CLASS, result.get(1));
    }

    @Test
    void allocate_classPoolAcceptsFeatsBeyondItsOwnCapacity() {
        Feat first = feat("TOUGHNESS", FeatType.GENERAL, List.of());
        Feat second = feat("TOUGHNESS", FeatType.GENERAL, List.of());

        // capacity 0 everywhere - both still land in CLASS, none rejected
        List<FeatPool> result = FeatSlotCalculator.allocate(List.of(first, second), Set.of(), Set.of(),
                new Capacities(0, 0, 0));

        assertEquals(List.of(FeatPool.CLASS, FeatPool.CLASS), result);
    }

    @Test
    void allocate_sameRepeatableFeatSelectedTwice_getsIndependentAssignments() {
        Feat toughness = feat("TOUGHNESS", FeatType.GENERAL, List.of());

        List<FeatPool> result = FeatSlotCalculator.allocate(List.of(toughness, toughness), Set.of(), Set.of(),
                new Capacities(0, 0, 1));

        assertEquals(FeatPool.RACE_BONUS, result.get(0));
        assertEquals(FeatPool.CLASS, result.get(1));
    }
}
