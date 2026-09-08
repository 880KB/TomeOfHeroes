package dev.swim.toh.persistence;

import dev.swim.toh.model.calculation.FeatSlotCalculator.FeatPool;
import dev.swim.toh.model.core.weapons.WeaponSlot;
import dev.swim.toh.model.data.attribute.AttributeName;
import dev.swim.toh.model.data.clazz.Clazz;
import dev.swim.toh.model.data.race.Race;
import dev.swim.toh.model.data.savingthrow.SavingThrow;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Plain, Jackson-friendly snapshot of everything a saved character file needs to restore a
 * {@link dev.swim.toh.model.core.CharacterModel}. Holds only Input values - Computed values are
 * re-derived on load, the same way they are for any other change to the model.
 */
public class CharacterData {

    public static final int CURRENT_SCHEMA_VERSION = 3;

    public int schemaVersion = CURRENT_SCHEMA_VERSION;
    public DescriptionData description = new DescriptionData();
    public Map<AttributeName, Integer> attributes = new EnumMap<>(AttributeName.class);
    public Map<SavingThrow, Integer> savingThrowMiscMods = new EnumMap<>(SavingThrow.class);
    public List<ChosenClassData> classes = new ArrayList<>();
    public List<SelectedFeatData> feats = new ArrayList<>();
    public ArmorClassData armorClass = new ArmorClassData();
    public int initiativeMiscMod;
    public HitPointsData hitPoints = new HitPointsData();
    public int spellResistance;
    public List<WeaponSlotData> weapons = new ArrayList<>();

    public static class DescriptionData {
        public String name;
        public Race race;
        public String age;
        public String size;
    }

    public static class ChosenClassData {
        public Clazz clazz;
        public int level;
        public boolean firstClass;
    }

    public static class SelectedFeatData {
        public String featId;
        // which pool this feat's selection was frozen into at the time it was chosen - null for
        // an automatic grant, which never competes for a slot
        public FeatPool pool;
        // whether this feat was a free grant (e.g. a Wizard's 1st-level Scribe Scroll) rather
        // than a player choice - kept separate from `pool` rather than inferring it from
        // pool == null, so the two concepts ("which pool" vs. "was it free") don't get coupled
        public boolean automatic;
    }

    public static class ArmorClassData {
        public int armorBonus;
        public int shieldBonus;
        public int naturalArmor;
        public int deflectionBonus;
        public int miscMod;
    }

    public static class HitPointsData {
        public int baseMax;
        public int current;
    }

    public static class WeaponSlotData {
        public WeaponSlot slot;
        // null if the slot was empty when saved
        public String weaponId;
        public String note;
    }
}
