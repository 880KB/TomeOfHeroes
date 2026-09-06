package dev.swim.toh.model.data.prerequisites;

import dev.swim.toh.model.core.CharacterModel;

/**
 * Not enforced yet - the app has no concept of "which weapon" a feat like Waffenfokus applies to
 * (the "*" repeatable-with-a-choice mechanic isn't modeled). Kept so the prerequisite is still
 * visible to the player.
 */
public class ProficiencyWithWeaponPrerequisite implements Prerequisite {

    public boolean isSatisfiedBy(CharacterModel characterModel) {
        return true;
    }

    @Override
    public String toString() {
        return "Geübt im Umgang mit der Waffe";
    }
}
