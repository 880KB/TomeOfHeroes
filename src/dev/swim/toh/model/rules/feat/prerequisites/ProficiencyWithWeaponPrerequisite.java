package dev.swim.toh.model.rules.feat.prerequisites;

import dev.swim.toh.model.core.Character;

public class ProficiencyWithWeaponPrerequisite implements Prerequisite {


    public boolean isSatisfiedBy(Character character) {
        return true;
    }
}
