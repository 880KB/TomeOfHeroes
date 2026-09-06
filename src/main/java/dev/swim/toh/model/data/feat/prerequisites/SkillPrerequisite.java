package dev.swim.toh.model.data.feat.prerequisites;

import dev.swim.toh.model.core.CharacterModel;

/**
 * Not enforced yet - skills aren't modeled in the app at all. Kept so the prerequisite is still
 * visible to the player, same placeholder approach as {@link ProficiencyWithWeaponPrerequisite}.
 */
public record SkillPrerequisite(String skillName) implements Prerequisite {

    public boolean isSatisfiedBy(CharacterModel characterModel) {
        return true;
    }

    @Override
    public String toString() {
        return skillName + " (Fertigkeit)";
    }
}
