package dev.swim.toh.definition.feat;

import dev.swim.toh.model.data.clazz.Clazz;
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
    // classes whose bonus-feat list explicitly includes this feat (e.g. Fighter's PHB list);
    // empty/omitted for feats whose bonus-pool eligibility is derived structurally instead
    // (e.g. Wizard bonus feats, derived from FeatType being ITEM_CREATION/METAMAGIC)
    private List<Clazz> bonusFeatClasses;

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

    public List<Clazz> getBonusFeatClasses() {
        return bonusFeatClasses;
    }

    public void setBonusFeatClasses(List<Clazz> bonusFeatClasses) {
        this.bonusFeatClasses = bonusFeatClasses;
    }
}
