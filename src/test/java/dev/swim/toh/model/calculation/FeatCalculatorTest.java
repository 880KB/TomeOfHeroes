package dev.swim.toh.model.calculation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FeatCalculatorTest {

    @ParameterizedTest
    @CsvSource({
            "0, 0",
            "4, 0",
            "5, 1",
            "9, 1",
            "10, 2",
            "20, 4"
    })
    void getMaxWizardBonusFeats(int wizardLevel, int expected) {
        assertEquals(expected, FeatCalculator.getMaxWizardBonusFeats(wizardLevel));
    }

    @Test
    void getMaxWizardBonusFeats_doesNotCountTheAutomaticFirstLevelScribeScroll() {
        // the free Scribe Scroll grant at 1st level is a separate mechanism, not part of this pool
        assertEquals(0, FeatCalculator.getMaxWizardBonusFeats(1));
    }
}
