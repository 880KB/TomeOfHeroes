package dev.swim.toh.model.data.feat;

import dev.swim.toh.definition.feat.FeatDefinition;
import dev.swim.toh.definition.feat.PrerequisiteDefinition;
import dev.swim.toh.model.data.feat.prerequisites.AttributePrerequisite;
import dev.swim.toh.model.data.feat.prerequisites.FeatPrerequisite;
import dev.swim.toh.model.data.feat.prerequisites.Prerequisite;

import java.util.List;

public class FeatMapper {

    public static Feat toFeat(FeatDefinition def) {
        return new Feat(
                def.getId(),
                def.getName(),
                def.getType(),
                def.getRepeatType(),
                def.getShortDescription(),
                mapPrerequisites(def.getPrerequisites())
        );
    }

    private static List<Prerequisite> mapPrerequisites(List<PrerequisiteDefinition> defs) {
        return defs.stream()
                .map(FeatMapper::mapPrerequisite)
                .toList();
    }

    private static Prerequisite mapPrerequisite(PrerequisiteDefinition def) {
        return switch (def.getType()) {
            case ATTRIBUTE -> new AttributePrerequisite(
                    def.getAttribute(),
                    def.getMin()
            );
            case FEAT -> new FeatPrerequisite(def.getFeat());
        };
    }
}
