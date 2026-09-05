package dev.swim.toh.model.core;

import dev.swim.toh.model.data.feat.FeatRepository;
import dev.swim.toh.model.rules.FeatRules;
import org.springframework.stereotype.Component;

@Component
public class CharacterModelFactory {

    private final FeatRepository featRepository;
    private final FeatRules featRules;

    public CharacterModelFactory(FeatRepository featRepository, FeatRules featRules) {
        this.featRepository = featRepository;
        this.featRules = featRules;
    }

    public CharacterModel createCharacter() {
        return new CharacterModel(featRepository, featRules);
    }
}
