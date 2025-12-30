package dev.swim.toh.model.core.feats;

import dev.swim.toh.model.core.CoreBase;
import dev.swim.toh.model.data.feat.Feat;
import dev.swim.toh.model.data.feat.FeatName;
import dev.swim.toh.model.data.feat.FeatRepository;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Feats extends CoreBase<FeatsInput, FeatsComputed> {

    private final FeatRepository featRepository;

    public Feats(FeatRepository featRepository, FeatsInput input) {
        this.featRepository = featRepository;
        this.input = input;
        this.computed = new FeatsComputed();
    }

    public ObservableList<ChosenFeat> getFeatList() {
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

    public void addFeat(FeatName featName) {
        input.addFeat(featName);
    }

    public void addFeatNoPrerequisitesCheck(FeatName featName) {
        input.addFeatNoPrerequisitesCheck(featName);
    }

    public void removeFeat(ChosenFeat chosenFeat) {
        input.removeFeat(chosenFeat);
    }

    public boolean hasFeat(FeatName featName) {
        return input.hasFeat(featName);
    }

    public List<FeatName> getAvailableFeats() {
        return input.getAvailableFeats();
    }

    public boolean prerequisitesSatisfied(FeatName featName) {
        return input.prerequisitesSatisfied(featName);
    }

    public List<Feat> getNotSelectedFeats() {
        return input.getNotSelectedFeats().stream()
                .map(featRepository::getFeat)
                .toList();
    }
}
