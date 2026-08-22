package net.ramixin.visibletraders.mixins;

import net.minecraft.class_1728;
import net.minecraft.class_1914;
import net.minecraft.class_1915;
import net.minecraft.class_1916;
import net.ramixin.visibletraders.ducks.ClientSideMerchantDuck;
import net.ramixin.visibletraders.ducks.MerchantMenuDuck;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(class_1728.class)
public abstract class MerchantMenuMixin implements MerchantMenuDuck {

    @Shadow private int merchantLevel;
    @Shadow @Final private class_1915 trader;

    @Shadow public abstract class_1916 getOffers();

    @Unique
    private int unlockedTradeCount = 0;

    @Inject(method = "setMerchantLevel", at = @At("TAIL"))
    private void readUnlockedTradeCountFromLevel(int i, CallbackInfo ci) {
        unlockedTradeCount = i >> 8;
        if(this.trader instanceof ClientSideMerchantDuck duck) {
            List<class_1914> list = getOffers().subList(0, unlockedTradeCount);
            class_1916 offers = new class_1916();
            offers.addAll(list);
            duck.visibleTraders$setClientUnlockedTrades(offers);
        }
        this.merchantLevel = i & 255;
    }


    @Override
    public boolean visibleTraders$shouldAllowTrade(int i) {
        if(unlockedTradeCount == 0) return true;
        return i <= unlockedTradeCount-1;
    }
}
