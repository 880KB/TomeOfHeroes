package dev.swim.toh.definition.feat;

import dev.swim.toh.model.core.bonus.BonusType;

public class BenefitDefinition {

    public enum Type {
        BONUS
        // später: weitere Effekt-Arten
    }

    private Type type;

    // BONUS
    private String target;
    private BonusType bonusType;
    private Integer value;

    // getters / setters
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public BonusType getBonusType() {
        return bonusType;
    }

    public void setBonusType(BonusType bonusType) {
        this.bonusType = bonusType;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
