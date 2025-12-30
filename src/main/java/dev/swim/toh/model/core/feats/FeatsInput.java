package dev.swim.toh.model.core.feats;

import dev.swim.toh.model.core.InputBase;
import dev.swim.toh.model.data.feat.FeatName;
import dev.swim.toh.model.rules.feat.FeatRules;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class FeatsInput extends InputBase {

    private final FeatRules featRules;

    public FeatsInput(FeatRules featRules) {
        this.featRules = featRules;
    }

    private final ObservableList<ChosenFeat> featList = FXCollections.observableArrayList();

    protected ObservableList<ChosenFeat> getFeatList() {
        return featList;
    }

    protected void addFeat(FeatName featName) {
        if (featRules.canAdd(characterModel, featName))
            featList.add(new ChosenFeat(featName));
    }

    protected void addFeatNoPrerequisitesCheck(FeatName featName) {
        if (featRules.canAddNoPrerequisitesCheck(characterModel, featName))
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

    // TODO: move check to FeatRules
    protected List<FeatName> getAvailableFeats() {
        return Stream.of(FeatName.values())
                .filter(featName -> featRules.canAdd(characterModel, featName))
                .toList();
    }

    // TODO: move check to FeatRules
    protected List<FeatName> getNotSelectedFeats() {
        return Stream.of(FeatName.values())
                // TODO: also return multi selectable feats
                .filter(featName -> !characterModel.feats.hasFeat(featName))
                .toList();
    }

    protected boolean prerequisitesSatisfied(FeatName featName) {
        return featRules.prerequisitesSatisfied(characterModel, featName);
    }
}
