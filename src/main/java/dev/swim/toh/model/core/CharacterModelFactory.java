package dev.swim.toh.model.core;

import dev.swim.toh.model.data.feat.FeatRepository;
import dev.swim.toh.model.data.weapon.WeaponRepository;
import dev.swim.toh.model.rules.FeatRules;
import org.springframework.stereotype.Component;

@Component
public class CharacterModelFactory {

    private final FeatRepository featRepository;
    private final FeatRules featRules;
    private final WeaponRepository weaponRepository;

    public CharacterModelFactory(FeatRepository featRepository, FeatRules featRules, WeaponRepository weaponRepository) {
        this.featRepository = featRepository;
        this.featRules = featRules;
        this.weaponRepository = weaponRepository;
    }

    public CharacterModel createCharacter() {
        CharacterModel characterModel = new CharacterModel(featRepository, featRules, weaponRepository);
        characterModel.initTestData();
        return characterModel;
    }

    public CharacterModel createEmptyCharacter() {
        return new CharacterModel(featRepository, featRules, weaponRepository);
    }
}
