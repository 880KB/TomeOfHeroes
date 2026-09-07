package dev.swim.toh.model.core.feats;

import dev.swim.toh.definition.feat.FeatConfig;
import dev.swim.toh.definition.feat.FeatDefinition;
import dev.swim.toh.model.calculation.FeatSlotCalculator.FeatPool;
import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.core.classes.ChosenClass;
import dev.swim.toh.model.data.clazz.Clazz;
import dev.swim.toh.model.data.feat.Feat;
import dev.swim.toh.model.data.feat.FeatRepository;
import dev.swim.toh.model.data.feat.FeatType;
import dev.swim.toh.model.data.feat.RepeatType;
import dev.swim.toh.model.rules.FeatRules;
import dev.swim.toh.persistence.CharacterData;
import dev.swim.toh.persistence.CharacterDataMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Covers the exact scenario the user found: a Cleric manually takes Scribe Scroll (costs a
 * normal slot), then multiclasses into Wizard and reaches level 5 (opening a Wizard-bonus slot).
 * The already-chosen feat must NOT be retroactively reclassified as consuming that new pool -
 * pool assignment is a historical fact, frozen the moment a feat is added (see
 * FeatsComputed#assignPool), not something re-derived from the character's current state.
 */
class FeatPoolFreezeTest {

    private static final String SCRIBE_SCROLL = "SCHRIFTROLLE_ANFERTIGEN";
    private static final String EXTEND_SPELL = "ZAUBER_AUSDEHNEN";

    private static CharacterModel newCharacter() {
        FeatDefinition scribeScroll = featDefinition(SCRIBE_SCROLL, FeatType.ITEM_CREATION);
        FeatDefinition extendSpell = featDefinition(EXTEND_SPELL, FeatType.METAMAGIC);

        FeatConfig config = new FeatConfig();
        config.setFeats(List.of(scribeScroll, extendSpell));
        FeatRepository featRepository = new FeatRepository(config);
        FeatRules featRules = new FeatRules(featRepository);
        return new CharacterModel(featRepository, featRules);
    }

    private static FeatDefinition featDefinition(String id, FeatType type) {
        FeatDefinition def = new FeatDefinition();
        def.setId(id);
        def.setName(id);
        def.setType(type);
        def.setRepeatType(RepeatType.SINGLE);
        def.setPrerequisites(List.of());
        def.setBenefits(List.of());
        return def;
    }

    @Test
    void manuallyChosenFeatKeepsItsPoolAfterLaterMulticlassingIntoWizard() {
        CharacterModel character = newCharacter();

        // no Wizard yet - only the default Human race-bonus / class-level slots exist
        character.feats.addFeat(SCRIBE_SCROLL);
        SelectedFeat scribeScroll = character.feats.getFeatList().get(0);
        FeatPool originalPool = scribeScroll.getPool();
        assertTrue(originalPool == FeatPool.RACE_BONUS || originalPool == FeatPool.CLASS,
                "expected the pre-Wizard pick to land in an unrestricted pool, was " + originalPool);

        // multiclass into Wizard and reach level 5 - opens a fresh Wizard-bonus slot
        character.classes.addClass(Clazz.WIZARD, 1, true);
        ChosenClass wizard = character.classes.getClassList().stream()
                .filter(cc -> cc.clazzProperty().get() == Clazz.WIZARD)
                .findFirst().orElseThrow();
        wizard.levelProperty().set(5);

        assertEquals(originalPool, scribeScroll.getPool(),
                "an already-chosen feat must not be retroactively reassigned to a pool that opened up later");

        // a *new* metamagic pick, made after the slot exists, correctly uses it
        character.feats.addFeat(EXTEND_SPELL);
        SelectedFeat extendSpell = character.feats.getFeatList().get(1);
        assertEquals(FeatPool.WIZARD_BONUS, extendSpell.getPool());
    }

    @Test
    void automaticGrantRoundTripsThroughSaveLoadAndIsCleanedUpOnClassRemoval() {
        CharacterModel original = newCharacter();
        original.classes.addClass(Clazz.WIZARD, 1, true);

        SelectedFeat grant = original.feats.getFeatList().get(0);
        assertTrue(original.feats.isAutomaticGrant(grant));
        assertNull(grant.getPool());

        CharacterData data = CharacterDataMapper.toData(original);

        CharacterModel loaded = newCharacter();
        CharacterDataMapper.applyTo(data, loaded);

        SelectedFeat loadedGrant = loaded.feats.getFeatList().stream()
                .filter(sf -> sf.featProperty().get().getId().equals(SCRIBE_SCROLL))
                .findFirst().orElseThrow();
        assertTrue(loaded.feats.isAutomaticGrant(loadedGrant), "restored grant must still be marked automatic");
        assertNull(loadedGrant.getPool());

        // removing Wizard afterwards must clean up the restored grant, not leave a ghost entry -
        // this only works if AutomaticFeatGrants' own bookkeeping correctly re-pointed at the
        // restored instance while classes were being (re-)loaded
        ChosenClass loadedWizard = loaded.classes.getClassList().stream()
                .filter(cc -> cc.clazzProperty().get() == Clazz.WIZARD)
                .findFirst().orElseThrow();
        loaded.classes.removeClass(loadedWizard);

        assertFalse(loaded.feats.hasFeat(SCRIBE_SCROLL), "automatic grant should be removed once its granting class is gone");
    }
}
