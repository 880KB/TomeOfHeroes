package dev.swim.toh.definition.feat;

import dev.swim.toh.model.data.feat.FeatType;
import dev.swim.toh.model.data.feat.RepeatType;

import java.util.List;

public class FeatDefinition {
    private String id;
    private String name;
    private FeatType type;
    private RepeatType repeatType;
    private String shortDescription;
    private String description;
    private List<PrerequisiteDefinition> prerequisites;
    private List<BenefitDefinition> benefits;

    // getters / setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FeatType getType() {
        return type;
    }

    public void setType(FeatType type) {
        this.type = type;
    }

    public RepeatType getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(RepeatType repeatType) {
        this.repeatType = repeatType;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PrerequisiteDefinition> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<PrerequisiteDefinition> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public List<BenefitDefinition> getBenefits() {
        return benefits;
    }

    public void setBenefits(List<BenefitDefinition> benefits) {
        this.benefits = benefits;
    }
}
