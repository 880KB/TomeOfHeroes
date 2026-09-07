package dev.swim.toh.model.core.feats;

import dev.swim.toh.model.core.ComputedBase;
import dev.swim.toh.model.core.classes.ChosenClass;
import dev.swim.toh.model.data.clazz.Clazz;
import javafx.collections.ListChangeListener;

/**
 * Grants feats the player doesn't choose - currently just a Wizard's free 1st-level Scribe
 * Scroll. Kept as a live invariant tied to the character's current classes (re-derived on every
 * class-list change) rather than a one-time add, consistent with the rest of this app treating
 * derived state as reactive to current inputs rather than build history - removing the Wizard
 * class removes the grant too, matching how e.g. maxFighterBonusFeats resets when Fighter is
 * removed.
 */
public class AutomaticFeatGrants extends ComputedBase {

    private static final String SCRIBE_SCROLL_ID = "SCHRIFTROLLE_ANFERTIGEN";

    private SelectedFeat scribeScrollGrant;

    @Override
    public void addListeners() {
        characterModel.classes.getClassList().addListener(
                (ListChangeListener<? super ChosenClass>) c -> updateScribeScrollGrant());
        updateScribeScrollGrant();
    }

    private void updateScribeScrollGrant() {
        boolean hasWizard = characterModel.classes.hasClass(Clazz.WIZARD);
        if (hasWizard && scribeScrollGrant == null) {
            // returns null if the player already holds the feat manually - leave it untouched
            // and player-removable in that case, rather than adopting it as automatic
            scribeScrollGrant = characterModel.feats.addAutomaticFeat(characterModel.feats.getFeatById(SCRIBE_SCROLL_ID));
        }
        else if (!hasWizard && scribeScrollGrant != null) {
            characterModel.feats.removeAutomaticFeat(scribeScrollGrant);
            scribeScrollGrant = null;
        }
    }
}
