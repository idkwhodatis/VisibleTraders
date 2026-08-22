package net.ramixin.visibletraders.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_1297;
import net.minecraft.class_1914;
import net.minecraft.class_3853;
import net.minecraft.class_3988;
import net.minecraft.class_5819;
import net.ramixin.visibletraders.VisibleTraders;
import net.ramixin.visibletraders.threading.FutureMerchantOffer;
import net.ramixin.visibletraders.threading.SerializableListing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_3988.class)
public class AbstractVillagerMixin {

    @WrapOperation(method = "addOffersFromItemListings", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;getOffer(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/util/RandomSource;)Lnet/minecraft/world/item/trading/MerchantOffer;"))
    private class_1914 makeMapTradesFutures(class_3853.class_1652 instance, class_1297 entity, class_5819 randomSource, Operation<class_1914> original) {
        if(!(instance instanceof SerializableListing listing)) return original.call(instance, entity, randomSource);
        FutureMerchantOffer futureOffer = new FutureMerchantOffer(listing, () -> original.call(instance, entity, randomSource));
        if(!VisibleTraders.TRADE_WORKER.isRunning()) {
            VisibleTraders.LOGGER.error("Trade thread is inactive. Restart to re-enable multithreaded trade generation");
            futureOffer.fulfillFuture();
        } else {
            VisibleTraders.TRADE_WORKER.addOrder(futureOffer);
        }
        return futureOffer;
    }

}
