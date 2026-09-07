package dev.swim.toh.definition.weapon;

import dev.swim.toh.model.data.weapon.DamageType;
import dev.swim.toh.model.data.weapon.WeaponHandedness;
import dev.swim.toh.model.data.weapon.WeaponProficiency;
import dev.swim.toh.model.data.weapon.WeaponRangeType;

import java.util.List;

public class WeaponDefinition {
    private String id;
    private String name;
    private WeaponProficiency proficiency;
    private WeaponRangeType rangeType;
    private WeaponHandedness handedness;
    private String damageSmall;
    private String damageMedium;
    private String secondaryDamageSmall;
    private String secondaryDamageMedium;
    private int criticalThreatRange = 20;
    private int criticalMultiplier;
    private Integer rangeIncrement;
    private double weight;
    private List<DamageType> damageTypes;
    private boolean damageTypeChoice;
    private boolean nonlethal;
    private boolean reach;
    private boolean doubleWeapon;
    private String shortDescription;
    private String description;

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

    public WeaponProficiency getProficiency() {
        return proficiency;
    }

    public void setProficiency(WeaponProficiency proficiency) {
        this.proficiency = proficiency;
    }

    public WeaponRangeType getRangeType() {
        return rangeType;
    }

    public void setRangeType(WeaponRangeType rangeType) {
        this.rangeType = rangeType;
    }

    public WeaponHandedness getHandedness() {
        return handedness;
    }

    public void setHandedness(WeaponHandedness handedness) {
        this.handedness = handedness;
    }

    public String getDamageSmall() {
        return damageSmall;
    }

    public void setDamageSmall(String damageSmall) {
        this.damageSmall = damageSmall;
    }

    public String getDamageMedium() {
        return damageMedium;
    }

    public void setDamageMedium(String damageMedium) {
        this.damageMedium = damageMedium;
    }

    public String getSecondaryDamageSmall() {
        return secondaryDamageSmall;
    }

    public void setSecondaryDamageSmall(String secondaryDamageSmall) {
        this.secondaryDamageSmall = secondaryDamageSmall;
    }

    public String getSecondaryDamageMedium() {
        return secondaryDamageMedium;
    }

    public void setSecondaryDamageMedium(String secondaryDamageMedium) {
        this.secondaryDamageMedium = secondaryDamageMedium;
    }

    public int getCriticalThreatRange() {
        return criticalThreatRange;
    }

    public void setCriticalThreatRange(int criticalThreatRange) {
        this.criticalThreatRange = criticalThreatRange;
    }

    public int getCriticalMultiplier() {
        return criticalMultiplier;
    }

    public void setCriticalMultiplier(int criticalMultiplier) {
        this.criticalMultiplier = criticalMultiplier;
    }

    public Integer getRangeIncrement() {
        return rangeIncrement;
    }

    public void setRangeIncrement(Integer rangeIncrement) {
        this.rangeIncrement = rangeIncrement;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public List<DamageType> getDamageTypes() {
        return damageTypes;
    }

    public void setDamageTypes(List<DamageType> damageTypes) {
        this.damageTypes = damageTypes;
    }

    public boolean isDamageTypeChoice() {
        return damageTypeChoice;
    }

    public void setDamageTypeChoice(boolean damageTypeChoice) {
        this.damageTypeChoice = damageTypeChoice;
    }

    public boolean isNonlethal() {
        return nonlethal;
    }

    public void setNonlethal(boolean nonlethal) {
        this.nonlethal = nonlethal;
    }

    public boolean isReach() {
        return reach;
    }

    public void setReach(boolean reach) {
        this.reach = reach;
    }

    public boolean isDoubleWeapon() {
        return doubleWeapon;
    }

    public void setDoubleWeapon(boolean doubleWeapon) {
        this.doubleWeapon = doubleWeapon;
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
}
