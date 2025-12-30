package dev.swim.toh.model.data.feat;

import dev.swim.toh.definition.feat.FeatConfig;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

@Component
public class FeatRepository {

    private final Map<FeatName, Feat> feats = new EnumMap<>(FeatName.class);

    public FeatRepository(FeatConfig config) {
        config.getFeats()
                .stream()
                .map(FeatMapper::toFeat)
                .forEach(feat -> feats.put(feat.getName(), feat));
    }

    public Feat getFeat(FeatName name) {
        return feats.get(name);
    }

    public Collection<Feat> getAll() {
        return feats.values();
    }
}
