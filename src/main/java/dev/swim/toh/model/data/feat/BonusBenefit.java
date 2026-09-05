package dev.swim.toh.model.data.feat;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.core.bonus.Bonus;
import dev.swim.toh.model.core.bonus.BonusTarget;
import dev.swim.toh.model.core.bonus.BonusType;

public record BonusBenefit(BonusTarget target, BonusType bonusType, int value) implements Benefit {

    @Override
    public void apply(CharacterModel characterModel, Object sourceId) {
        characterModel.bonusPool.add(target, new Bonus(bonusType, value, sourceId));
    }

    @Override
    public void remove(CharacterModel characterModel, Object sourceId) {
        characterModel.bonusPool.removeAllFrom(sourceId);
    }
}
