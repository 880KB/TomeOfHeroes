package dev.swim.toh.model.core.feats;

import dev.swim.toh.model.core.ComputedBase;
import dev.swim.toh.model.data.attribute.AttributeName;
import dev.swim.toh.model.data.feat.Feat;
import dev.swim.toh.model.validation.Severity;
import dev.swim.toh.model.validation.Violation;
import dev.swim.toh.translation.PrerequisiteFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.stream.Collectors;

public class FeatsValidation extends ComputedBase {

    private final ObservableList<Violation> violations = FXCollections.observableArrayList();

    public ObservableList<Violation> violationsProperty() {
        return violations;
    }

    @Override
    public void addListeners() {
        // re-check whenever the selected feats change, or anything a Prerequisite could depend on
        // (currently: attributes and other feats - see the Prerequisite implementations)
        characterModel.feats.getFeatList().addListener((ListChangeListener<? super SelectedFeat>) c -> revalidate());
        for (AttributeName attributeName : AttributeName.values())
            characterModel.attributes.getAttributeBaseProperty(attributeName)
                    .addListener((obs, oldValue, newValue) -> revalidate());
        revalidate();
    }

    private void revalidate() {
        violations.setAll(characterModel.feats.getFeatList().stream()
                .filter(selectedFeat -> !characterModel.feats.prerequisitesSatisfied(selectedFeat.featProperty().get()))
                .map(this::toViolation)
                .toList());
    }

    private Violation toViolation(SelectedFeat selectedFeat) {
        Feat feat = selectedFeat.featProperty().get();
        String unmetPrerequisites = feat.getPrerequisites().stream()
                .filter(prerequisite -> !prerequisite.isSatisfiedBy(characterModel))
                .map(prerequisite -> PrerequisiteFormatter.toGerman(prerequisite, characterModel.feats))
                .collect(Collectors.joining(", "));
        return new Violation("Voraussetzung nicht erfüllt: " + unmetPrerequisites, Severity.ERROR, selectedFeat);
    }
}
