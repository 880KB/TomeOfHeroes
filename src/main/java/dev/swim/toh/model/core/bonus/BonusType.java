package dev.swim.toh.model.core.bonus;

public enum BonusType {
    ALCHEMICAL,
    ARMOR,
    CIRCUMSTANCE,
    COMPETENCE,
    DEFLECTION,
    DODGE,
    ENHANCEMENT,
    INHERENT,
    INSIGHT,
    LUCK,
    MORALE,
    NATURAL_ARMOR,
    PROFANE,
    RACIAL,
    RESISTANCE,
    SACRED,
    SHIELD,
    SIZE,
    UNTYPED;

    /**
     * Same-type bonuses normally don't stack (only the highest applies) - Dodge and Untyped are
     * the exceptions in D&D 3.5 and always stack, even with themselves.
     */
    public boolean stacks() {
        return this == DODGE || this == UNTYPED;
    }
}
