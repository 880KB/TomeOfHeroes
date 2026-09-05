package dev.swim.toh.model.data.feat.prerequisites;

import dev.swim.toh.model.core.CharacterModel;

public record FeatPrerequisite(String requiredFeat) implements Prerequisite {

    public boolean isSatisfiedBy(CharacterModel characterModel) {
        return characterModel.feats.hasFeat(requiredFeat);
    }

    @Override
    public String toString() {
        return requiredFeat == null ? "" : requiredFeat;
    }
}
