package net.ramixin.visibletraders.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.class_1309;
import net.minecraft.class_1641;
import net.minecraft.class_1642;
import net.minecraft.class_1646;
import net.minecraft.class_3218;
import net.ramixin.visibletraders.ducks.VillagerDuck;
import net.ramixin.visibletraders.ducks.ZombieVillagerDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_1642.class)
public class ZombieMixin {

    @Inject(method = "killedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/ZombieVillager;setVillagerXp(I)V"))
    private void transferTradesToZombieVillager(class_3218 serverLevel, class_1309 livingEntity, CallbackInfoReturnable<Boolean> cir, @Local class_1646 villager, @Local class_1641 zombieVillager) {
        VillagerDuck.of(villager).visibleTraders$getLockedTradeData().ifPresent(data -> ZombieVillagerDuck.of(zombieVillager).visibleTraders$setLockedTradeData(data));
    }

}
