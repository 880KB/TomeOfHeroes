package dev.swim.toh.model.rules.feat;

import dev.swim.toh.model.core.Character;
import dev.swim.toh.model.data.feat.Feat;
import dev.swim.toh.model.data.feat.FeatName;
import dev.swim.toh.model.data.feat.FeatRegistry;

public class FeatRules {

    /**
     * A feat can be added if:
     * - all prerequisites are satisfied and
     * - the character does not already have the feat or the feat can be added multiple times
     *   (with or without different options)
     */
    public static boolean canAdd(Character character, FeatName featName) {
        Feat feat = FeatRegistry.getFeat(featName);
        // TODO: add feat options
        return prerequisitesSatisfied(character, feat) && notSelectedOrRepeatable(character, feat);
    }

    public static boolean canAddNoPrerequisitesCheck(Character character, FeatName featName) {
        return notSelectedOrRepeatable(character, FeatRegistry.getFeat(featName));
    }

    private static boolean notSelectedOrRepeatable(Character character, Feat feat) {
        return !character.feats.hasFeat(feat.getName()) || feat.isRepeatable();
    }

    private static boolean prerequisitesSatisfied(Character character, Feat feat) {
        return feat.getPrerequisites().stream().allMatch(prerequisite -> prerequisite.isSatisfiedBy(character));
    }
}
