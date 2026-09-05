package dev.swim.toh.model.calculation;

import dev.swim.toh.model.core.bonus.Bonus;

import java.util.List;
import java.util.stream.Collectors;

public class BonusCalculator {

    public static int getTotal(List<Bonus> bonuses) {
        return bonuses.stream()
                .collect(Collectors.groupingBy(Bonus::type))
                .values().stream()
                .mapToInt(BonusCalculator::totalForType)
                .sum();
    }

    private static int totalForType(List<Bonus> bonusesOfSameType) {
        if (bonusesOfSameType.getFirst().type().stacks())
            return bonusesOfSameType.stream().mapToInt(Bonus::value).sum();
        return bonusesOfSameType.stream().mapToInt(Bonus::value).max().orElse(0);
    }
}
