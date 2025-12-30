package dev.swim.toh.model.rules.feat.prerequisites;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.data.feat.FeatName;

public record FeatPrerequisite(FeatName requiredFeat) implements Prerequisite {

    public boolean isSatisfiedBy(CharacterModel characterModel) {
        return characterModel.feats.hasFeat(requiredFeat);
    }

    @Override
    public String toString() {
        return requiredFeat.toString();
    }
}
