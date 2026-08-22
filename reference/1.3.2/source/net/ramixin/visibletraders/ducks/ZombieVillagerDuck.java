package net.ramixin.visibletraders.ducks;

import net.minecraft.class_1641;
import net.ramixin.visibletraders.LockedTradeData;

public interface ZombieVillagerDuck {

    static ZombieVillagerDuck of(class_1641 zombieVillager) {
        return (ZombieVillagerDuck)zombieVillager;
    }

    void visibleTraders$setLockedTradeData(LockedTradeData data);

}
