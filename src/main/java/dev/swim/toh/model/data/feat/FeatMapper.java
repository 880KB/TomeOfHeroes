package dev.swim.toh.model.data.feat;

import dev.swim.toh.definition.feat.BenefitDefinition;
import dev.swim.toh.definition.feat.FeatDefinition;
import dev.swim.toh.definition.feat.PrerequisiteDefinition;
import dev.swim.toh.model.core.bonus.BonusTarget;
import dev.swim.toh.model.core.hitpoints.HpBonusTarget;
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
                mapPrerequisites(def.getPrerequisites()),
                mapBenefits(def.getBenefits())
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

    private static List<Benefit> mapBenefits(List<BenefitDefinition> defs) {
        return defs.stream()
                .map(FeatMapper::mapBenefit)
                .toList();
    }

    private static Benefit mapBenefit(BenefitDefinition def) {
        return switch (def.getType()) {
            case BONUS -> new BonusBenefit(resolveBonusTarget(def.getTarget()), def.getBonusType(), def.getValue());
        };
    }

    // TODO: nur HP-Ziele bekannt - bei weiteren Bonus-Zielen (RK, Rettungswürfe, ...) erweitern
    private static BonusTarget resolveBonusTarget(String target) {
        return HpBonusTarget.valueOf(target);
    }
}
