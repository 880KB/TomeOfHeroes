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
        if (FeatRules.canAdd(character, featName))
            featList.add(new ChosenFeat(featName));
    }

    protected void addFeatNoPrerequisitesCheck(FeatName featName) {
        if (FeatRules.canAddNoPrerequisitesCheck(character, featName))
            featList.add(new ChosenFeat(featName));
    }

    protected void removeFeat(ChosenFeat chosenFeat) {
        if (chosenFeat != null)
            featList.remove(chosenFeat);
    }

    protected boolean hasFeat(FeatName featName) {
        return featList.stream()
                .anyMatch(feat -> feat.nameProperty().get() == featName);
    }

    protected List<FeatName> getAvailableFeats() {
        return Stream.of(FeatName.values())
                .filter(featName -> FeatRules.canAdd(character, featName))
                .toList();
    }

    protected List<FeatName> getNotSelectedFeats() {
        return Stream.of(FeatName.values())
                // TODO: also return multi selectable feats
                .filter(featName -> !character.feats.hasFeat(featName))
                .toList();
    }
}
