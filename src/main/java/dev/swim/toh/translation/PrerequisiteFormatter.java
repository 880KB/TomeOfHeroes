package dev.swim.toh.translation;

import dev.swim.toh.model.core.feats.Feats;
import dev.swim.toh.model.data.feat.Feat;
import dev.swim.toh.model.data.feat.prerequisites.AttributePrerequisite;
import dev.swim.toh.model.data.feat.prerequisites.FeatPrerequisite;
import dev.swim.toh.model.data.feat.prerequisites.Prerequisite;

public class PrerequisiteFormatter {
    public static String toGerman(Prerequisite prerequisite, Feats feats) {
        if (prerequisite instanceof AttributePrerequisite attributePrerequisite)
            return AttributeNameTranslator.toGerman(attributePrerequisite.requiredAttributeName()) + " " + attributePrerequisite.requiredValue();
        if (prerequisite instanceof FeatPrerequisite featPrerequisite) {
            Feat requiredFeat = feats.getFeatById(featPrerequisite.requiredFeat());
            return "Talent: " + (requiredFeat != null ? requiredFeat.getName() : featPrerequisite.requiredFeat());
        }
        return prerequisite.toString();
    }
}
