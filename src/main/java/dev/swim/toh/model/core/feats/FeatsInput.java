package dev.swim.toh.model.core.feats;

import dev.swim.toh.model.core.InputBase;
import dev.swim.toh.model.data.feat.Feat;
import dev.swim.toh.model.rules.FeatRules;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FeatsInput extends InputBase {

    private final FeatRules featRules;

    public FeatsInput(FeatRules featRules) {
        this.featRules = featRules;
    }

    private final ObservableList<SelectedFeat> featList = FXCollections.observableArrayList();

    protected ObservableList<SelectedFeat> getFeatList() {
        return featList;
    }

    protected void addFeat(Feat feat) {
        if (featRules.canAdd(characterModel, feat))
            featList.add(new SelectedFeat(feat));
    }

    protected void addFeatNoPrerequisitesCheck(Feat feat) {
        if (featRules.canAddNoPrerequisitesCheck(characterModel, feat))
            featList.add(new SelectedFeat(feat));
    }

    protected void removeFeat(SelectedFeat selectedFeat) {
        if (selectedFeat != null)
            featList.remove(selectedFeat);
    }

    protected boolean hasFeat(Feat feat) {
        return featList.stream()
                .anyMatch(selected -> selected.featProperty().get().getId().equals(feat.getId()));
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
