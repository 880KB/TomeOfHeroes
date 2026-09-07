package dev.swim.toh.model.data.feat;

import dev.swim.toh.model.data.clazz.Clazz;
import dev.swim.toh.model.data.prerequisites.Prerequisite;

import java.util.List;

public class Feat {
    private final String id;
    private final String name;
    private final FeatType type;
    private final RepeatType repeatType;
    private final String shortDescription;
    private final String description;
    private final List<Prerequisite> prerequisites;
    private final List<Benefit> benefits;
    private final List<Clazz> bonusFeatClasses;
    // private final List<FeatOption> validOptions;

    public Feat(String id,
                String name,
                FeatType type,
                RepeatType repeatType,
                String shortDescription,
                String description,
                List<Prerequisite> prerequisites,
                List<Benefit> benefits,
                List<Clazz> bonusFeatClasses
                // List<FeatOption> validOptions
    ) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.repeatType = repeatType;
        this.shortDescription = shortDescription;
        this.description = description;
        this.prerequisites = prerequisites;
        this.benefits = benefits;
        this.bonusFeatClasses = bonusFeatClasses;
        // this.validOptions = validOptions;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FeatType getType() {
        return type;
    }

    public RepeatType getRepeatType() {
        return repeatType;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public List<Prerequisite> getPrerequisites() {
        return prerequisites;
    }

    public List<Benefit> getBenefits() {
        return benefits;
    }

    public List<Clazz> getBonusFeatClasses() {
        return bonusFeatClasses;
    }
}
