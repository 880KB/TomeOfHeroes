package dev.swim.toh.persistence;

import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.core.classes.ChosenClass;
import dev.swim.toh.model.core.feats.SelectedFeat;
import dev.swim.toh.model.core.weapons.SelectedWeaponSlot;
import dev.swim.toh.model.data.attribute.AttributeName;
import dev.swim.toh.model.data.savingthrow.SavingThrow;

public class CharacterDataMapper {

    public static CharacterData toData(CharacterModel model) {
        CharacterData data = new CharacterData();

        data.description.name = model.description.nameProperty().get();
        data.description.race = model.description.raceProperty().get();
        data.description.age = model.description.ageProperty().get();
        data.description.size = model.description.sizeProperty().get();

        for (AttributeName attributeName : AttributeName.values())
            data.attributes.put(attributeName, model.attributes.getAttributeBaseProperty(attributeName).get());

        for (SavingThrow savingThrow : SavingThrow.values())
            data.savingThrowMiscMods.put(savingThrow, model.savingThrows.getSavingThrowMiscModProperty(savingThrow).get());

        for (ChosenClass chosenClass : model.classes.getClassList()) {
            CharacterData.ChosenClassData classData = new CharacterData.ChosenClassData();
            classData.clazz = chosenClass.clazzProperty().get();
            classData.level = chosenClass.levelProperty().get();
            classData.firstClass = chosenClass.isFirstClassProperty().get();
            data.classes.add(classData);
        }

        for (SelectedFeat selectedFeat : model.feats.getFeatList()) {
            CharacterData.SelectedFeatData featData = new CharacterData.SelectedFeatData();
            featData.featId = selectedFeat.featProperty().get().getId();
            featData.pool = selectedFeat.getPool();
            featData.automatic = model.feats.isAutomaticGrant(selectedFeat);
            data.feats.add(featData);
        }

        data.armorClass.armorBonus = model.armorClass.getArmorBonusProperty().get();
        data.armorClass.shieldBonus = model.armorClass.getShieldBonusProperty().get();
        data.armorClass.naturalArmor = model.armorClass.getNaturalArmorProperty().get();
        data.armorClass.deflectionBonus = model.armorClass.getDeflectionBonusProperty().get();
        data.armorClass.miscMod = model.armorClass.getMiscModProperty().get();

        data.initiativeMiscMod = model.initiative.getMiscModProperty().get();

        data.hitPoints.baseMax = model.hitPoints.getBaseMaxHitPointsProperty().get();
        data.hitPoints.current = model.hitPoints.getCurrentHitPointsProperty().get();

        data.spellResistance = model.spellResistance.spellResistanceProperty().get();

        for (SelectedWeaponSlot selectedWeaponSlot : model.weapons.getSlots()) {
            CharacterData.WeaponSlotData weaponSlotData = new CharacterData.WeaponSlotData();
            weaponSlotData.slot = selectedWeaponSlot.getSlot();
            weaponSlotData.weaponId = model.weapons.getWeaponId(selectedWeaponSlot.getSlot());
            weaponSlotData.note = selectedWeaponSlot.noteProperty().get();
            data.weapons.add(weaponSlotData);
        }

        return data;
    }

    /**
     * Applies a loaded snapshot onto a freshly created, still-empty CharacterModel (see
     * CharacterModelFactory#createEmptyCharacter) - classes go through the normal add-method so
     * their side effects ("exactly one first class") still run. Feats are restored (not
     * re-added) exactly as saved, deliberately loaded *before* classes: AutomaticFeatGrants
     * reacts to class changes, and would otherwise grant a fresh automatic Scribe Scroll the
     * moment the Wizard class is added - before a saved, manually-paid Scribe Scroll gets a
     * chance to be restored, silently replacing it with a free one it never earned. Restoring
     * feats first means that row already exists (with its saved pool/automatic status intact)
     * by the time the class-list listener fires.
     */
    public static void applyTo(CharacterData data, CharacterModel model) {
        model.description.nameProperty().set(data.description.name);
        model.description.raceProperty().set(data.description.race);
        model.description.ageProperty().set(data.description.age);
        model.description.sizeProperty().set(data.description.size);

        for (AttributeName attributeName : AttributeName.values()) {
            Integer value = data.attributes.get(attributeName);
            if (value != null)
                model.attributes.getAttributeBaseProperty(attributeName).set(value);
        }

        for (SavingThrow savingThrow : SavingThrow.values()) {
            Integer value = data.savingThrowMiscMods.get(savingThrow);
            if (value != null)
                model.savingThrows.getSavingThrowMiscModProperty(savingThrow).set(value);
        }

        for (CharacterData.SelectedFeatData featData : data.feats)
            model.feats.restoreFeat(featData.featId, featData.pool, featData.automatic);

        for (CharacterData.ChosenClassData classData : data.classes)
            model.classes.addClass(classData.clazz, classData.level, classData.firstClass);

        model.armorClass.getArmorBonusProperty().set(data.armorClass.armorBonus);
        model.armorClass.getShieldBonusProperty().set(data.armorClass.shieldBonus);
        model.armorClass.getNaturalArmorProperty().set(data.armorClass.naturalArmor);
        model.armorClass.getDeflectionBonusProperty().set(data.armorClass.deflectionBonus);
        model.armorClass.getMiscModProperty().set(data.armorClass.miscMod);

        model.initiative.getMiscModProperty().set(data.initiativeMiscMod);

        model.hitPoints.getBaseMaxHitPointsProperty().set(data.hitPoints.baseMax);
        model.hitPoints.getCurrentHitPointsProperty().set(data.hitPoints.current);

        model.spellResistance.spellResistanceProperty().set(data.spellResistance);

        for (CharacterData.WeaponSlotData weaponSlotData : data.weapons) {
            model.weapons.setWeapon(weaponSlotData.slot, weaponSlotData.weaponId);
            model.weapons.noteProperty(weaponSlotData.slot).set(weaponSlotData.note);
        }
    }
}
