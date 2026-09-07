package dev.swim.toh.model.data.weapon;

import dev.swim.toh.definition.weapon.WeaponDefinition;

import java.util.List;
import java.util.Set;

public class WeaponMapper {

    public static Weapon toWeapon(WeaponDefinition def) {
        return new Weapon(
                def.getId(),
                def.getName(),
                def.getProficiency(),
                def.getRangeType(),
                def.getHandedness(),
                def.getDamageSmall(),
                def.getDamageMedium(),
                def.getSecondaryDamageSmall(),
                def.getSecondaryDamageMedium(),
                def.getCriticalThreatRange(),
                def.getCriticalMultiplier(),
                def.getRangeIncrement(),
                def.getWeight(),
                def.getDamageTypes() == null ? Set.of() : Set.copyOf(def.getDamageTypes()),
                def.isDamageTypeChoice(),
                def.isNonlethal(),
                def.isReach(),
                def.isDoubleWeapon(),
                def.getShortDescription(),
                def.getDescription()
        );
    }

    public static List<Weapon> toWeapons(List<WeaponDefinition> defs) {
        return defs.stream().map(WeaponMapper::toWeapon).toList();
    }
}
