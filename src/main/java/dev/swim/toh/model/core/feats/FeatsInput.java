package dev.swim.toh.model.core.feats;

import dev.swim.toh.model.core.InputBase;
import dev.swim.toh.model.data.feat.Feat;
import dev.swim.toh.model.rules.FeatRules;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

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
            addSelectedFeat(feat);
    }

    protected void addFeatNoPrerequisitesCheck(Feat feat) {
        if (featRules.canAddNoPrerequisitesCheck(characterModel, feat))
            addSelectedFeat(feat);
    }

    protected void removeFeat(SelectedFeat selectedFeat) {
        if (selectedFeat != null) {
            featList.remove(selectedFeat);
            selectedFeat.featProperty().get().getBenefits()
                    .forEach(benefit -> benefit.remove(characterModel, selectedFeat));
        }
    }

    private void addSelectedFeat(Feat feat) {
        SelectedFeat selectedFeat = new SelectedFeat(feat);
        featList.add(selectedFeat);
        feat.getBenefits().forEach(benefit -> benefit.apply(characterModel, selectedFeat));
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
