package dev.swim.toh.model.data.weapon;

import dev.swim.toh.definition.weapon.WeaponConfig;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class WeaponRepository {

    private final Map<String, Weapon> weapons = new HashMap<>();

    public WeaponRepository(WeaponConfig config) {
        config.getWeapons()
                .stream()
                .map(WeaponMapper::toWeapon)
                .forEach(weapon -> weapons.put(weapon.getId(), weapon));
    }

    public Weapon getWeapon(String id) {
        return weapons.get(id);
    }

    public Collection<Weapon> getAll() {
        return weapons.values();
    }
}
