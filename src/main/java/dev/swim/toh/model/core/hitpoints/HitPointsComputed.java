package dev.swim.toh.model.core.hitpoints;

import dev.swim.toh.model.calculation.BonusCalculator;
import dev.swim.toh.model.core.ComputedBase;
import dev.swim.toh.model.core.bonus.Bonus;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;

public class HitPointsComputed extends ComputedBase {

    private final IntegerProperty bonusMaxHitPoints = new SimpleIntegerProperty(0);
    private final IntegerProperty totalMaxHitPoints = new SimpleIntegerProperty(0);

    public IntegerProperty getBonusMaxHitPointsProperty() {
        return bonusMaxHitPoints;
    }

    public IntegerProperty getTotalMaxHitPointsProperty() {
        return totalMaxHitPoints;
    }

    @Override
    public void addListeners() {
        characterModel.hitPoints.getBaseMaxHitPointsProperty().addListener((obs, oldValue, newValue) -> updateTotal());
        characterModel.bonusPool.getBonuses(HpBonusTarget.MAX_HIT_POINTS)
                .addListener((ListChangeListener<Bonus>) c -> updateTotal());
        updateTotal();
    }

    private void updateTotal() {
        int base = characterModel.hitPoints.getBaseMaxHitPointsProperty().get();
        int bonus = BonusCalculator.getTotal(characterModel.bonusPool.getBonuses(HpBonusTarget.MAX_HIT_POINTS));
        bonusMaxHitPoints.set(bonus);
        totalMaxHitPoints.set(base + bonus);
    }
}
