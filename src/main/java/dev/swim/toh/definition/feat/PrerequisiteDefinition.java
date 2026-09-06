package dev.swim.toh.definition.feat;

import dev.swim.toh.model.data.attribute.AttributeName;
import dev.swim.toh.model.data.clazz.Clazz;

import java.util.List;

public class PrerequisiteDefinition {

    public enum Type {
        ATTRIBUTE,
        FEAT,
        CHARACTER_LEVEL,
        CLASS_LEVEL,
        BASE_ATTACK_BONUS,
        SKILL,
        WEAPON_PROFICIENCY,
        CASTER_LEVEL
    }

    private Type type;

    // ATTRIBUTE
    private AttributeName attribute;
    private Integer min;

    // FEAT
    private String feat;

    // CLASS_LEVEL - satisfied if the character has at least `min` levels in any of `classes`
    // (a list, not a single class, to cover "Kleriker oder Paladin"-style either/or prerequisites)
    private List<Clazz> classes;

    // SKILL - not enforced yet, skills aren't modeled in the app; kept for display only
    private String skill;

    // getters / setters
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public AttributeName getAttribute() {
        return attribute;
    }

    public void setAttribute(AttributeName attribute) {
        this.attribute = attribute;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public String getFeat() {
        return feat;
    }

    public void setFeat(String feat) {
        this.feat = feat;
    }

    public List<Clazz> getClasses() {
        return classes;
    }

    public void setClasses(List<Clazz> classes) {
        this.classes = classes;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }
}
