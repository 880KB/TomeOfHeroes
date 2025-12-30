package dev.swim.toh.model.rules.feat;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.data.feat.Feat;
import dev.swim.toh.model.data.feat.FeatName;
import dev.swim.toh.model.data.feat.FeatRepository;
import org.springframework.stereotype.Component;

@Component
public class FeatRules {

    private final FeatRepository featRepository;

    public FeatRules(FeatRepository featRepository) {
        this.featRepository = featRepository;
    }

    /**
     * A feat can be added if:
     * - all prerequisites are satisfied and
     * - the character does not already have the feat or the feat can be added multiple times
     *   (with or without different options)
     */
    public boolean canAdd(CharacterModel characterModel, FeatName featName) {
        Feat feat = featRepository.getFeat(featName);
        // TODO: add feat options
        return prerequisitesSatisfied(characterModel, feat) && notSelectedOrRepeatable(characterModel, feat);
    }

    public boolean canAddNoPrerequisitesCheck(CharacterModel characterModel, FeatName featName) {
        return notSelectedOrRepeatable(characterModel, featRepository.getFeat(featName));
    }

    public boolean prerequisitesSatisfied(CharacterModel characterModel, FeatName featName) {
        return prerequisitesSatisfied(characterModel, featRepository.getFeat(featName));
    }

    private boolean notSelectedOrRepeatable(CharacterModel characterModel, Feat feat) {
        return !characterModel.feats.hasFeat(feat.getName()) || feat.isRepeatable();
    }

    private boolean prerequisitesSatisfied(CharacterModel characterModel, Feat feat) {
        return feat.getPrerequisites().stream().allMatch(prerequisite -> prerequisite.isSatisfiedBy(characterModel));
    }
}
