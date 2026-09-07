package dev.swim.toh.model.core.feats;

import dev.swim.toh.model.calculation.FeatSlotCalculator.FeatPool;
import dev.swim.toh.model.core.CoreBase;
import dev.swim.toh.model.data.feat.Feat;
import dev.swim.toh.model.data.feat.FeatRepository;
import dev.swim.toh.model.rules.FeatRules;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;

import java.util.List;

public class Feats extends CoreBase<FeatsInput, FeatsComputed> {

    private final FeatRepository featRepository;

    public Feats(FeatRepository featRepository, FeatRules featRules) {
        this.featRepository = featRepository;
        this.input = new FeatsInput(featRules);
        this.computed = new FeatsComputed(featRules);
    }

    public ObservableList<SelectedFeat> getFeatList() {
        return input.getFeatList();
    }

    public IntegerProperty maxFeatsProperty() {
        return computed.maxFeatsProperty();
    }

    public IntegerProperty maxClassFeatsProperty() {
        return computed.maxClassFeatsProperty();
    }

    public IntegerProperty maxFighterBonusFeatsProperty() {
        return computed.maxFighterBonusFeatsProperty();
    }

    public IntegerProperty raceBonusFeatsProperty() {
        return computed.raceBonusFeatsProperty();
    }

    public IntegerProperty maxWizardBonusFeatsProperty() {
        return computed.maxWizardBonusFeatsProperty();
    }

    public IntegerProperty usedClassFeatsProperty() {
        return computed.usedClassFeatsProperty();
    }

    public IntegerProperty usedFighterBonusFeatsProperty() {
        return computed.usedFighterBonusFeatsProperty();
    }

    public IntegerProperty usedWizardBonusFeatsProperty() {
        return computed.usedWizardBonusFeatsProperty();
    }

    public IntegerProperty usedRaceBonusFeatsProperty() {
        return computed.usedRaceBonusFeatsProperty();
    }

    public void addFeat(String id) {
        SelectedFeat selectedFeat = input.addFeat(featRepository.getFeat(id));
        if (selectedFeat != null)
            computed.assignPool(selectedFeat);
    }

    public void addFeatNoPrerequisitesCheck(Feat feat) {
        SelectedFeat selectedFeat = input.addFeatNoPrerequisitesCheck(feat);
        if (selectedFeat != null)
            computed.assignPool(selectedFeat);
    }

    /**
     * Recreates a saved feat exactly as it was: pool and automatic-grant status are historical
     * facts to restore, not to recompute - see FeatsInput#restoreFeat.
     */
    public void restoreFeat(String id, FeatPool pool, boolean automatic) {
        Feat feat = featRepository.getFeat(id);
        if (feat != null)
            input.restoreFeat(feat, pool, automatic);
    }

    public void removeFeat(SelectedFeat selectedFeat) {
        input.removeFeat(selectedFeat);
    }

    public SelectedFeat addAutomaticFeat(Feat feat) {
        return input.addAutomaticFeat(feat);
    }

    public void removeAutomaticFeat(SelectedFeat selectedFeat) {
        input.removeAutomaticFeat(selectedFeat);
    }

    public boolean isAutomaticGrant(SelectedFeat selectedFeat) {
        return input.isAutomaticGrant(selectedFeat);
    }

    public boolean hasFeat(String id) {
        return input.hasFeat(featRepository.getFeat(id));
    }

    public boolean hasFeat(Feat feat) {
        return input.hasFeat(feat);
    }

    public Feat getFeatById(String id) {
        return featRepository.getFeat(id);
    }

    public List<String> getAvailableFeats() {
        return input.getAvailableFeats();
    }

    public boolean prerequisitesSatisfied(Feat feat) {
        return input.prerequisitesSatisfied(feat);
    }

    public List<Feat> getNotSelectedFeats() {
        return input.getNotSelectedFeats();
    }
}
