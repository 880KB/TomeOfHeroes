package dev.swim.toh.model.core.bonus;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

public class BonusPool {

    private final Map<BonusTarget, ObservableList<Bonus>> bonusesByTarget = new HashMap<>();

    public void add(BonusTarget target, Bonus bonus) {
        getBonuses(target).add(bonus);
    }

    public void removeAllFrom(Object sourceId) {
        bonusesByTarget.values().forEach(bonuses -> bonuses.removeIf(bonus -> bonus.sourceId().equals(sourceId)));
    }

    public ObservableList<Bonus> getBonuses(BonusTarget target) {
        return bonusesByTarget.computeIfAbsent(target, t -> FXCollections.observableArrayList());
    }
}
