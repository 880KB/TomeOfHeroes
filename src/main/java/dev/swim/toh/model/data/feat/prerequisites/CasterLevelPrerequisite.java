package dev.swim.toh.model.data.feat.prerequisites;

import dev.swim.toh.model.core.CharacterModel;

/**
 * Not enforced yet - the app has no spellcasting system, so there's nothing to compute a caster
 * level from. Kept so the prerequisite is still visible to the player, same placeholder approach
 * as {@link ProficiencyWithWeaponPrerequisite}.
 */
public record CasterLevelPrerequisite(int requiredLevel) implements Prerequisite {

    public boolean isSatisfiedBy(CharacterModel characterModel) {
        return true;
    }

    @Override
    public String toString() {
        return "Zauberstufe " + requiredLevel;
    }
}
