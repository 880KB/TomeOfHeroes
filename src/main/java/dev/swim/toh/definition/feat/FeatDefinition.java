package dev.swim.toh.definition.feat;

import dev.swim.toh.model.data.feat.FeatName;

import java.util.List;

public class FeatDefinition {
    private FeatName name;
    private String description;
    private boolean repeatable;
    private boolean repeatableWithDifferentOption;
    private List<PrerequisiteDefinition> prerequisites;

    // getters / setters
    public FeatName getName() {
        return name;
    }

    public void setName(FeatName name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }

    public boolean isRepeatableWithDifferentOption() {
        return repeatableWithDifferentOption;
    }

    public void setRepeatableWithDifferentOption(boolean repeatableWithDifferentOption) {
        this.repeatableWithDifferentOption = repeatableWithDifferentOption;
    }

    public List<PrerequisiteDefinition> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<PrerequisiteDefinition> prerequisites) {
        this.prerequisites = prerequisites;
    }
}
