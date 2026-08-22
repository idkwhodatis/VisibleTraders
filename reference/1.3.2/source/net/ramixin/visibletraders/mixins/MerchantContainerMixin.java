package net.ramixin.visibletraders.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_1725;
import net.minecraft.class_1915;
import net.minecraft.class_1916;
import net.ramixin.visibletraders.ducks.ClientSideMerchantDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(class_1725.class)
public class MerchantContainerMixin {

    @WrapOperation(method = "updateSellItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/trading/Merchant;getOffers()Lnet/minecraft/world/item/trading/MerchantOffers;"))
    private class_1916 removeTradeAutoFillIfLocked(class_1915 instance, Operation<class_1916> original) {
        class_1916 offers = original.call(instance);
        if(instance instanceof ClientSideMerchantDuck duck) {
            class_1916 unlockedOffers = duck.visibleTraders$getClientUnlockedTrades();
            if(unlockedOffers == null) return offers;
            else return unlockedOffers;
        }
        return offers;
    }

}
