package dev.swim.toh.model.data.feat.prerequisites;

import dev.swim.toh.model.core.CharacterModel;

public class ProficiencyWithWeaponPrerequisite implements Prerequisite {


    public boolean isSatisfiedBy(CharacterModel characterModel) {
        return true;
    }
}
