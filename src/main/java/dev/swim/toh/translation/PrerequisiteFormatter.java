package dev.swim.toh.translation;

import dev.swim.toh.model.core.feats.Feats;
import dev.swim.toh.model.data.feat.Feat;
import dev.swim.toh.model.data.prerequisites.AttributePrerequisite;
import dev.swim.toh.model.data.prerequisites.BaseAttackBonusPrerequisite;
import dev.swim.toh.model.data.prerequisites.CasterLevelPrerequisite;
import dev.swim.toh.model.data.prerequisites.CharacterLevelPrerequisite;
import dev.swim.toh.model.data.prerequisites.ClassLevelPrerequisite;
import dev.swim.toh.model.data.prerequisites.FeatPrerequisite;
import dev.swim.toh.model.data.prerequisites.Prerequisite;
import dev.swim.toh.model.data.prerequisites.ProficiencyWithWeaponPrerequisite;
import dev.swim.toh.model.data.prerequisites.SkillPrerequisite;
import dev.swim.toh.model.data.clazz.Clazz;

import java.util.stream.Collectors;

public class PrerequisiteFormatter {
    public static String toGerman(Prerequisite prerequisite, Feats feats) {
        if (prerequisite instanceof AttributePrerequisite attributePrerequisite)
            return AttributeNameTranslator.toGerman(attributePrerequisite.requiredAttributeName()) + " " + attributePrerequisite.requiredValue();
        if (prerequisite instanceof FeatPrerequisite featPrerequisite) {
            Feat requiredFeat = feats.getFeatById(featPrerequisite.requiredFeat());
            return "Talent: " + (requiredFeat != null ? requiredFeat.getName() : featPrerequisite.requiredFeat());
        }
        if (prerequisite instanceof CharacterLevelPrerequisite characterLevelPrerequisite)
            return "Charakterstufe " + characterLevelPrerequisite.requiredLevel() + "+";
        if (prerequisite instanceof ClassLevelPrerequisite classLevelPrerequisite) {
            String classNames = classLevelPrerequisite.requiredClasses().stream()
                    .map(PrerequisiteFormatter::classNameGerman)
                    .collect(Collectors.joining(" oder "));
            return classLevelPrerequisite.requiredLevel() <= 1
                    ? classNames
                    : classNames + " Stufe " + classLevelPrerequisite.requiredLevel() + "+";
        }
        if (prerequisite instanceof BaseAttackBonusPrerequisite baseAttackBonusPrerequisite)
            return "Grund-Angriffsbonus +" + baseAttackBonusPrerequisite.requiredValue();
        if (prerequisite instanceof SkillPrerequisite skillPrerequisite)
            return skillPrerequisite.skillName() + " (Fertigkeit)";
        if (prerequisite instanceof ProficiencyWithWeaponPrerequisite)
            return "Geübt im Umgang mit der Waffe";
        if (prerequisite instanceof CasterLevelPrerequisite casterLevelPrerequisite)
            return "Zauberstufe " + casterLevelPrerequisite.requiredLevel() + "+";
        return prerequisite.toString();
    }

    private static String classNameGerman(Clazz clazz) {
        return ClazzTranslator.toGerman(clazz);
    }
}
