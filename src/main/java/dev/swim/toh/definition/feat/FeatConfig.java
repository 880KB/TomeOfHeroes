package dev.swim.toh.definition.feat;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "feats")
public class FeatConfig {

    private List<FeatDefinition> feats;

    public List<FeatDefinition> getFeats() {
        return feats;
    }

    public void setFeats(List<FeatDefinition> feats) {
        this.feats = feats;
    }
}
