package dev.swim.toh.model.data.prerequisites;

import dev.swim.toh.model.core.CharacterModel;

/**
 * Not enforced yet - the app doesn't compute a base attack bonus anywhere. Kept so the
 * prerequisite is still visible to the player, same placeholder approach as
 * {@link ProficiencyWithWeaponPrerequisite}.
 */
public record BaseAttackBonusPrerequisite(int requiredValue) implements Prerequisite {

    public boolean isSatisfiedBy(CharacterModel characterModel) {
        return true;
    }

    @Override
    public String toString() {
        return "Grund-Angriffsbonus +" + requiredValue;
    }
}
