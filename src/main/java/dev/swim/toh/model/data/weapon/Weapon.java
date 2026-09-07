package dev.swim.toh.model.data.weapon;

import java.util.Set;

public class Weapon {
    private final String id;
    private final String name;
    private final WeaponProficiency proficiency;
    private final WeaponRangeType rangeType;
    // null for RANGED weapons (the source table has no handedness column there)
    private final WeaponHandedness handedness;
    // Tabelle 7-5's "Schaden (K)"/"Schaden (M)" columns - damage depends on the wielder's size,
    // not the weapon itself. Small/Medium are the two sizes the book gives directly; Tiny/Large/...
    // are a further calculation (see table footnote 1) not modeled yet.
    private final String damageSmall;
    private final String damageMedium;
    // second head's damage for a double weapon (footnote 5) - null otherwise
    private final String secondaryDamageSmall;
    private final String secondaryDamageMedium;
    private final int criticalThreatRange;
    private final int criticalMultiplier;
    // meters, null for weapons without a range increment
    private final Integer rangeIncrement;
    // weight for a Medium wielder (footnote 1: half for Small, double for Large - a calculation,
    // not stored per size)
    private final double weight;
    private final Set<DamageType> damageTypes;
    // true ("oder"): the wielder picks one of damageTypes at the moment of attack.
    // false ("und"): all of damageTypes apply simultaneously. Meaningless when damageTypes has
    // only one entry.
    private final boolean damageTypeChoice;
    private final boolean nonlethal;
    private final boolean reach;
    private final boolean doubleWeapon;
    private final String shortDescription;
    private final String description;

    public Weapon(String id,
                  String name,
                  WeaponProficiency proficiency,
                  WeaponRangeType rangeType,
                  WeaponHandedness handedness,
                  String damageSmall,
                  String damageMedium,
                  String secondaryDamageSmall,
                  String secondaryDamageMedium,
                  int criticalThreatRange,
                  int criticalMultiplier,
                  Integer rangeIncrement,
                  double weight,
                  Set<DamageType> damageTypes,
                  boolean damageTypeChoice,
                  boolean nonlethal,
                  boolean reach,
                  boolean doubleWeapon,
                  String shortDescription,
                  String description) {
        this.id = id;
        this.name = name;
        this.proficiency = proficiency;
        this.rangeType = rangeType;
        this.handedness = handedness;
        this.damageSmall = damageSmall;
        this.damageMedium = damageMedium;
        this.secondaryDamageSmall = secondaryDamageSmall;
        this.secondaryDamageMedium = secondaryDamageMedium;
        this.criticalThreatRange = criticalThreatRange;
        this.criticalMultiplier = criticalMultiplier;
        this.rangeIncrement = rangeIncrement;
        this.weight = weight;
        this.damageTypes = damageTypes;
        this.damageTypeChoice = damageTypeChoice;
        this.nonlethal = nonlethal;
        this.reach = reach;
        this.doubleWeapon = doubleWeapon;
        this.shortDescription = shortDescription;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public WeaponProficiency getProficiency() {
        return proficiency;
    }

    public WeaponRangeType getRangeType() {
        return rangeType;
    }

    public WeaponHandedness getHandedness() {
        return handedness;
    }

    public String getDamageSmall() {
        return damageSmall;
    }

    public String getDamageMedium() {
        return damageMedium;
    }

    public String getSecondaryDamageSmall() {
        return secondaryDamageSmall;
    }

    public String getSecondaryDamageMedium() {
        return secondaryDamageMedium;
    }

    public int getCriticalThreatRange() {
        return criticalThreatRange;
    }

    public int getCriticalMultiplier() {
        return criticalMultiplier;
    }

    public Integer getRangeIncrement() {
        return rangeIncrement;
    }

    public double getWeight() {
        return weight;
    }

    public Set<DamageType> getDamageTypes() {
        return damageTypes;
    }

    public boolean isDamageTypeChoice() {
        return damageTypeChoice;
    }

    public boolean isNonlethal() {
        return nonlethal;
    }

    public boolean isReach() {
        return reach;
    }

    public boolean isDoubleWeapon() {
        return doubleWeapon;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }
}
