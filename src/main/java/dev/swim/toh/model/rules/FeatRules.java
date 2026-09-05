package dev.swim.toh.model.rules;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.data.feat.Feat;
import dev.swim.toh.model.data.feat.FeatRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FeatRules {

    private final FeatRepository featRepository;

    public FeatRules(FeatRepository featRepository) {
        this.featRepository = featRepository;
    }

    public List<String> getAvailableFeats(CharacterModel characterModel) {
        return featRepository.getAll().stream()
                .filter(feat -> canAdd(characterModel, feat))
                .map(Feat::getId)
                .toList();
    }

    public List<Feat> getNotSelectedFeats(CharacterModel characterModel) {
        return featRepository.getAll().stream()
                .filter(feat -> notSelectedOrRepeatable(characterModel, feat))
                .toList();
    }

    /**
     * A feat can be added if:
     * - all prerequisites are satisfied and
     * - the character does not already have the feat or the feat can be added multiple times
     *   (with or without different options)
     */
    public boolean canAdd(CharacterModel characterModel, Feat feat) {
        // TODO: add feat options
        return prerequisitesSatisfied(characterModel, feat) && notSelectedOrRepeatable(characterModel, feat);
    }

    public boolean canAddNoPrerequisitesCheck(CharacterModel characterModel, Feat feat) {
        return notSelectedOrRepeatable(characterModel, feat);
    }

    private boolean notSelectedOrRepeatable(CharacterModel characterModel, Feat feat) {
        return !characterModel.feats.hasFeat(feat) || feat.getRepeatType().isRepeatable();
    }

    public boolean prerequisitesSatisfied(CharacterModel characterModel, Feat feat) {
        return feat.getPrerequisites().stream().allMatch(prerequisite -> prerequisite.isSatisfiedBy(characterModel));
    }
}
