package net.ramixin.visibletraders.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_1646;
import net.minecraft.class_1657;
import net.minecraft.class_1915;
import net.minecraft.class_1916;
import net.ramixin.visibletraders.ducks.VillagerDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_1915.class)
public interface MerchantMixin {

    @WrapOperation(method = "openTradingScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;sendMerchantOffers(ILnet/minecraft/world/item/trading/MerchantOffers;IIZZ)V"))
    private void sendLockedOffersWithNormalOffersOnScreenOpen(class_1657 instance, int syncId, class_1916 merchantOffers, int j, int k, boolean bl, boolean bl2, Operation<Void> original) {
        if(!(((class_1915)this) instanceof class_1646 villager)) original.call(instance, syncId, merchantOffers, j, k, bl, bl2);
        else {
            VillagerDuck duck = VillagerDuck.of(villager);
            original.call(instance, syncId, duck.visibleTraders$getCombinedOffers(), duck.visibleTraders$getShiftedLevel(), k, bl, bl2);
        }
    }

}
