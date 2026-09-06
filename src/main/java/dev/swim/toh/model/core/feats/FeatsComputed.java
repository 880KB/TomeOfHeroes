package dev.swim.toh.model.core.feats;

import dev.swim.toh.model.calculation.FeatCalculator;
import dev.swim.toh.model.core.ComputedBase;
import dev.swim.toh.model.core.classes.ChosenClass;
import dev.swim.toh.model.data.clazz.Clazz;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;

public class FeatsComputed extends ComputedBase {

    private final IntegerProperty maxFeats = new SimpleIntegerProperty(0);
    private final IntegerProperty maxClassFeats = new SimpleIntegerProperty(0);
    private final IntegerProperty maxFighterBonusFeats = new SimpleIntegerProperty(0);
    private final IntegerProperty raceBonusFeats = new SimpleIntegerProperty(0);

    @Override
    public void addListeners() {
        // max class feats
        characterModel.classes.getCharacterLevelProperty().addListener((obs, oldLevel, newLevel) ->
                maxClassFeats.set(FeatCalculator.getMaxClassFeats(newLevel.intValue()))
        );
        maxClassFeats.set(FeatCalculator.getMaxClassFeats(characterModel.classes.getCharacterLevelProperty().get()));

        // race bonus feats
        characterModel.description.raceProperty().addListener((obs, oldRace, newRace) ->
                raceBonusFeats.set(FeatCalculator.getRaceBonusFeats(newRace))
        );
        raceBonusFeats.set(FeatCalculator.getRaceBonusFeats(characterModel.description.raceProperty().get()));

        // max fighter bonus feats for already-present classes
        for (ChosenClass chosenClass : characterModel.classes.getClassList()) {
            if (chosenClass.clazzProperty().get() == Clazz.FIGHTER) {
                chosenClass.levelProperty().addListener((obs, oldLevel, newLevel) ->
                        updateMaxFighterBonusFeats(newLevel.intValue()));
                updateMaxFighterBonusFeats(chosenClass.levelProperty().get());
            }
        }

        // TODO: max fighter bonus feats
        characterModel.classes.getClassList().addListener((ListChangeListener<? super ChosenClass>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (ChosenClass chosenClass : c.getAddedSubList()) {
                        if (chosenClass.clazzProperty().get() == Clazz.FIGHTER) {
                            chosenClass.levelProperty().addListener((obs, oldLevel, newLevel) ->
                                    updateMaxFighterBonusFeats(newLevel.intValue()));
                            updateMaxFighterBonusFeats(chosenClass.levelProperty().get());
                        }
                    }
                }
                else if (c.wasRemoved()) {
                    for (ChosenClass removedClass : c.getRemoved()) {
                        if (removedClass.clazzProperty().get() == Clazz.FIGHTER) {
                            maxFighterBonusFeats.set(0);
                        }
                    }
                }
            }
        });

        // max feats
        maxClassFeats.addListener((obs, oldMaxClassFeats, newMaxClassFeats) ->
                maxFeats.set(newMaxClassFeats.intValue() + maxFighterBonusFeats.intValue() + raceBonusFeats.intValue())
        );
        maxFighterBonusFeats.addListener((obs, oldMaxFighterBonusFeats, newMaxFighterBonusFeats) ->
                maxFeats.set(maxClassFeats.intValue() + newMaxFighterBonusFeats.intValue() + raceBonusFeats.intValue())
        );
        raceBonusFeats.addListener((obs, oldRaceBonusFeats, newRaceBonusFeats) ->
                maxFeats.set(maxClassFeats.intValue() + maxFighterBonusFeats.intValue() + newRaceBonusFeats.intValue())
        );
        maxFeats.set(maxClassFeats.get() + maxFighterBonusFeats.get() + raceBonusFeats.get());
    };

    protected IntegerProperty maxFeatsProperty() {
        return maxFeats;
    }

    protected IntegerProperty maxClassFeatsProperty() {
        return maxClassFeats;
    }

    protected IntegerProperty maxFighterBonusFeatsProperty() {
        return maxFighterBonusFeats;
    }

    protected IntegerProperty raceBonusFeatsProperty() {
        return raceBonusFeats;
    }

    private void updateMaxFighterBonusFeats(int fighterLevel) {
        maxFighterBonusFeats.set(FeatCalculator.getMaxFighterBonusFeats(fighterLevel));
    }
}
