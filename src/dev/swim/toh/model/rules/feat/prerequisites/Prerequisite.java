package dev.swim.toh.model.rules.feat.prerequisites;

import dev.swim.toh.model.core.Character;

public interface Prerequisite {
    boolean isSatisfiedBy(Character character);
}
