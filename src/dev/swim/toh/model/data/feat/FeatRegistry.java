package dev.swim.toh.model.data.feat;

import dev.swim.toh.model.core.feats.prerequisites.FeatPrerequisite;
import dev.swim.toh.model.data.clazz.Clazz;

import java.util.*;

public class FeatRegistry {
    private static final Map<FeatName, Feat> FEAT_MAP = new HashMap<>();

    static {

        FEAT_MAP.put(FeatName.ALERTNESS, new Feat(
                FeatName.ALERTNESS,
                "You gain a +2 bonus on Listen and Spot checks.",
                List.of(),
                false,
                false,
                // List.of(),          // keine Optionen
                List.of()
        ));

        FEAT_MAP.put(FeatName.WEAPON_FOCUS, new Feat(
                FeatName.WEAPON_FOCUS,
                "Beschreibung",
                List.of(
                        new FeatPrerequisite(FeatName.WEAPON_SPECIALIZATION)
                ),
                true,
                true,
                // new ArrayList<>(List.of(WeaponType.values())), // Optionen: alle Waffen
                List.of()
        ));

        FEAT_MAP.put(FeatName.SCRIBE_SCROLL, new Feat(
                FeatName.SCRIBE_SCROLL,
                "auch Beschreibung",
                List.of(),
                false,
                false,
                // List.of(),
                List.of(Clazz.WIZARD)
        ));
    }

    public static Feat getFeat(FeatName name) {
        return FEAT_MAP.get(name);
    }

    public static Collection<Feat> getAllFeats() {
        return FEAT_MAP.values();
    }
}
