package dev.swim.toh.model.data.feat;

import dev.swim.toh.definition.feat.FeatConfig;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class FeatRepository {

    private final Map<String, Feat> feats = new HashMap<>();

    public FeatRepository(FeatConfig config) {
        config.getFeats()
                .stream()
                .map(FeatMapper::toFeat)
                .forEach(feat -> feats.put(feat.getId(), feat));
    }

    public Feat getFeat(String id) {
        return feats.get(id);
    }

    public Collection<Feat> getAll() {
        return feats.values();
    }
}
