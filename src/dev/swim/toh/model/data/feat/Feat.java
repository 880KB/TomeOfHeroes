package dev.swim.toh.model.data.feat;

import dev.swim.toh.model.rules.feat.prerequisites.Prerequisite;
import dev.swim.toh.model.data.clazz.Clazz;

import java.util.List;

public class Feat {
    private final FeatName name;
    private final String description;
    private final List<Prerequisite> prerequisites;
    private final boolean isRepeatable;
    private final boolean isRepeatableWithDifferentOption;
    // private final List<FeatOption> validOptions;
    private final List<Clazz> isGrantedByClasses;

    public Feat(FeatName name,
                String description,
                List<Prerequisite> prerequisites,
                boolean isRepeatable,
                boolean isRepeatableWithDifferentOption,
                // List<FeatOption> validOptions,
                List<Clazz> isGrantedByClasses) {
        this.name = name;
        this.description = description;
        this.prerequisites = prerequisites;
        this.isRepeatable = isRepeatable;
        this.isRepeatableWithDifferentOption = isRepeatableWithDifferentOption;
        // this.validOptions = validOptions;
        this.isGrantedByClasses = isGrantedByClasses;
    }

    public FeatName getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Prerequisite> getPrerequisites() {
        return prerequisites;
    }

    public boolean isRepeatable() {
        return isRepeatable;
    }

    public boolean isRepeatableWithDifferentOption() {
        return isRepeatableWithDifferentOption;
    }
}
