package dev.swim.toh.model.core.feats;

import dev.swim.toh.model.core.InputBase;
import dev.swim.toh.model.data.feat.FeatName;
import dev.swim.toh.model.rules.feat.FeatRules;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Stream;

public class FeatsInput extends InputBase {
    private final ObservableList<ChosenFeat> featList = FXCollections.observableArrayList();

    protected ObservableList<ChosenFeat> getFeatList() {
        return featList;
    }

    protected void addFeat(FeatName featName) {
        if (FeatRules.canAddFeat(featList, featName))
            featList.add(new ChosenFeat(featName));
    }

    protected void removeFeat(ChosenFeat chosenFeat) {
        if (chosenFeat != null)
            featList.remove(chosenFeat);
    }

    protected boolean hasFeat(ChosenFeat chosenFeat) {
        return featList.stream()
                .anyMatch(feat -> feat.nameProperty().get() == chosenFeat.nameProperty().get());
    }

    protected List<FeatName> getAvailableFeats() {
        return Stream.of(FeatName.values())
                .filter(featName -> !this.hasFeat(new ChosenFeat(featName)))
                .toList();
    }
}
