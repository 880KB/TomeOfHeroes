package dev.swim.toh.definition.feat;

import dev.swim.toh.model.data.attribute.AttributeName;
import dev.swim.toh.model.data.feat.FeatName;

public class PrerequisiteDefinition {

    public enum Type {
        ATTRIBUTE,
        FEAT
        // später: CLASS, BAB, SKILL, ...
    }

    private Type type;

    // ATTRIBUTE
    private AttributeName attribute;
    private Integer min;

    // FEAT
    private FeatName feat;

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

    public FeatName getFeat() {
        return feat;
    }

    public void setFeat(FeatName feat) {
        this.feat = feat;
    }
}
