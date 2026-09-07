package dev.swim.toh.definition.weapon;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "weapons")
public class WeaponConfig {

    private List<WeaponDefinition> weapons;

    public List<WeaponDefinition> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<WeaponDefinition> weapons) {
        this.weapons = weapons;
    }
}
