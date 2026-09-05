package dev.swim.toh.model.data.feat;

import dev.swim.toh.model.core.CharacterModel;

public interface Benefit {
    void apply(CharacterModel characterModel, Object sourceId);

    void remove(CharacterModel characterModel, Object sourceId);
}
