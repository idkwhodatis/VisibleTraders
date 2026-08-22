package net.ramixin.visibletraders.ducks;

import net.minecraft.class_3195;
import net.minecraft.class_3853;
import net.minecraft.class_6862;
import net.minecraft.class_6880;
import net.minecraft.class_9428;

public interface TreasureMapForEmeraldsDuck {

    int visibleTrades$getEmeraldCost();

    class_6862<class_3195> visibleTrades$getDestination();

    String visibleTrades$getDisplayName();

    class_6880<class_9428> visibleTrades$getDestinationType();

    int visibleTrades$getMaxUses();

    int visibleTrades$getVillagerXp();

    static TreasureMapForEmeraldsDuck get(class_3853.class_1654 listing) {
        return (TreasureMapForEmeraldsDuck) listing;
    }
}
