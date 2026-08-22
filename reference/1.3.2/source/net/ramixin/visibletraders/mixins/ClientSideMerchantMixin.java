package net.ramixin.visibletraders.mixins;

import net.minecraft.class_1645;
import net.minecraft.class_1916;
import net.ramixin.visibletraders.ducks.ClientSideMerchantDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(class_1645.class)
public class ClientSideMerchantMixin implements ClientSideMerchantDuck {

    @Unique
    private class_1916 clientUnlockedTrades = null;


    @Unique
    @Override
    public class_1916 visibleTraders$getClientUnlockedTrades() {
        return clientUnlockedTrades;
    }

    @Unique
    @Override
    public void visibleTraders$setClientUnlockedTrades(class_1916 offers) {
        this.clientUnlockedTrades = offers;
    }

}
