package dev.swim.toh.model.core.feats.prerequisites;

import dev.swim.toh.model.data.feat.FeatName;

public class FeatPrerequisite implements Prerequisite {
    private final FeatName requiredFeat;

    public FeatPrerequisite(FeatName requiredFeat) {
        this.requiredFeat = requiredFeat;
    }

    public FeatName getRequiredFeat() {
        return requiredFeat;
    }
}
