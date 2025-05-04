package dev.swim.toh.model.data.feat;

import dev.swim.toh.model.rules.feat.prerequisites.AttributePrerequisite;
import dev.swim.toh.model.rules.feat.prerequisites.FeatPrerequisite;
import dev.swim.toh.model.data.attribute.AttributeName;

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

        FEAT_MAP.put(FeatName.COMBAT_EXPERTISE, new Feat(
                FeatName.COMBAT_EXPERTISE,
                "Angriffsbonus gegen RK",
                List.of(
                        new AttributePrerequisite(AttributeName.INTELLIGENCE, 13)
                ),
                false,
                false,
                // new ArrayList<>(List.of(WeaponType.values())), // Optionen: alle Waffen
                List.of()
        ));

        FEAT_MAP.put(FeatName.IMPROVED_DISARM, new Feat(
                FeatName.IMPROVED_DISARM,
                "+4 auf Entwaffnen",
                List.of(
                        new AttributePrerequisite(AttributeName.INTELLIGENCE, 13),
                        new FeatPrerequisite(FeatName.COMBAT_EXPERTISE)
                ),
                false,
                false,
                // List.of(),
                List.of()
        ));

//        FEAT_MAP.put(FeatName.IMPROVED_CRITICAL, new Feat(
//                FeatName.IMPROVED_CRITICAL,
//                "Verbesserter kritischer Treffer",
//
//        ))
    }

    public static Feat getFeat(FeatName name) {
        return FEAT_MAP.get(name);
    }

    public static Collection<Feat> getAllFeats() {
        return FEAT_MAP.values();
    }

    public static Collection<Feat> getFeats(Collection<FeatName> featNames) {
        return featNames.stream()
                .filter(FEAT_MAP::containsKey)
                .map(FEAT_MAP::get).toList();
    }
}
