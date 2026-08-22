package net.ramixin.visibletraders.ducks;

import net.minecraft.class_1646;
import net.minecraft.class_1916;
import net.ramixin.visibletraders.LockedTradeData;

import java.util.Optional;

public interface VillagerDuck {

    static VillagerDuck of(class_1646 villager) {
        return (VillagerDuck)villager;
    }

    void visibleTraders$setLockedTradeData(LockedTradeData data);

    Optional<LockedTradeData> visibleTraders$getLockedTradeData();

    void visibleTrades$regenerateTrades();

    class_1916 visibleTraders$getCombinedOffers();

    int visibleTraders$getShiftedLevel();

    void visibleTraders$updateTrades();
}
