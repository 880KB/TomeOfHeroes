package dev.swim.toh.model.data.feat.prerequisites;

import dev.swim.toh.model.core.CharacterModel;

public interface Prerequisite {
    boolean isSatisfiedBy(CharacterModel characterModel);
}
