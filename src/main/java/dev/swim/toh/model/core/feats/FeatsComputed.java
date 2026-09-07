package dev.swim.toh.model.core.feats;

import dev.swim.toh.model.calculation.FeatCalculator;
import dev.swim.toh.model.calculation.FeatSlotCalculator;
import dev.swim.toh.model.calculation.FeatSlotCalculator.FeatPool;
import dev.swim.toh.model.core.ComputedBase;
import dev.swim.toh.model.core.classes.ChosenClass;
import dev.swim.toh.model.data.clazz.Clazz;
import dev.swim.toh.model.data.feat.Feat;
import dev.swim.toh.model.rules.FeatRules;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;

import java.util.List;
import java.util.Set;
import java.util.function.IntUnaryOperator;

public class FeatsComputed extends ComputedBase {

    private final FeatRules featRules;

    private final IntegerProperty maxFeats = new SimpleIntegerProperty(0);
    private final IntegerProperty maxClassFeats = new SimpleIntegerProperty(0);
    private final IntegerProperty maxFighterBonusFeats = new SimpleIntegerProperty(0);
    private final IntegerProperty maxWizardBonusFeats = new SimpleIntegerProperty(0);
    private final IntegerProperty raceBonusFeats = new SimpleIntegerProperty(0);

    private final IntegerProperty usedClassFeats = new SimpleIntegerProperty(0);
    private final IntegerProperty usedFighterBonusFeats = new SimpleIntegerProperty(0);
    private final IntegerProperty usedWizardBonusFeats = new SimpleIntegerProperty(0);
    private final IntegerProperty usedRaceBonusFeats = new SimpleIntegerProperty(0);

    public FeatsComputed(FeatRules featRules) {
        this.featRules = featRules;
    }

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

        // max fighter/wizard bonus feats
        wireLevelBasedMax(Clazz.FIGHTER, maxFighterBonusFeats, FeatCalculator::getMaxFighterBonusFeats);
        wireLevelBasedMax(Clazz.WIZARD, maxWizardBonusFeats, FeatCalculator::getMaxWizardBonusFeats);

        // max feats
        maxClassFeats.addListener((obs, oldValue, newValue) -> updateMaxFeats());
        maxFighterBonusFeats.addListener((obs, oldValue, newValue) -> updateMaxFeats());
        maxWizardBonusFeats.addListener((obs, oldValue, newValue) -> updateMaxFeats());
        raceBonusFeats.addListener((obs, oldValue, newValue) -> updateMaxFeats());
        updateMaxFeats();

        // used-per-pool counts: each selected feat's pool is decided once, at the moment it's
        // added (see assignPool), and frozen from then on - a later level-up changing a max*
        // property must NOT reshuffle which pool an already-selected feat counts against, only
        // affect what's available to the *next* pick. So these are a live count of already-frozen
        // assignments, not a recomputed allocation - a FilteredList reacts to both list
        // add/remove and to the in-place pool-property change assignPool makes right after
        // adding (see the extractor on FeatsInput.featList).
        FilteredList<SelectedFeat> fighterBonusFeats = new FilteredList<>(characterModel.feats.getFeatList(),
                sf -> sf.getPool() == FeatPool.FIGHTER_BONUS);
        FilteredList<SelectedFeat> wizardBonusFeats = new FilteredList<>(characterModel.feats.getFeatList(),
                sf -> sf.getPool() == FeatPool.WIZARD_BONUS);
        FilteredList<SelectedFeat> raceBonusSelectedFeats = new FilteredList<>(characterModel.feats.getFeatList(),
                sf -> sf.getPool() == FeatPool.RACE_BONUS);
        FilteredList<SelectedFeat> classFeats = new FilteredList<>(characterModel.feats.getFeatList(),
                sf -> sf.getPool() == FeatPool.CLASS);
        usedFighterBonusFeats.bind(Bindings.size(fighterBonusFeats));
        usedWizardBonusFeats.bind(Bindings.size(wizardBonusFeats));
        usedRaceBonusFeats.bind(Bindings.size(raceBonusSelectedFeats));
        usedClassFeats.bind(Bindings.size(classFeats));
    }

    /**
     * Wires target to the level of the character's ChosenClass of the given clazz (0 if absent),
     * for classes already present at startup and for ones added/removed later - mirrors the
     * shape needed for both Fighter and Wizard bonus-feat pools.
     */
    private void wireLevelBasedMax(Clazz clazz, IntegerProperty target, IntUnaryOperator formula) {
        for (ChosenClass chosenClass : characterModel.classes.getClassList()) {
            if (chosenClass.clazzProperty().get() == clazz) {
                chosenClass.levelProperty().addListener((obs, oldLevel, newLevel) ->
                        target.set(formula.applyAsInt(newLevel.intValue())));
                target.set(formula.applyAsInt(chosenClass.levelProperty().get()));
            }
        }

        characterModel.classes.getClassList().addListener((ListChangeListener<? super ChosenClass>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (ChosenClass chosenClass : c.getAddedSubList()) {
                        if (chosenClass.clazzProperty().get() == clazz) {
                            chosenClass.levelProperty().addListener((obs, oldLevel, newLevel) ->
                                    target.set(formula.applyAsInt(newLevel.intValue())));
                            target.set(formula.applyAsInt(chosenClass.levelProperty().get()));
                        }
                    }
                }
                else if (c.wasRemoved()) {
                    for (ChosenClass removedClass : c.getRemoved()) {
                        if (removedClass.clazzProperty().get() == clazz) {
                            target.set(0);
                        }
                    }
                }
            }
        });
    }

    private void updateMaxFeats() {
        maxFeats.set(maxClassFeats.get() + maxFighterBonusFeats.get() + maxWizardBonusFeats.get() + raceBonusFeats.get());
    }

    /**
     * Decides, once and for good, which pool a newly added feat consumes (Kämpfer-Bonus,
     * Magier-Bonus, Rasse, Allg. - in that fixed priority) via the pure FeatSlotCalculator, fed
     * with the *currently remaining* capacity of each pool (max minus what's already frozen into
     * it) rather than the raw maxima. Must be called exactly once, right after the feat is added
     * to the list - never re-run later, since a later level-up must not retroactively reshuffle
     * an already-made choice.
     */
    protected void assignPool(SelectedFeat selectedFeat) {
        Feat feat = selectedFeat.featProperty().get();
        Set<Feat> fighterBonusEligible = featRules.isFighterBonusFeatEligible(feat) ? Set.of(feat) : Set.of();
        Set<Feat> wizardBonusEligible = featRules.isWizardBonusFeatEligible(feat) ? Set.of(feat) : Set.of();
        FeatSlotCalculator.Capacities capacities = new FeatSlotCalculator.Capacities(
                maxFighterBonusFeats.get() - usedFighterBonusFeats.get(),
                maxWizardBonusFeats.get() - usedWizardBonusFeats.get(),
                raceBonusFeats.get() - usedRaceBonusFeats.get());
        FeatPool pool = FeatSlotCalculator.allocate(List.of(feat), fighterBonusEligible, wizardBonusEligible, capacities).get(0);
        selectedFeat.poolProperty().set(pool);
    }

    protected IntegerProperty maxFeatsProperty() {
        return maxFeats;
    }

    protected IntegerProperty maxClassFeatsProperty() {
        return maxClassFeats;
    }

    protected IntegerProperty maxFighterBonusFeatsProperty() {
        return maxFighterBonusFeats;
    }

    protected IntegerProperty maxWizardBonusFeatsProperty() {
        return maxWizardBonusFeats;
    }

    protected IntegerProperty raceBonusFeatsProperty() {
        return raceBonusFeats;
    }

    protected IntegerProperty usedClassFeatsProperty() {
        return usedClassFeats;
    }

    protected IntegerProperty usedFighterBonusFeatsProperty() {
        return usedFighterBonusFeats;
    }

    protected IntegerProperty usedWizardBonusFeatsProperty() {
        return usedWizardBonusFeats;
    }

    protected IntegerProperty usedRaceBonusFeatsProperty() {
        return usedRaceBonusFeats;
    }
}
