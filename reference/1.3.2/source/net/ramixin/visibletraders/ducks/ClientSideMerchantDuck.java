package net.ramixin.visibletraders.ducks;

import net.minecraft.class_1916;
import org.spongepowered.asm.mixin.Unique;

public interface ClientSideMerchantDuck {


    @Unique
    class_1916 visibleTraders$getClientUnlockedTrades();

    @Unique
    void visibleTraders$setClientUnlockedTrades(class_1916 offers);

}
