package dev.swim.toh.model.core.feats;

import dev.swim.toh.model.core.CoreBase;
import dev.swim.toh.model.data.feat.FeatName;
import javafx.collections.ObservableList;

import java.util.List;

public class Feats extends CoreBase<FeatsInput, FeatsComputed> {
    public Feats() {
        input = new FeatsInput();
        computed = new FeatsComputed();
    }

    public ObservableList<ChosenFeat> getFeatList() {
        return input.getFeatList();
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

    public List<FeatName> getNotSelectedFeats() {
        return input.getNotSelectedFeats();
    }
}
