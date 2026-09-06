package dev.swim.toh.model.core.feats;

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
        this.computed = new FeatsComputed();
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

    public void addFeat(String id) {
        input.addFeat(featRepository.getFeat(id));
    }

    public void addFeatNoPrerequisitesCheck(Feat feat) {
        input.addFeatNoPrerequisitesCheck(feat);
    }

    public void removeFeat(SelectedFeat selectedFeat) {
        input.removeFeat(selectedFeat);
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
