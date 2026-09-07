package dev.swim.toh.model.core.feats;

import dev.swim.toh.model.calculation.FeatSlotCalculator.FeatPool;
import dev.swim.toh.model.core.InputBase;
import dev.swim.toh.model.data.feat.Feat;
import dev.swim.toh.model.rules.FeatRules;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FeatsInput extends InputBase {

    private final FeatRules featRules;
    private final Set<SelectedFeat> automaticGrants = new HashSet<>();

    public FeatsInput(FeatRules featRules) {
        this.featRules = featRules;
    }

    // extractor on poolProperty - pool assignment happens *after* a SelectedFeat is already in
    // this list (see FeatsComputed.assignPool), so plain observableArrayList() wouldn't notify
    // anyone once that later set() happens; this makes it fire like any other list change
    private final ObservableList<SelectedFeat> featList =
            FXCollections.observableArrayList(sf -> new Observable[]{sf.poolProperty()});

    protected ObservableList<SelectedFeat> getFeatList() {
        return featList;
    }

    protected SelectedFeat addFeat(Feat feat) {
        return featRules.canAdd(characterModel, feat) ? addSelectedFeat(feat) : null;
    }

    protected SelectedFeat addFeatNoPrerequisitesCheck(Feat feat) {
        return featRules.canAddNoPrerequisitesCheck(characterModel, feat) ? addSelectedFeat(feat) : null;
    }

    /**
     * Recreates a feat exactly as it was saved - pool and automatic-grant status are historical
     * facts at this point, not something to (re-)compute, so this bypasses eligibility/prerequisite
     * checks and FeatsComputed.assignPool entirely.
     */
    protected void restoreFeat(Feat feat, FeatPool pool, boolean automatic) {
        SelectedFeat selectedFeat = addSelectedFeat(feat);
        selectedFeat.poolProperty().set(pool);
        if (automatic)
            automaticGrants.add(selectedFeat);
    }

    protected void removeFeat(SelectedFeat selectedFeat) {
        if (selectedFeat != null) {
            featList.remove(selectedFeat);
            automaticGrants.remove(selectedFeat);
            selectedFeat.featProperty().get().getBenefits()
                    .forEach(benefit -> benefit.remove(characterModel, selectedFeat));
        }
    }

    /**
     * Grants a feat the player didn't choose (e.g. a Wizard's free 1st-level Scribe Scroll) -
     * doesn't consume any feat-slot pool. Per the rules the free feat is granted at the moment a
     * character reaches the triggering level, not re-evaluated retroactively - a Cleric who
     * already spent a feat on Scribe Scroll before ever multiclassing into Wizard doesn't get
     * refunded for it later, so if the player already holds the feat manually this is a no-op
     * (returns null). If it's already present *and already automatic* (e.g. just restored from a
     * save file before its granting class was reloaded), hands back that same instance instead -
     * the caller needs it to (re-)point its own bookkeeping at, not a fresh grant.
     */
    protected SelectedFeat addAutomaticFeat(Feat feat) {
        SelectedFeat existing = findSelectedFeat(feat);
        if (existing != null)
            return automaticGrants.contains(existing) ? existing : null;
        SelectedFeat selectedFeat = addSelectedFeat(feat);
        automaticGrants.add(selectedFeat);
        return selectedFeat;
    }

    protected void removeAutomaticFeat(SelectedFeat selectedFeat) {
        removeFeat(selectedFeat);
    }

    protected boolean isAutomaticGrant(SelectedFeat selectedFeat) {
        return automaticGrants.contains(selectedFeat);
    }

    private SelectedFeat addSelectedFeat(Feat feat) {
        SelectedFeat selectedFeat = new SelectedFeat(feat);
        featList.add(selectedFeat);
        feat.getBenefits().forEach(benefit -> benefit.apply(characterModel, selectedFeat));
        return selectedFeat;
    }

    protected boolean hasFeat(Feat feat) {
        return findSelectedFeat(feat) != null;
    }

    private SelectedFeat findSelectedFeat(Feat feat) {
        return featList.stream()
                .filter(selected -> selected.featProperty().get().getId().equals(feat.getId()))
                .findFirst()
                .orElse(null);
    }

    protected List<String> getAvailableFeats() {
        return featRules.getAvailableFeats(characterModel);
    }

    protected List<Feat> getNotSelectedFeats() {
        return featRules.getNotSelectedFeats(characterModel);
    }

    protected boolean prerequisitesSatisfied(Feat feat) {
        return featRules.prerequisitesSatisfied(characterModel, feat);
    }
}
