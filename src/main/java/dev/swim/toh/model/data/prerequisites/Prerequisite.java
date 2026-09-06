package dev.swim.toh.model.data.prerequisites;

import dev.swim.toh.model.core.CharacterModel;

public interface Prerequisite {
    boolean isSatisfiedBy(CharacterModel characterModel);
}
